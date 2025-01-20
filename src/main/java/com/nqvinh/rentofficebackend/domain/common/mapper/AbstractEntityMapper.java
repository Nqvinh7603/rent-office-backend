package com.nqvinh.rentofficebackend.domain.common.mapper;

import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

public abstract class AbstractEntityMapper<D, E> {

    public abstract D toDto(E entity);
    public abstract E toEntity(D dto);

    public List<D> toDtoList(List<E> entities) {
        if (entities == null) {
            return null;
        }
        return entities.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<E> toEntityList(List<D> dtos) {
        if (dtos == null) {
            return null;
        }
        return dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public void partialUpdate(@MappingTarget E entity, D dto) {
        if (dto == null) {
            return;
        }
    }
}
