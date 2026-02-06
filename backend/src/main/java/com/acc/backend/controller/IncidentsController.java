package com.acc.backend.controller;

import com.acc.backend.dao.IncidentDao;
import com.acc.backend.model.Incident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;

@RestController
public class IncidentsController {

    @Autowired
    private IncidentDao incidentDao;

    @Operation(
        summary = "Retrieve incidents",
        description = "Retrieve incidents from the database using optional filters."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Incident(s) found"),
    })
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/incidents")
    public ResponseEntity<List<Incident>> retrieveIncidents(
            @Parameter(description = "Incident title", example = "Incident a00a8cdbfec652ac2c5be3cc03aa2f3f") @RequestParam(value = "title", required = false) String title,
            @Parameter(description = "Incident description", example = "Description: aa1694b4b1dd7e9c6758ce77667d4e5f") @RequestParam(value = "description", required = false) String description,
            @Parameter(description = "Incident severity", example = "MEDIUM") @RequestParam(value = "severity", required = false) String severity,
            @Parameter(description = "Owner info for the incident, it can be his last name, first name or email address", example = "John/Smith/john.smith@company.com") @RequestParam(value = "owner", required = false) String owner
    ) {
        List<Incident> incidents = incidentDao.searchIncidents(title, description, severity, owner);
        return ResponseEntity.ok(incidents);
    }

}
