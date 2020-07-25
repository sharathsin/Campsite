package com.campsite.services;

import java.text.ParseException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.transaction.Transactional;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.campsite.dao.BookingDao;
import com.campsite.dto.BookingDTO;
import com.campsite.exception.CampsiteException;
import com.campsite.exception.CampsiteValidationException;
import com.campsite.model.error.ValidationError;
import com.campsite.model.input.CreateBooking;
import com.campsite.model.input.ModifyBooking;
import com.campsite.model.output.BookingOutput;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookingServiceImpl implements BookingService {

    private final BookingDao bookingDao;

    @Override
    @Transactional
    public BookingOutput bookCampsite(CreateBooking createBooking) throws ParseException, CampsiteValidationException {
        validate(createBooking);
        final UUID bookingId = UUID.randomUUID();
        List<BookingDTO> bookingDTOList = mapBooking(createBooking);
        saveBookings(bookingId, bookingDTOList);
        BookingOutput bookingOutput = new BookingOutput();
        bookingOutput.setBookingId(bookingId.toString());
        return bookingOutput;
    }

    @Override
    @Transactional
    public void cancelBooking(String bookingId) throws CampsiteException {
        final List<BookingDTO> bookingDTOList = bookingDao.findBooking(bookingId);
        if (bookingDTOList.isEmpty()) {
            throw new CampsiteException("Booking Not Found", HttpStatus.NOT_FOUND.value());
        }
        bookingDao.cancelBooking(bookingDTOList);
    }

    @Override
    @Transactional
    public BookingOutput modifyCampsite(ModifyBooking modifyBooking) throws CampsiteException, ParseException, CampsiteValidationException {
        validate(modifyBooking);
        final List<BookingDTO> bookingDTOList = bookingDao.findBooking(modifyBooking.getBookingId());
        List<BookingDTO> cancelBookings = new ArrayList<>();
        List<BookingDTO> toBeSavedBookings = new ArrayList<>();
        BookingOutput bookingOutput = new BookingOutput();
        bookingOutput.setBookingId(modifyBooking.getBookingId());
        if (bookingDTOList.isEmpty()) {
            throw new CampsiteException("Booking Not Found", HttpStatus.NOT_FOUND.value());
        } else {
            List<BookingDTO> toBeModified = mapBooking(modifyBooking);
            filterToBeCancelledBookings(bookingDTOList, cancelBookings, toBeModified);
            replaceToBeSavedBookings(bookingDTOList, toBeSavedBookings, toBeModified);
            saveBookings(UUID.fromString(modifyBooking.getBookingId()), bookingDTOList);
            bookingDao.cancelBooking(cancelBookings);
        }
        return bookingOutput;
    }

    @Override
    public List<LocalDate> getAvailableDates(String startDate, String endDate) throws CampsiteValidationException {
        LocalDate startLocalDate = convertDate(startDate);
        LocalDate endLocalDate;
        if (!StringUtils.isEmpty(endDate)) {
            endLocalDate = convertDate(endDate);
        } else {
            endLocalDate = startLocalDate.plusMonths(1);
        }
        validateDate(startLocalDate, endLocalDate);
        long numOfDays = ChronoUnit.DAYS.between(startLocalDate, endLocalDate);

        List<LocalDate> listOfDates = getAllDatesBetweenTwoDates(startLocalDate, numOfDays);
        List<BookingDTO> bookingDTOList = bookingDao.findBookingsBetweenDates(startLocalDate, endLocalDate);
        for (BookingDTO bookingDTO : bookingDTOList) {
            if (listOfDates.contains(bookingDTO.getBookingDate())) {
                listOfDates.remove(bookingDTO.getBookingDate());
            }
        }

        return listOfDates;
    }

    private List<LocalDate> getAllDatesBetweenTwoDates(LocalDate startDate, long numOfDays) {
        return Stream.iterate(startDate, date -> date.plusDays(1)).limit(numOfDays).collect(Collectors.toList());
    }

    private void replaceToBeSavedBookings(List<BookingDTO> bookingDTOList, List<BookingDTO> toBeSavedBookings, List<BookingDTO> toBeModified) {
        for (BookingDTO bookingDTO : toBeModified) {
            if (!bookingDTOList.contains(bookingDTO)) {
                toBeSavedBookings.add(bookingDTO);
            }

        }
    }

    private void filterToBeCancelledBookings(List<BookingDTO> bookingDTOList, List<BookingDTO> cancelBookings, List<BookingDTO> toBeModified) {
        for (BookingDTO bookingDTO : bookingDTOList) {
            if (!toBeModified.contains(bookingDTO)) {
                cancelBookings.add(bookingDTO);
            }

        }
    }

    private List<BookingDTO> mapBooking(CreateBooking createBooking) {
        LocalDate startDate = convertDate(createBooking.getArrivalDate());
        LocalDate endDate = convertDate(createBooking.getDepartureDate());
        long duration = ChronoUnit.DAYS.between(startDate, endDate);
        List<BookingDTO> bookingDTOList = new ArrayList<>();

        for (int i = 0; i < duration; i++) {
            BookingDTO bookingDTO = new BookingDTO();
            bookingDTO.setBookingDate(startDate.plusDays(i));
            bookingDTO.setEmail(createBooking.getEmail());
            bookingDTO.setName(createBooking.getName());
            bookingDTOList.add(bookingDTO);
        }

        return bookingDTOList;
    }

    private LocalDate convertDate(String input) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate date1 = LocalDate.parse(input, dtf);
        return date1;
    }

    private void saveBookings(UUID bookingId, List<BookingDTO> bookingDTOList) {
        for (BookingDTO bookingDTO : bookingDTOList) {
            bookingDTO.setBookingId(bookingId.toString());
            bookingDao.createBooking(bookingDTO);
        }
    }

    private void validate(CreateBooking createBooking) throws CampsiteValidationException {
        LocalDate startDate = convertDate(createBooking.getArrivalDate());
        LocalDate endDate = convertDate(createBooking.getDepartureDate());


        if(startDate.isBefore(LocalDate.now())) {
            throw new CampsiteValidationException("BAD REQUEST", 400, Collections
                    .singletonList(
                            new ValidationError(createBooking.getClass().getSimpleName(), "arrivalDate", "arrivalDate cannot be in the past")));
        }
        if (startDate.isAfter(endDate)) {
            throw new CampsiteValidationException("BAD REQUEST", 400, Collections
                    .singletonList(
                            new ValidationError(createBooking.getClass().getSimpleName(), "arrivalDate", "arrivalDate cannotbe after departureDate")));
        }
        long duration = ChronoUnit.DAYS.between(startDate, endDate);

        if(duration >3) {
            throw new CampsiteValidationException("BAD REQUEST", 400, Collections
                    .singletonList(
                            new ValidationError(createBooking.getClass().getSimpleName(), "arrivalDate", "you cannot book campsite for morethan 3days")));
        }
        duration = ChronoUnit.MONTHS.between(LocalDate.now(), startDate);

        if(duration > 1) {
            throw new CampsiteValidationException("BAD REQUEST", 400, Collections
                    .singletonList(
                            new ValidationError(createBooking.getClass().getSimpleName(), "arrivalDate", "you cannot book more than 1month ahead")));
        }

    }

    private void validateDate(LocalDate startDate, LocalDate endDate) throws CampsiteValidationException {
        if (startDate.isAfter(endDate)) {
            throw new CampsiteValidationException("BAD REQUEST", 400, Collections
                    .singletonList(
                            new ValidationError("startdate", "startdate", "startdate cannotbe after enddate")));
        }

    }
}
