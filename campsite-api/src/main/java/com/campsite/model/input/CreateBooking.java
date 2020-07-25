package com.campsite.model.input;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


@Data

@ApiModel(value = "CreateBookingInput", description = "Input payload for booking campsite")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CreateBooking {
 @NotBlank
 @ApiModelProperty(required = true, name = "name", position = 0,example = "Charles Darwin")
 private String name;

 @NotBlank
 @Pattern(regexp = "^([a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?)$",message = "Invalid email")
 @ApiModelProperty(required = true, name = "email", position = 0,example = "charles@gmail.com")
 private String email;

 @NotBlank
 @Pattern(regexp = "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$"
         + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
         + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
         + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$",message = "arrival date must be in YYYY-MM-DD format")
 @ApiModelProperty(required = true, name = "arrivalDate", position = 0,example = "2020-03-17")
 private String arrivalDate;

 @NotBlank
 @Pattern(regexp = "^((2000|2400|2800|(19|2[0-9](0[48]|[2468][048]|[13579][26])))-02-29)$"
         + "|^(((19|2[0-9])[0-9]{2})-02-(0[1-9]|1[0-9]|2[0-8]))$"
         + "|^(((19|2[0-9])[0-9]{2})-(0[13578]|10|12)-(0[1-9]|[12][0-9]|3[01]))$"
         + "|^(((19|2[0-9])[0-9]{2})-(0[469]|11)-(0[1-9]|[12][0-9]|30))$",message = "departure date must be in MM-DD-YYYY format")
 @ApiModelProperty(required = true, name = "departureDate", position = 0,example = "2020-04-18")
 private String departureDate;

}
