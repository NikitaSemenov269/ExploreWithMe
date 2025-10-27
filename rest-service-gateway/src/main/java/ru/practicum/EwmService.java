package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

@Component
public class EwmService {
    private final RestClient restClient;

    public EwmService(@Value("${ewm-service.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    // методы в зависимости от требований ФЗ ( swagger ).
}
