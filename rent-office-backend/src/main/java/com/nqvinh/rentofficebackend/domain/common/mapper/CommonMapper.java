package com.nqvinh.rentofficebackend.domain.common.mapper;

import com.nqvinh.rentofficebackend.domain.building.dto.BuildingDto;
import com.nqvinh.rentofficebackend.domain.building.entity.Building;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

public interface CommonMapper<D, E> {

    D toDto(E entity);

    E toEntity(D dto);

    default List<D> toDtoList(List<E> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    default List<E> toEntityList(List<D> dto) {
        if (dto == null) {
            return null;
        }
        return dto.stream().map(this::toEntity).collect(Collectors.toList());
    }

    void partialUpdate(@MappingTarget E entity, D dto);

    @Mapping(target = "consignmentStatusHistories", ignore = true)
    void partialUpdate(@MappingTarget BuildingDto dto, Building entity);
}
