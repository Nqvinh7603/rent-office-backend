package com.nqvinh.rentofficebackend.domain.common.service;

public interface RedisService {
    void set(String key, Object value);

    void set(String key, Object value, int timeout);

    Object get(String key);

    boolean delete(String key);
}
