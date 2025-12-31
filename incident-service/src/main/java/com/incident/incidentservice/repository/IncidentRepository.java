package com.incident.incidentservice.repository;

import com.incident.incidentservice.entity.Incident;
import com.incident.incidentservice.enums.IncidentStatus;
import com.incident.incidentservice.enums.Severity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncidentRepository extends JpaRepository<Incident, Long> {

    Page<Incident> findByStatus(IncidentStatus status, Pageable pageable);
    
    Page<Incident> findBySeverity(Severity severity, Pageable pageable);
    
    Page<Incident> findByAssigneeId(Long assigneeId, Pageable pageable);

    @Query("SELECT i FROM Incident i WHERE i.status IN :statuses")
    List<Incident> findByStatusIn(List<IncidentStatus> statuses);

    @Query("SELECT i FROM Incident i WHERE " +
           "(:status IS NULL OR i.status = :status) AND " +
           "(:severity IS NULL OR i.severity = :severity) AND " +
           "(:assigneeId IS NULL OR i.assigneeId = :assigneeId)")
    Page<Incident> findByFilters(IncidentStatus status, Severity severity, Long assigneeId, Pageable pageable);

    long countByStatus(IncidentStatus status);
}
