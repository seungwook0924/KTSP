package com.seungwook.ktsp.global.api.service;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
public class ApiService {

    private final RestTemplate apiRestTemplate;

    public boolean isSafeIp(String ip) {
        final String path = "/admin/ip/default-filter/" + ip;

        ResponseEntity<JsonNode> response = apiRestTemplate.getForEntity(path, JsonNode.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return response.getBody()
                    .path("data")
                    .path("defaultSafeIp")
                    .asBoolean(false);  // defaultSafeIp가 없으면 false
        }

        return false;
    }
}
