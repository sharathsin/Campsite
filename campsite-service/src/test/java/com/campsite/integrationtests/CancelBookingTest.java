package com.campsite.integrationtests;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
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

import com.campsite.model.input.CreateBooking;
import com.campsite.model.input.ModifyBooking;
import com.campsite.model.output.BookingOutput;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class CancelBookingTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper jsonMapper;
    @Test
    public void  modifyBooking_givenValidBookingIdIsSent_shouldReturn201() throws Exception {
        CreateBooking createBooking = new CreateBooking();
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        createBooking.setArrivalDate(LocalDate.now().plusDays(9).format(dtf));
        createBooking.setDepartureDate(LocalDate.now().plusDays(10).format(dtf));
        createBooking.setEmail("test@gmail.com");
        createBooking.setName("name");


        String inputPayload = jsonMapper.writeValueAsString(createBooking);

        String response = mockMvc.perform(delete(getBookTargetUrlPath())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();


        BookingOutput bookingOutput = jsonMapper.readValue(response, BookingOutput.class);


        response = mockMvc.perform(post(getCancelTargetUrlPath(bookingOutput.getBookingId()))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(inputPayload))
                .andExpect(status().is(201))
                .andReturn().getResponse().getContentAsString();



    }

    @Test
    public void  modifyBooking_whenInvalidBookingIdIsSent_shouldReturn409() throws Exception {


        String response = mockMvc.perform(delete(getCancelTargetUrlPath("Invalid"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().is(409))
                .andReturn().getResponse().getContentAsString();
    }

    private String getBookTargetUrlPath() {
        return "/v1/bookReservation";
    }
    private String getCancelTargetUrlPath(String bookingId) {
        return "/v1/cancelReservation?bookingId="+bookingId;
    }

}