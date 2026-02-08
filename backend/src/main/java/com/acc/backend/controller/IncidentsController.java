package com.acc.backend.controller;

import com.acc.backend.dao.IncidentDao;
import com.acc.backend.model.Incident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
public class IncidentsController {

    @Autowired
    private IncidentDao incidentDao;

    @Operation(
        summary = "Retrieve incidents",
        description = "Retrieve incidents from the database using optional filters and pagination."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Incident(s) found"),
    })
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/incidents")
    public ResponseEntity<Page<Incident>> retrieveIncidents(
            @Parameter(description = "Incident title", example = "Incident a00a8cdbfec652ac2c5be3cc03aa2f3f") @RequestParam(value = "title", required = false) String title,
            @Parameter(description = "Incident description", example = "Description: aa1694b4b1dd7e9c6758ce77667d4e5f") @RequestParam(value = "description", required = false) String description,
            @Parameter(description = "Incident severity", example = "MEDIUM") @RequestParam(value = "severity", required = false) String severity,
            @Parameter(description = "Owner info for the incident, it can be his last name, first name or email address", example = "John/Smith/john.smith@company.com") @RequestParam(value = "owner", required = false) String owner,
            @Parameter(description = "Page number to retrieve") @RequestParam(value = "page", defaultValue = "0") int page,
            @Parameter(description = "Number of results per page (10, 20, or 50)") @RequestParam(value = "size", defaultValue = "10") int size
    ) {
        if (size != 10 && size != 20 && size != 50) {
            size = 10; // Default to 10 if size is not one of the allowed values
        }
        Pageable pageable = PageRequest.of(page, size);
        Page<Incident> incidents = incidentDao.searchIncidents(title, description, severity, owner, pageable);
        return ResponseEntity.ok(incidents);
    }

}
