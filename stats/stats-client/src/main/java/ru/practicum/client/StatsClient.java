/*
package ru.practicum.client;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import ru.practicum.DTO.RequestStatisticDto;
import ru.practicum.DTO.ResponseStatisticDto;

import java.util.List;

@Service
public class StatsClient {
    private final RestClient restClient;

    public StatsClient() {
        this.restClient = RestClient.create();
    }

    public void saveHit(RequestStatisticDto requestStatisticDto) {
        restClient
                .post()
                .uri("http://localhost:9090/hit")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestStatisticDto)
                .retrieve()
                .toEntity(RequestStatisticDto.class);
    }

    public ResponseStatisticDto getStats(String start, String end, List<String> uris, boolean unique) {
        return restClient
                .get()
                .uri(uriSpec -> uriSpec
                        .path("/stats")
                        .queryParam("start", start)
                        .queryParam("end", end)
                        .queryParam("uris", uris)
                        .queryParam("unique", unique)
                        .build())
                .retrieve()
                .body(ResponseStatisticDto.class);
    }
}
*/
