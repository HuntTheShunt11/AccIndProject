package com.acc.backend.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldNameConstants;

import java.time.LocalDateTime;

@Entity
@Table(name = "incident")
@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldNameConstants
@Schema(description = "Incident")
public class Incident {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "Incident id", example = "123")
    private Integer id;

    @Column(name = "title", nullable = false)
    @Schema(description = "Incident title")
    private String title;

    @Column(name = "description", nullable = false)
    @Schema(description = "Incident description")
    private String description;

    @Column(name = "severity", nullable = false)
    @Schema(description = "Severity of the incident", example = "Medium")
    private String severity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    @Schema(description = "User that created the incident")
    private Person owner;

    @Column(name = "created_at", nullable = false)
    @Schema(description = "Creation date of the incident", example = "2026-06-02T12:00:00")
    private LocalDateTime createdAt;

}
