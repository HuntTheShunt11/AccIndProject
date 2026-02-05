package com.acc.backend.dao;

import com.acc.backend.model.Incident;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IncidentDao extends JpaRepository<Incident, Integer>, IncidentCustomRequests {

}
