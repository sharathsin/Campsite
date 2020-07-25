package com.campsite.controllers;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Calendar;

import javax.validation.Valid;

import org.apache.http.HttpStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.campsite.exception.CampsiteException;
import com.campsite.exception.CampsiteValidationException;
import com.campsite.model.error.ErrorOutput;
import com.campsite.model.input.CreateBooking;
import com.campsite.model.input.ModifyBooking;
import com.campsite.model.output.BookingOutput;
import com.campsite.services.BookingService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@Api( tags = "Campsite Booking System")
@RequestMapping("/v1")
@RequiredArgsConstructor
public class CampsiteController {

    private final BookingService bookingService;

    @ApiOperation("Book Campsite for a  particular Date")
    @PostMapping(path = "/bookReservation", produces = "application/json")

    @ApiResponses({
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Internal error, sub service failure, server crash", response = ErrorOutput.class),
            @ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Input data errors", response = ErrorOutput.class),
            @ApiResponse(code = HttpStatus.SC_CONFLICT, message = "Conflict", response = ErrorOutput.class),
            @ApiResponse(code = HttpStatus.SC_OK,message = "Booking Successful", response = BookingOutput.class)

    })
    public ResponseEntity bookCampsite(@RequestBody @Valid CreateBooking createBooking) throws ParseException, CampsiteValidationException {
     try {
         return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(bookingService.bookCampsite(createBooking));
     }
     catch (DataIntegrityViolationException exception) {
         ErrorOutput errorOutput = new ErrorOutput(HttpStatus.SC_CONFLICT,"Conflict" ,"Campsite not available in requested dates ","campsite", Calendar.getInstance().getTime(),null);
         return ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT).body(errorOutput);
     }

    }

    @ApiOperation("Cancel a Booking")
    @DeleteMapping(path = "/cancelReservation", produces = "application/json")

    @ApiResponses({
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Internal error, sub service failure, server crash", response = ErrorOutput.class),
            @ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Input data errors", response = ErrorOutput.class),
            @ApiResponse(code = HttpStatus.SC_CONFLICT, message = "Conflict", response = ErrorOutput.class),
            @ApiResponse(code = HttpStatus.SC_NO_CONTENT,message = "Booking Cancelled")

    })
    public ResponseEntity cancelBooking(@RequestParam @Valid String bookingId) throws ParseException {
        try {
            bookingService.cancelBooking(bookingId);
        } catch (CampsiteException exception) {
            ErrorOutput errorOutput = new ErrorOutput(exception.getCode(),"NotFound" ,"Invalid Bookingid ","campsite", Calendar.getInstance().getTime(),null);
            return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).body(errorOutput);
        }
        return ResponseEntity.status(org.springframework.http.HttpStatus.NO_CONTENT).build() ;

    }

    @ApiOperation("Modify a booking")
    @PostMapping(path = "/modifyReservation", produces = "application/json")
    @ApiResponses({
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Internal error, sub service failure, server crash", response = ErrorOutput.class),
            @ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Input data errors", response = ErrorOutput.class),
            @ApiResponse(code = HttpStatus.SC_CONFLICT, message = "Conflict", response = ErrorOutput.class),
            @ApiResponse(code = HttpStatus.SC_CREATED,message = "Booking Successful", response = BookingOutput.class)

    })
    @ResponseStatus(org.springframework.http.HttpStatus.CREATED)
    public ResponseEntity modifyBooking(@RequestBody @Valid ModifyBooking modifyBooking) throws ParseException, CampsiteValidationException {
        try {
            return ResponseEntity.status(org.springframework.http.HttpStatus.CREATED).body(bookingService.modifyCampsite(modifyBooking));
        }
        catch (DataIntegrityViolationException  exception) {
            ErrorOutput errorOutput = new ErrorOutput(HttpStatus.SC_CONFLICT,"Conflict" ,"Campsite not available in requested dates. Cannot modify the reservation ","campsite", Calendar.getInstance().getTime(),null);
            return ResponseEntity.status(org.springframework.http.HttpStatus.CONFLICT).body(errorOutput);
        }
        catch (CampsiteException exception) {
            ErrorOutput errorOutput = new ErrorOutput(exception.getCode(),"NotFound" ,"Invalid Bookingid ","campsite", Calendar.getInstance().getTime(),null);
            return ResponseEntity.status(org.springframework.http.HttpStatus.NOT_FOUND).body(errorOutput);
        }
    }

    @ApiOperation("Get All Available Dates")
    @GetMapping(path = "/getAvailableDates",produces = "application/json")
    @ResponseStatus(org.springframework.http.HttpStatus.OK)
    @ApiResponses({
            @ApiResponse(code = HttpStatus.SC_INTERNAL_SERVER_ERROR, message = "Internal error, sub service failure, server crash", response = ErrorOutput.class),
            @ApiResponse(code = HttpStatus.SC_BAD_REQUEST, message = "Input data errors", response = ErrorOutput.class),
            @ApiResponse(code = HttpStatus.SC_OK,message = "RetrievedSuccessful", response = LocalDate[].class)

    })
    public ResponseEntity getAvailableDates(@ApiParam(required = true) @RequestParam String startDate, @ApiParam(required = false) @RequestParam(required = false)  String endDate)
            throws CampsiteValidationException {
        return ResponseEntity.ok(bookingService.getAvailableDates(startDate,endDate)) ;

    }
}
