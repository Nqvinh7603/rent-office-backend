package com.nqvinh.rentofficebackend.infrastructure.utils;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;
import jakarta.persistence.metamodel.Metamodel;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EntityUtils {
    String getIdFieldName(EntityManager entityManager, Class<?> entityClass) {
        Metamodel metamodel = entityManager.getMetamodel();
        EntityType<?> entityType = metamodel.entity(entityClass);
        return entityType.getId(Object.class).getName();
    }
}
