package com.acc.backend.controller;

import com.acc.backend.dao.IncidentDao;
import com.acc.backend.model.Incident;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IncidentsController {

    @Autowired
    private IncidentDao incidentDao;

    @GetMapping("/incidents")
    public List<Incident> retrieveIncidents(
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "severity", required = false) String severity,
            @RequestParam(value = "owner", required = false) String owner
    ) {
        return incidentDao.searchIncidents(title, description, severity, owner);
    }

}
