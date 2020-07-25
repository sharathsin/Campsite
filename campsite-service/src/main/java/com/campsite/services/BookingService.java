package com.campsite.services;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.List;

import com.campsite.exception.CampsiteException;
import com.campsite.exception.CampsiteValidationException;
import com.campsite.model.input.CreateBooking;
import com.campsite.model.input.ModifyBooking;
import com.campsite.model.output.BookingOutput;

public interface BookingService {
     BookingOutput bookCampsite(CreateBooking createBooking) throws ParseException, CampsiteValidationException;

     void cancelBooking(String bookingId) throws CampsiteException;

     BookingOutput modifyCampsite(ModifyBooking modifyBooking) throws ParseException, CampsiteException, CampsiteValidationException;

     List<LocalDate> getAvailableDates(String startDate, String endDate) throws CampsiteValidationException;

}
