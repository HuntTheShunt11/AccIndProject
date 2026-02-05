package com.acc.backend.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Entity
@Table(name = "person")
@Data
@Getter
@FieldNameConstants
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "email")
    private String email;

}
