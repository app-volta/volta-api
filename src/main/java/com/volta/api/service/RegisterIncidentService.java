package com.volta.api.service;

import com.volta.api.database.repository.IncidentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterIncidentService {
    private final IncidentRepository incidentRepository;

}
