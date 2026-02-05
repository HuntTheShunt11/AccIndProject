package com.acc.backend.dao;

import com.acc.backend.model.Incident;
import com.acc.backend.model.Person;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import java.util.List;

@Repository
public class IncidentDaoImpl implements IncidentCustomRequests {

    @PersistenceContext
    private EntityManager entityManager;

    private static final String PERCENT_SIGN = "%";

    /**
     * This method implements a dynamic search for incidents based on the provided parameters.
     * The results are ordered by the createdAt field in descending order.
     * @param title The title of the incident to search for (optional).
     * @param description The description of the incident to search for (optional).
     * @param severity The severity level of the incident to search for (optional).
     * @param owner The owner details (lastName, firstName, or email) to search for (optional).
     * @return A list of incidents that match the search criteria.
     */
    @Override
    public List<Incident> searchIncidents(String title, String description, String severity, String owner) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Incident> cq = cb.createQuery(Incident.class);
        Root<Incident> incident = cq.from(Incident.class);

        // Join with the Person entity to filter by owner details (lastName, firstName, email)
        Join<Object, Object> person = incident.join(Incident.Fields.owner, JoinType.INNER);

        Predicate predicate = cb.conjunction();
        // Add filters if they are present
        if (title != null && !title.isEmpty()) {
            predicate = cb.and(predicate, cb.like(cb.lower(incident.get(Incident.Fields.title)), PERCENT_SIGN + title.toLowerCase() + PERCENT_SIGN));
        }
        if (description != null && !description.isEmpty()) {
            predicate = cb.and(predicate, cb.like(cb.lower(incident.get(Incident.Fields.description)), PERCENT_SIGN + description.toLowerCase() + PERCENT_SIGN));
        }
        if (severity != null && !severity.isEmpty()) {
            predicate = cb.and(predicate, cb.like(cb.lower(incident.get(Incident.Fields.severity)), PERCENT_SIGN + severity.toLowerCase() + PERCENT_SIGN));
        }
        if (owner != null && !owner.isEmpty()) {
            Predicate ownerPred = cb.or(
                cb.like(cb.lower(person.get(Person.Fields.lastName)), PERCENT_SIGN + owner.toLowerCase() + PERCENT_SIGN),
                cb.like(cb.lower(person.get(Person.Fields.firstName)), PERCENT_SIGN + owner.toLowerCase() + PERCENT_SIGN),
                cb.like(cb.lower(person.get(Person.Fields.email)), PERCENT_SIGN + owner.toLowerCase() + PERCENT_SIGN)
            );
            predicate = cb.and(predicate, ownerPred);
        }
        cq.where(predicate);

        // Order incidents by createdAt in descending order
        cq.orderBy(cb.desc(incident.get(Incident.Fields.createdAt)));
        TypedQuery<Incident> query = entityManager.createQuery(cq);
        return query.getResultList();
    }
}
