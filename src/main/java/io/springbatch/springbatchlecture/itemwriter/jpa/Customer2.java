package io.springbatch.springbatchlecture.itemwriter.jpa;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Customer2 {

    @Id
    private long id;
    private String firstName;
    private String lastName;
    private Date birthdate;
}
