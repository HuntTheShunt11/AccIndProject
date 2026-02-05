package com.acc.backend.dao;

import com.acc.backend.model.Incident;

import java.util.List;

public interface IncidentCustomRequests {
    List<Incident> searchIncidents(String title, String description, String severity, String owner);
}
