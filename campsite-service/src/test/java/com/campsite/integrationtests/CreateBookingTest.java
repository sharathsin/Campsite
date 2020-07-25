package com.campsite.integrationtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import com.campsite.model.error.ErrorOutput;
import com.campsite.model.input.CreateBooking;
import com.campsite.model.output.BookingOutput;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class CreateBookingTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper jsonMapper;


    @Test
    public void  bookCampsite_givenArrivalDateIsBeforeCurrentDate_shouldReturn400() throws Exception {
        CreateBooking createBooking = new CreateBooking();
        createBooking.setArrivalDate("2020-01-01");
        createBooking.setDepartureDate("2020-01-04");
        createBooking.setEmail("test@gmail.com");
        createBooking.setName("test");
        String inputPayload = jsonMapper.writeValueAsString(createBooking);

        String response = mockMvc.perform(post(getTargetUrlPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputPayload))
                .andExpect(status().is(400))
                .andReturn().getResponse().getContentAsString();

        ErrorOutput errorOutput = jsonMapper.readValue(response, ErrorOutput.class);
        assertEquals(1,errorOutput.getValidationErrors().size());
        assertEquals("arrivalDate cannot be in the past", errorOutput.getValidationErrors().get(0).getMessage());
    }


    @Test
    public void  bookCampsite_givenArrivalDateisAfterDepartureDate_shouldReturn400() throws Exception {
        CreateBooking createBooking = new CreateBooking();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        createBooking.setArrivalDate(LocalDate.now().plusDays(3).format(dtf));
        createBooking.setDepartureDate(LocalDate.now().plusDays(1).toString());
        createBooking.setEmail("test@gmail.com");
        createBooking.setName("test");
        String inputPayload = jsonMapper.writeValueAsString(createBooking);

        String response = mockMvc.perform(post(getTargetUrlPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputPayload))
                .andExpect(status().is(400))
                .andReturn().getResponse().getContentAsString();

        ErrorOutput errorOutput = jsonMapper.readValue(response, ErrorOutput.class);
        assertEquals(1,errorOutput.getValidationErrors().size());
        assertEquals("arrivalDate cannotbe after departureDate", errorOutput.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void  bookCampsite_givenArrivalDateis1monthAheadOfNow_shouldReturn400() throws Exception {
        CreateBooking createBooking = new CreateBooking();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        createBooking.setArrivalDate(LocalDate.now().plusMonths(3).format(dtf));
        createBooking.setDepartureDate(LocalDate.now().plusMonths(3).toString());
        createBooking.setEmail("test@gmail.com");
        createBooking.setName("test");
        String inputPayload = jsonMapper.writeValueAsString(createBooking);

        String response = mockMvc.perform(post(getTargetUrlPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputPayload))
                .andExpect(status().is(400))
                .andReturn().getResponse().getContentAsString();

        ErrorOutput errorOutput = jsonMapper.readValue(response, ErrorOutput.class);
        assertEquals(1,errorOutput.getValidationErrors().size());
        assertEquals("you cannot book more than 1month ahead", errorOutput.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void  bookCampsite_givenNumberOfDaysIsMoreThan3_shouldReturn400() throws Exception {
        CreateBooking createBooking = new CreateBooking();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        createBooking.setArrivalDate(LocalDate.now().format(dtf));
        createBooking.setDepartureDate(LocalDate.now().plusDays(5).toString());
        createBooking.setEmail("test@gmail.com");
        createBooking.setName("test");
        String inputPayload = jsonMapper.writeValueAsString(createBooking);

        String response = mockMvc.perform(post(getTargetUrlPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputPayload))
                .andExpect(status().is(400))
                .andReturn().getResponse().getContentAsString();

        ErrorOutput errorOutput = jsonMapper.readValue(response, ErrorOutput.class);
        assertEquals(1,errorOutput.getValidationErrors().size());
        assertEquals("you cannot book campsite for morethan 3days", errorOutput.getValidationErrors().get(0).getMessage());
    }

    @Test
    public void  bookCampsite_givenBlankArrivalDate_shouldReturn400() throws Exception {
        CreateBooking createBooking = new CreateBooking();
        createBooking.setArrivalDate("");
        createBooking.setDepartureDate("2020-08-01");
        createBooking.setEmail("test@gmail.com");
        createBooking.setName("Name");
        String inputPayload = jsonMapper.writeValueAsString(createBooking);

        String response = mockMvc.perform(post(getTargetUrlPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputPayload))
                .andExpect(status().is(400))
                .andReturn().getResponse().getContentAsString();

        ErrorOutput errorOutput = jsonMapper.readValue(response, ErrorOutput.class);
        assertEquals(2, errorOutput.getValidationErrors().size());
        assertEquals("arrivalDate", errorOutput.getValidationErrors().get(0).getField());
        assertEquals("arrivalDate", errorOutput.getValidationErrors().get(1).getField());

    }

    @Test
    public void  bookCampsite_givenBlankDepartureDate_shouldReturn400() throws Exception {
        CreateBooking createBooking = new CreateBooking();
        createBooking.setArrivalDate("2020-08-01");
        createBooking.setDepartureDate(" ");
        createBooking.setEmail("test@gmail.com");
        createBooking.setName("Name");
        String inputPayload = jsonMapper.writeValueAsString(createBooking);

        String response = mockMvc.perform(post(getTargetUrlPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputPayload))
                .andExpect(status().is(400))
                .andReturn().getResponse().getContentAsString();

        ErrorOutput errorOutput = jsonMapper.readValue(response, ErrorOutput.class);
        assertEquals(2, errorOutput.getValidationErrors().size());
        assertEquals("departureDate", errorOutput.getValidationErrors().get(0).getField());
        assertEquals("departureDate", errorOutput.getValidationErrors().get(1).getField());

    }

    @Test
    public void  bookCampsite_givenBlankEmail_shouldReturn400() throws Exception {
        CreateBooking createBooking = new CreateBooking();
        createBooking.setArrivalDate("2020-08-01");
        createBooking.setDepartureDate("2020-08-02");
        createBooking.setEmail(" ");
        createBooking.setName("Name");
        String inputPayload = jsonMapper.writeValueAsString(createBooking);

        String response = mockMvc.perform(post(getTargetUrlPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputPayload))
                .andExpect(status().is(400))
                .andReturn().getResponse().getContentAsString();

        ErrorOutput errorOutput = jsonMapper.readValue(response, ErrorOutput.class);
        assertEquals(2, errorOutput.getValidationErrors().size());
        assertEquals("email", errorOutput.getValidationErrors().get(0).getField());
        assertEquals("email", errorOutput.getValidationErrors().get(1).getField());

    }

    @Test
    public void  bookCampsite_givenBlankName_shouldReturn400() throws Exception {
        CreateBooking createBooking = new CreateBooking();
        createBooking.setArrivalDate("2020-08-01");
        createBooking.setDepartureDate("2020-08-02");
        createBooking.setEmail("test@gmail.com");
        createBooking.setName(" ");
        String inputPayload = jsonMapper.writeValueAsString(createBooking);

        String response = mockMvc.perform(post(getTargetUrlPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputPayload))
                .andExpect(status().is(400))
                .andReturn().getResponse().getContentAsString();

        ErrorOutput errorOutput = jsonMapper.readValue(response, ErrorOutput.class);
        assertEquals(1, errorOutput.getValidationErrors().size());
        assertEquals("name", errorOutput.getValidationErrors().get(0).getField());

    }

    @Test
    public void  bookCampsite_givenInvalidJson_shouldReturn400() throws Exception {

        String inputPayload = jsonMapper.writeValueAsString("{\"s}");

        String response = mockMvc.perform(post(getTargetUrlPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputPayload))
                .andExpect(status().is(400))
                .andReturn().getResponse().getContentAsString();

        ErrorOutput errorOutput = jsonMapper.readValue(response, ErrorOutput.class);
        assertEquals("JSON packet sent is invalid", errorOutput.getError());


    }

    @Test
    public void  bookCampsite_givenDataValid_shouldReturn201() throws Exception {
        CreateBooking createBooking = new CreateBooking();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        createBooking.setArrivalDate(LocalDate.now().plusDays(5).format(dtf));
        createBooking.setDepartureDate(LocalDate.now().plusDays(6).format(dtf));
        createBooking.setEmail("test@gmail.com");
        createBooking.setName("name");
        String inputPayload = jsonMapper.writeValueAsString(createBooking);

        String response = mockMvc.perform(post(getTargetUrlPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputPayload))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        BookingOutput bookingOutput = jsonMapper.readValue(response, BookingOutput.class);

        assertNotNull(bookingOutput.getBookingId());
    }
    @Test
    public void  bookCampsite_whenBookedTwice_shouldReturn409() throws Exception {
        CreateBooking createBooking = new CreateBooking();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        createBooking.setArrivalDate(LocalDate.now().plusDays(1).format(dtf));
        createBooking.setDepartureDate(LocalDate.now().plusDays(2).toString());
        createBooking.setEmail("test@gmail.com");
        createBooking.setName("name");
        String inputPayload = jsonMapper.writeValueAsString(createBooking);

        String response = mockMvc.perform(post(getTargetUrlPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputPayload))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();

        BookingOutput bookingOutput = jsonMapper.readValue(response, BookingOutput.class);

        assertNotNull(bookingOutput.getBookingId());

        response = mockMvc.perform(post(getTargetUrlPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputPayload))
                .andExpect(status().is(409))
                .andReturn().getResponse().getContentAsString();
    }

    private String getTargetUrlPath() {
        return "/v1/bookReservation";
    }

}
