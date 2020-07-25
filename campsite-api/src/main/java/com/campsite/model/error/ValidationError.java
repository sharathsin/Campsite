package com.campsite.model.error;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "ValidationError")
@Data

public class ValidationError implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "The object containing the failure.", example = "CreateBooking", position = 1)
    private final String object;

    @ApiModelProperty(value = "The field containing the failure.", example = "bookingId", position = 2)
    private final String field;

    @ApiModelProperty(value = "The message describing the failure.", example = "Field must be present", position = 3)
    private final String message;

    @JsonCreator
    public ValidationError(@JsonProperty("object") String object, @JsonProperty("field") String field,@JsonProperty("message") String message) {
        this.object = object;
        this.field = field;
        this.message = message;
    }
}