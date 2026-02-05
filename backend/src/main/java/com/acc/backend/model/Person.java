package com.acc.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.FieldNameConstants;

@Entity
@Table(name = "person")
@Data
@Getter
@FieldNameConstants
@Schema(description = "Person")
public class Person {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "id of the record", example = "1")
    private Integer id;

    @Column(name = "last_name", nullable = false)
    @Schema(description = "Last Name", example = "Smith")
    private String lastName;

    @Column(name = "first_name", nullable = false)
    @Schema(description = "First name", example = "John")
    private String firstName;

    @Column(name = "email")
    @Schema(description = "Email", example = "john.smith@company.com")
    private String email;

}
