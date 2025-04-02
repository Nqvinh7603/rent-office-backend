
/*******************************************************************************
 * Class        ：CustomJwtDecoder
 * Created date ：2025/04/02
 * Lasted date  ：2025/04/02
 * Author       ：vinhNQ2
 * Change log   ：2025/04/02：01-00 vinhNQ2 create a new
******************************************************************************/
package com.nqvinh.rentofficebackend.infrastructure.config.security;
/**
 * CustomJwtDecoder
 *
 * @version 01-00
 * @since 01-00
 * @author vinhNQ2
 */
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;

import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    private final Map<String, JwtDecoder> decoders = new HashMap<>();
    private final JwtDecoder defaultDecoder;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public CustomJwtDecoder(
            @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}") String issuerUri,
            RSAKeyRecord rsaKeyRecord) {
        // Decoder cho issuer-uri (Google)
        JwtDecoder googleDecoder = JwtDecoders.fromOidcIssuerLocation(issuerUri);
        decoders.put("https://accounts.google.com", googleDecoder);

        // Decoder cho RSA key (nội bộ)
        this.defaultDecoder = NimbusJwtDecoder.withPublicKey(rsaKeyRecord.rsaPublicKey()).build();
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            // Decode token để lấy issuer mà không cần verify
            String[] parts = token.split("\\.");
            if (parts.length != 3) {
                throw new JwtException("Invalid JWT token format");
            }

            // Lấy payload và decode từ Base64
            String payload = new String(java.util.Base64.getDecoder().decode(parts[1]));
            String issuer = extractIssuer(payload);

            // Chọn decoder dựa trên issuer
            JwtDecoder decoder = decoders.getOrDefault(issuer, defaultDecoder);
            return decoder.decode(token);
        } catch (Exception e) {
            throw new JwtException("Failed to decode JWT: " + e.getMessage(), e);
        }
    }

    private String extractIssuer(String payload) throws Exception {
        // Parse payload JSON để lấy issuer
        Map<String, Object> payloadMap = objectMapper.readValue(payload, Map.class);
        return (String) payloadMap.get("iss");
    }
}