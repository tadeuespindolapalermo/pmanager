package com.java360.pmanager.infrastructure.controller;

import com.java360.pmanager.domain.applicationservice.ApiKeyService;
import com.java360.pmanager.domain.document.ApiKey;
import com.java360.pmanager.infrastructure.dto.ApiKeyDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

import static com.java360.pmanager.infrastructure.controller.RestConstants.PATH_API_KEYS;

@RestController
@RequestMapping(PATH_API_KEYS)
@RequiredArgsConstructor
public class ApiKeyRestResource {

    private final ApiKeyService apiKeyService;

    @PostMapping
    public ResponseEntity<ApiKeyDTO> createApiKey() {
        ApiKey apiKey = apiKeyService.createApiKey();

        return ResponseEntity
            .created(URI.create(PATH_API_KEYS + "/" + apiKey.getId()))
            .body(ApiKeyDTO.create(apiKey));
    }

    @PutMapping("{id}/revoke")
    public ResponseEntity<Void> revokeApiKey(@PathVariable String id) {
        apiKeyService.revokeApiKey(id);
        return ResponseEntity.noContent().build();
    }

}
