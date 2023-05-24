package io.springbatch.springbatchlecture.itemreader.jdbccursor;

import lombok.Data;

@Data
public class Customer {
    private Long id;
    private String firstName;
    private String lastName;
    private String birthdate;
}
