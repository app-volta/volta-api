package com.volta.api.controller;

import com.volta.api.dto.response.IncidentResponseDTO;
import com.volta.api.service.RegisterIncidentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController()
@RequiredArgsConstructor
@RequestMapping("/incidents")
public class IncidentController {

}
