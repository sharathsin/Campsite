package com.campsite.model.error;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@JsonInclude(JsonInclude.Include.NON_NULL)
@ApiModel(value = "ErrorOutput")
@Data
public class ErrorOutput {

    @ApiModelProperty(value = "The HTTP status code.", example = "400", position = 1)
    private final int status;

    @ApiModelProperty(value = "The HTTP reason phrase; the description associated with the HTTP status code.", example = "Bad Request", position = 2)
    private final String error;

    @ApiModelProperty(value = "Human readable description of the error and/or cause of the error.", example = "Input validation failed", position = 3) private final String message;

    @ApiModelProperty(value = "Name of the webservice returning this response.", example = "Campsite Booking", position = 4)
    private String service;

    @ApiModelProperty(value = "ISO 8601 timestamp.", example = "2019-02-04T11:42:39Z", position = 6)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSZ")
    private final Date timestamp;

    @ApiModelProperty(value = "For input validation errors: a list of tuples (object, field, message) describing the fields that did not pass input validation, plus the required syntax.", position = 7)
    private final List<ValidationError> validationErrors;

    @JsonCreator
    public ErrorOutput(
            @JsonProperty("status") int status,
            @JsonProperty("error") String error,
            @JsonProperty("message") String message,
            @JsonProperty("service") String service,
            @JsonProperty("timestamp") Date timestamp,
            @JsonProperty("validationErrors") List<ValidationError> validationErrors) {
        this.status = status;
        this.error = error;
        this.message = message;
        this.service = service;
        this.timestamp = timestamp;
        this.validationErrors = validationErrors;
    }

}