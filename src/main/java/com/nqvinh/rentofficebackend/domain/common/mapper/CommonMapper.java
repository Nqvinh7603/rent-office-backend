package com.nqvinh.rentofficebackend.domain.common.mapper;

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

    default void partialUpdate(@MappingTarget E entity, D dto) {
        if (dto == null) {
            return;
        }
    }
}
