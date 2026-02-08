package com.acc.backend.dao;

import com.acc.backend.model.Incident;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IncidentCustomRequests {
    Page<Incident> searchIncidents(String title, String description, String severity, String owner, Pageable pageable);
}
