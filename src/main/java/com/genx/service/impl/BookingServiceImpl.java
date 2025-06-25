package com.genx.service.impl;

    import com.genx.dto.response.BookingResponse;
    import com.genx.dto.response.BookingSummaryResponse;
    import com.genx.entity.*;
    import com.genx.enums.EBookingStatus;
    import com.genx.enums.EParticipantSampleStatus;
    import com.genx.enums.ESampleCollectionStatus;
    import com.genx.mapper.BookingMapper;


    import com.genx.repository.IBookingRepository;
    import com.genx.repository.IParticipantRepository;
    import com.genx.repository.ISampleCollectionRepository;
    import com.genx.repository.IStaffInfoRepository;
    import com.genx.security.SecurityUtil;
    import com.genx.service.interfaces.IBookingService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.time.LocalDateTime;

@Service
    public class BookingServiceImpl implements IBookingService {

        @Autowired
        private IBookingRepository bookingRepository;

        @Autowired
        private IStaffInfoRepository staffInfoRepository;;

        @Autowired
        private  BookingMapper bookingMapper;

        @Autowired
        private ISampleCollectionRepository sampleCollectionRepository;

        @Override
        public Page<BookingResponse> getAllBookings(Pageable pageable) {
            return bookingRepository.findAll(pageable)
                    .map(bookingMapper::toResponse);
        }

        @Override
        public Page<BookingSummaryResponse> searchBookingSummaries(EBookingStatus status, Long id, Pageable pageable) {
            return bookingRepository.searchByStatusAndBookingId(status, id, pageable)
                    .map(bookingMapper::toSummary);
        }

        @Override
        public BookingResponse getBookingById(Long id) {
            return bookingRepository.findById(id)
                    .map(bookingMapper::toResponse)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
        }


        @Override
        @Transactional
        public BookingResponse confirmBooking(Long id) {
            Long currentUser = SecurityUtil.getCurrentUserId()
                    .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng hiện tại"));

            StaffInfo staffInfo = staffInfoRepository.findByUserId(currentUser);
            if (staffInfo == null) {
                throw new IllegalArgumentException("Không tìm thấy thông tin nhân viên");
            }
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
            booking.setStatus(EBookingStatus.CONFIRMED);
            booking.setRecordStaff(staffInfo);
            for (Participant p : booking.getParticipants()) {
                p.setSampleStatus(EParticipantSampleStatus.PENDING);
            }

            // Tạo bản ghi SampleCollection nếu chưa có
            if (sampleCollectionRepository.findByBooking_Id(booking.getId()).isEmpty()) {
                SampleCollection sc = new SampleCollection();
                sc.setBooking(booking);
                sc.setStatus(ESampleCollectionStatus.COLLECTING);
                sc.setCollectedAt(LocalDateTime.now());
                sampleCollectionRepository.save(sc);
            }

            return bookingMapper.toResponse(bookingRepository.save(booking));
        }

        @Override
        @Transactional
        public BookingResponse cancelBooking(Long id, String reason) {
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
            booking.setStatus(EBookingStatus.CANCELED);
            booking.setNote(reason);
            return bookingMapper.toResponse(bookingRepository.save(booking));
        }

        @Override
        public Page<BookingResponse> searchBookings(EBookingStatus status, Long bookingId, Pageable pageable) {
            return bookingRepository.searchByStatusAndBookingId(status, bookingId, pageable)
                    .map(bookingMapper::toResponse);
        }

    }