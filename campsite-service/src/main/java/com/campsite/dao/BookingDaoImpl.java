package com.campsite.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Component;

import com.campsite.dto.BookingDTO;
import com.campsite.repository.BookingRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BookingDaoImpl implements BookingDao {
    private final BookingRepository bookingRepository;

    @Override
    public BookingDTO createBooking(BookingDTO bookingDTO) {
        return bookingRepository.save(bookingDTO);
    }


    @Override
    public void cancelBooking(List<BookingDTO> bookingDTOList) {
        bookingRepository.deleteInBatch(bookingDTOList);
    }

    @Override
    public List<BookingDTO> findBooking(String bookingId) {
        return bookingRepository.findByBookingId(bookingId);
    }

    @Override
    public List<BookingDTO> findBookingsBetweenDates(LocalDate start, LocalDate end) {
        return bookingRepository.getAllBetweenDates(start, end);
    }

}
