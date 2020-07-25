package com.campsite.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.campsite.dto.BookingDTO;

@Repository
public interface BookingRepository extends JpaRepository<BookingDTO, Long> {



    List<BookingDTO> findByBookingId(String bookingId);

    @Query(value = "from booking t where bookingDate BETWEEN :startDate AND :endDate")
     List<BookingDTO> getAllBetweenDates(@Param("startDate") LocalDate startDate,@Param("endDate")LocalDate endDate);
}

