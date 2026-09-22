package com.volta.api.mapper;

import com.volta.api.database.entity.Cooperative;
import com.volta.api.dto.request.CooperativeRequestDTO;
import com.volta.api.dto.response.CooperativeResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class CooperativeMapper {

    public Cooperative toEntity(CooperativeRequestDTO dto){
        Cooperative cooperative = new Cooperative();

        cooperative.setName(dto.name());
        cooperative.setCnpj(dto.cnpj());
        cooperative.setLatitude(dto.latitude());
        cooperative.setLongitude(dto.longitude());
        cooperative.setAverageRating(dto.averageRating());
        cooperative.setSpecialties(dto.specialties());

        return cooperative;
    }

    public CooperativeResponseDTO toResponse(Cooperative cooperative){
        return new CooperativeResponseDTO(
                cooperative.getName(),
                cooperative.getCnpj(),
                cooperative.getLatitude(),
                cooperative.getLongitude(),
                cooperative.getAverageRating(),
                cooperative.getSpecialties()
        );
    }
}
