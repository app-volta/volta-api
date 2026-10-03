package com.volta.api.database.procedure;

import com.volta.api.database.entity.Incident;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

public interface IncidentProcedure extends Repository<Incident, UUID> {

    @Transactional
    @Procedure(procedureName = "close_incident")
    void closeIncident(@Param("p_incident_id") UUID incidentId);
}
