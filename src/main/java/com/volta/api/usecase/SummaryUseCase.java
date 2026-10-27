package com.volta.api.usecase;

import com.volta.api.dto.summary.SummaryResponseDTO;
import com.volta.api.security.AuthenticatedUser;

public interface SummaryUseCase {

    SummaryResponseDTO getSummary(AuthenticatedUser author);
}
