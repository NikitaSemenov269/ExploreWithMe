package ru.practicum;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriBuilder;
import ru.practicum.DTO.RequestStatisticDto;
import ru.practicum.DTO.ResponseStatisticDto;

import java.util.List;

// Нужно инжектить в класс-контроллер, или сервисный слой (в зависимости от задачи) микросервиса wm-service.
@Component
@ConditionalOnProperty(name = "ewm-service.url")
public class StatsClient {
    private final RestClient restClient;

    public StatsClient(@Value("${stats-server.url}") String baseUrl) {
        this.restClient = RestClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    public void saveHit(RequestStatisticDto requestStatisticDto) {
        restClient
                .post()
                .uri("/hit") // Только endpoint, без полного URL
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestStatisticDto)
                .retrieve()
                .toBodilessEntity(); // ответ в виде статуса 201
    }

    // Для работы с Jackson лучше использовать более явный List.
    public List<ResponseStatisticDto> getStats(String start, String end, List<String> uris, boolean unique) {
        return restClient
                .get()
                .uri(uriBuilder -> {
                    UriBuilder builder = uriBuilder.path("/stats")
                            .queryParam("start", start)
                            .queryParam("end", end)
                            .queryParam("unique", unique);

                    if (uris != null && !uris.isEmpty()) {
                        uris.forEach(uri -> builder.queryParam("uris", uri));
                    }
                    return builder.build();
                })
                .retrieve()
                // ParameterizedTypeReference - сохранение типобезопасности.
                .body(new ParameterizedTypeReference<List<ResponseStatisticDto>>() {
                });
    }
}
