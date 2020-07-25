package com.campsite.integrationtests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
import java.util.List;

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
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class GetAvailableDatesTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper jsonMapper;
    @Test
    public void  getAvailableDates_givenStartDateIsAfterEndDate_shouldReturn400() throws Exception {


        String response = mockMvc.perform(get(getAvailableTargetUrlPath("2020-10-13","2020-10-11"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andReturn().getResponse().getContentAsString();

        ErrorOutput errorOutput = jsonMapper.readValue(response, ErrorOutput.class);
        assertEquals(1,errorOutput.getValidationErrors().size());
        assertEquals("startdate cannotbe after enddate", errorOutput.getValidationErrors().get(0).getMessage());
    }
    @Test
    public void  getAvailableDates_givenStartDateIsOnlyProvided_shouldReturn200() throws Exception {


        String response = mockMvc.perform(get(getAvailableTargetUrlPath("2020-10-13",""))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        LocalDate[] output = jsonMapper.readValue(response, LocalDate[].class);
        assertEquals(31, output.length);
    }

    @Test
    public void  getAvailableDates_givenStartDateandEndDateIsProvided_shouldReturn200() throws Exception {


        String response = mockMvc.perform(get(getAvailableTargetUrlPath("2020-10-13","2020-10-15"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        LocalDate[] output = jsonMapper.readValue(response, LocalDate[].class);
        assertEquals(2, output.length);
    }


    private String getAvailableTargetUrlPath(String startDate, String endDate) {
        return "/v1/getAvailableDates?startDate="+startDate+"&endDate="+endDate;
    }


}
