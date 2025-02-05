package com.nqvinh.rentofficebackend;

import com.nqvinh.rentofficebackend.infrastructure.config.security.RSAKeyRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableConfigurationProperties(RSAKeyRecord.class)
public class RentOfficeBackendApplication{
    public static void main(String[] args) {
        SpringApplication.run(RentOfficeBackendApplication.class, args);
    }
}
