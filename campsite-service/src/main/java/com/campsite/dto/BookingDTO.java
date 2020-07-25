package com.campsite.dto;

import java.time.LocalDate;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity(name = "booking")
@Table(name = "booking")
@Data

public class BookingDTO {
    @Id
    @GeneratedValue
    private long id;

    @Column(unique = true)
    private LocalDate bookingDate;


    private String bookingId;

    private String email;

    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BookingDTO that = (BookingDTO) o;
        return Objects.equals(bookingDate, that.bookingDate) && Objects.equals(email, that.email)
                && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(bookingDate, email, name);
    }


}
