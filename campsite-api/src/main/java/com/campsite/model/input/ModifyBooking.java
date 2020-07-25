package com.campsite.model.input;

import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ModifyBookingInput", description = "Input payload for booking campsite")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ModifyBooking extends CreateBooking {
 @NotBlank
 private String bookingId;


}
