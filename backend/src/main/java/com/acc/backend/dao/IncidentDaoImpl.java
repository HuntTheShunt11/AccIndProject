package com.acc.backend.dao;

import com.acc.backend.model.Incident;
import com.acc.backend.model.Person;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import java.util.ArrayList;

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
     * @param pageable The pagination information.
     * @return A page of incidents that match the search criteria.
     */
    @Override
    public Page<Incident> searchIncidents(String title, String description, String severity, String owner, Pageable pageable) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();

        // --- Main query to retrieve the incidents ---
        CriteriaQuery<Incident> cq = cb.createQuery(Incident.class);
        Root<Incident> incidentRoot = cq.from(Incident.class);
        Join<Incident, Person> personJoin = incidentRoot.join(Incident.Fields.owner, JoinType.INNER);

        List<Predicate> predicates = getPredicates(cb, incidentRoot, personJoin, title, description, severity, owner);

        cq.where(predicates);
        cq.orderBy(cb.desc(incidentRoot.get(Incident.Fields.createdAt)));

        TypedQuery<Incident> query = entityManager.createQuery(cq);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Incident> incidents = query.getResultList();

        // --- Count query for the pagination ---
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);
        Root<Incident> countRoot = countQuery.from(Incident.class);
        Join<Incident, Person> countPersonJoin = countRoot.join(Incident.Fields.owner, JoinType.INNER);
        countQuery.select(cb.count(countRoot));

        List<Predicate> countPredicates = getPredicates(cb, countRoot, countPersonJoin, title, description, severity, owner);
        countQuery.where(countPredicates);

        Long totalResults = entityManager.createQuery(countQuery).getSingleResult();

        return new PageImpl<>(incidents, pageable, totalResults);
    }

    private List<Predicate> getPredicates(CriteriaBuilder cb, Root<Incident> root, Join<Incident, Person> join, String title, String description, String severity, String owner) {
        List<Predicate> predicates = new ArrayList<>();
        if (title != null && !title.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get(Incident.Fields.title)), PERCENT_SIGN + title.toLowerCase() + PERCENT_SIGN));
        }
        if (description != null && !description.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get(Incident.Fields.description)), PERCENT_SIGN + description.toLowerCase() + PERCENT_SIGN));
        }
        if (severity != null && !severity.isEmpty()) {
            predicates.add(cb.like(cb.lower(root.get(Incident.Fields.severity)), PERCENT_SIGN + severity.toLowerCase() + PERCENT_SIGN));
        }
        if (owner != null && !owner.isEmpty()) {
            predicates.add(cb.or(
                cb.like(cb.lower(join.get(Person.Fields.lastName)), PERCENT_SIGN + owner.toLowerCase() + PERCENT_SIGN),
                cb.like(cb.lower(join.get(Person.Fields.firstName)), PERCENT_SIGN + owner.toLowerCase() + PERCENT_SIGN),
                cb.like(cb.lower(join.get(Person.Fields.email)), PERCENT_SIGN + owner.toLowerCase() + PERCENT_SIGN)
            ));
        }
        return predicates;
    }
}
