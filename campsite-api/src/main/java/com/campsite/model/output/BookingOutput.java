package com.campsite.model.output;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("BookingOutput")
public class BookingOutput {
    @ApiModelProperty(value = "bookingId", example = "123e4567-e89b-12d3-a456-426614174000", required = true, position = 0)
    String bookingId;
}
