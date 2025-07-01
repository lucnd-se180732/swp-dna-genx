package com.genx.service.impl;

    import com.genx.dto.response.BookingResponse;
    import com.genx.dto.response.BookingSummaryResponse;
    import com.genx.entity.Booking;
    import com.genx.entity.Participant;
    import com.genx.entity.SampleCollection;
    import com.genx.enums.EBookingStatus;
    import com.genx.enums.EParticipantSampleStatus;
    import com.genx.enums.EPaymentStatus;
    import com.genx.enums.ESampleCollectionStatus;
    import com.genx.mapper.BookingMapper;


    import com.genx.repository.IBookingRepository;
    import com.genx.repository.ISampleCollectionRepository;
    import com.genx.service.interfaces.IBookingService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.time.LocalDateTime;
    import java.util.Optional;

@Service
    public class BookingServiceImpl implements IBookingService {

        @Autowired
        private IBookingRepository bookingRepository;

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
            Booking booking = bookingRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Booking not found with id: " + id));
            booking.setStatus(EBookingStatus.CONFIRMED);
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

    @Override
    public Optional<Long> getTodayRevenue(EPaymentStatus status) {
        return bookingRepository.sumTodayRevenue(status);
    }

    @Override
    public Optional<Long> getMonthlyRevenue(EPaymentStatus status, int month, int year) {
        return bookingRepository.sumMonthlyRevenue(status, month, year);
    }

    @Override
    public long countByPaymentStatus(EPaymentStatus status) {
        return bookingRepository.countByPaymentStatus(status);
    }

}