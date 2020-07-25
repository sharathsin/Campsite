package com.campsite.dao;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;

import com.campsite.dto.BookingDTO;

@Component
public interface BookingDao {
   BookingDTO createBooking(BookingDTO bookingDTO);

   void cancelBooking(List<BookingDTO> bookingDTOList);

   List<BookingDTO> findBooking(String bookingId);

   List<BookingDTO> findBookingsBetweenDates(LocalDate start, LocalDate end);
}
