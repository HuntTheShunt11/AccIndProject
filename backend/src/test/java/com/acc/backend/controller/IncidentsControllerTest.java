package com.acc.backend.controller;

import com.acc.backend.dao.IncidentDao;
import com.acc.backend.model.Incident;
import com.acc.backend.model.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IncidentsControllerTest {

    @Mock
    private IncidentDao incidentDao;

    @InjectMocks
    private IncidentsController incidentsController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRetrieveIncidents_found() {
        Person owner = new Person();
        owner.setId(1);
        owner.setLastName("Smith");
        owner.setFirstName("John");
        owner.setEmail("john.smith@company.com");

        Incident incident = new Incident();
        incident.setId(1);
        incident.setTitle("Incident a00a8cdbfec652ac2c5be3cc03aa2f3f");
        incident.setDescription("Description: aa1694b4b1dd7e9c6758ce77667d4e5f");
        incident.setSeverity("HIGH");
        incident.setOwner(owner);
        incident.setCreatedAt(LocalDateTime.now());
        when(incidentDao.searchIncidents("Incident a00a8cdbfec652ac2c5be3cc03aa2f3f", null, null, null)).thenReturn(List.of(incident));

        ResponseEntity<List<Incident>> response = incidentsController.retrieveIncidents("Incident a00a8cdbfec652ac2c5be3cc03aa2f3f", null, null, null);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(incident.getTitle(), response.getBody().get(0).getTitle());
        assertEquals(incident.getDescription(), response.getBody().get(0).getDescription());
        assertEquals(incident.getSeverity(), response.getBody().get(0).getSeverity());
        assertNotNull(response.getBody().get(0).getOwner());
        assertEquals(owner.getFirstName(), response.getBody().get(0).getOwner().getFirstName());
        assertEquals(owner.getLastName(), response.getBody().get(0).getOwner().getLastName());
        assertEquals(owner.getEmail(), response.getBody().get(0).getOwner().getEmail());
    }

    @Test
    void testRetrieveIncidents_multipleResults() {
        Person owner1 = new Person();
        owner1.setId(2);
        owner1.setLastName("Brown");
        owner1.setFirstName("Michael");
        owner1.setEmail("michael.brown@company.com");

        Person owner2 = new Person();
        owner2.setId(3);
        owner2.setLastName("Brivio");
        owner2.setFirstName("Davide");
        owner2.setEmail("davide.brivio@company.com");

        Incident incident1 = new Incident();
        incident1.setId(2);
        incident1.setTitle("Incident a00a8cdbfec652ac2c5be3cc03aa2f3f");
        incident1.setDescription("Description : aa1694b4");
        incident1.setSeverity("LOW");
        incident1.setOwner(owner1);
        incident1.setCreatedAt(LocalDateTime.now());

        Incident incident2 = new Incident();
        incident2.setId(3);
        incident2.setTitle("Incident aa1694b4b1dd7e9c6758ce77667d4e5f");
        incident2.setDescription("Description : aa1694b4b1dd7e9c6758ce77667d4e5f");
        incident2.setSeverity("MEDIUM");
        incident2.setOwner(owner2);
        incident2.setCreatedAt(LocalDateTime.now());
        when(incidentDao.searchIncidents(null, null, null, null)).thenReturn(List.of(incident1, incident2));

        ResponseEntity<List<Incident>> response = incidentsController.retrieveIncidents(null, null, null, null);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());

        assertEquals(incident1.getTitle(), response.getBody().get(0).getTitle());
        assertEquals(incident1.getDescription(), response.getBody().get(0).getDescription());
        assertEquals(incident1.getSeverity(), response.getBody().get(0).getSeverity());
        assertNotNull(response.getBody().get(0).getOwner());
        assertEquals(owner1.getFirstName(), response.getBody().get(0).getOwner().getFirstName());
        assertEquals(owner1.getLastName(), response.getBody().get(0).getOwner().getLastName());
        assertEquals(owner1.getEmail(), response.getBody().get(0).getOwner().getEmail());

        assertEquals(incident2.getTitle(), response.getBody().get(1).getTitle());
        assertEquals(incident2.getDescription(), response.getBody().get(1).getDescription());
        assertEquals(incident2.getSeverity(), response.getBody().get(1).getSeverity());
        assertNotNull(response.getBody().get(1).getOwner());
        assertEquals(owner2.getFirstName(), response.getBody().get(1).getOwner().getFirstName());
        assertEquals(owner2.getLastName(), response.getBody().get(1).getOwner().getLastName());
        assertEquals(owner2.getEmail(), response.getBody().get(1).getOwner().getEmail());
    }

    @Test
    void testRetrieveIncidents_noResults() {
        when(incidentDao.searchIncidents("Unknown incident", null, null, null)).thenReturn(Collections.emptyList());
        ResponseEntity<List<Incident>> response = incidentsController.retrieveIncidents("Unknown incident", null, null, null);
        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testRetrieveIncidents_filterOwner() {
        Person owner = new Person();
        owner.setId(4);
        owner.setLastName("Johnson");
        owner.setFirstName("Sarah");
        owner.setEmail("sarah.johnson@company.com");

        Incident incident = new Incident();
        incident.setId(1);
        incident.setTitle("Incident owner test");
        incident.setDescription("Test owner filter");
        incident.setSeverity("MEDIUM");
        incident.setOwner(owner);
        incident.setCreatedAt(LocalDateTime.now());
        when(incidentDao.searchIncidents(null, null, null, "Sarah")).thenReturn(List.of(incident));

        ResponseEntity<List<Incident>> response = incidentsController.retrieveIncidents(null, null, null, "Sarah");

        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(incident.getTitle(), response.getBody().get(0).getTitle());
        assertEquals(incident.getDescription(), response.getBody().get(0).getDescription());
        assertEquals(incident.getSeverity(), response.getBody().get(0).getSeverity());
        assertEquals(incident.getCreatedAt(), response.getBody().get(0).getCreatedAt());
        assertEquals(owner.getFirstName(), response.getBody().get(0).getOwner().getFirstName());
        assertEquals(owner.getLastName(), response.getBody().get(0).getOwner().getLastName());
        assertEquals(owner.getEmail(), response.getBody().get(0).getOwner().getEmail());
    }
}
