package com.java360.pmanager.domain.applicationservice;

import com.java360.pmanager.domain.document.ApiKey;
import com.java360.pmanager.domain.repository.ApiKeyRepository;
import com.java360.pmanager.infrastructure.config.AppConfigProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ApiKeyService {

    private final ApiKeyRepository apiKeyRepository;

    private final AppConfigProperties props;

    public ApiKey createApiKey() {
        ApiKey apiKey = ApiKey
            .builder()
                .value(UUID.randomUUID().toString())
                .expiresWhen(
                    OffsetDateTime
                        .now()
                        .plusDays(props.getSecurity().getExpirationDays())
                        .toInstant()
                )
                .build();

        apiKeyRepository.save(apiKey);

        return apiKey;
    }
}
