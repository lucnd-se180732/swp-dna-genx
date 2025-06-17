package com.genx.service.impl;

    import com.genx.dto.response.BookingResponse;
    import com.genx.dto.response.BookingSummaryResponse;
    import com.genx.entity.Booking;
    import com.genx.enums.EBookingStatus;
    import com.genx.mapper.BookingMapper;


    import com.genx.repository.IBookingRepository;
    import com.genx.service.interfaces.IBookingService;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

@Service
    public class BookingServiceImpl implements IBookingService {

        @Autowired
        private IBookingRepository bookingRepository;

        @Autowired
        private  BookingMapper bookingMapper;


        @Override
        public Page<BookingResponse> getAllBookings(Pageable pageable) {
            return bookingRepository.findAll(pageable)
                    .map(bookingMapper::toResponse);
        }

        @Override
        public Page<BookingSummaryResponse> searchBookingSummaries(EBookingStatus status, Long bookingId, Pageable pageable) {
            return bookingRepository.searchByStatusAndBookingId(status, bookingId, pageable)
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