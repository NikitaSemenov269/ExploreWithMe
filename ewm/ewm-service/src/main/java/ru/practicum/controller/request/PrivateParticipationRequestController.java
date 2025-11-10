package ru.practicum.controller.request;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.service.ParticipationRequestService;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class PrivateParticipationRequestController {
    private final ParticipationRequestService service;

    @PostMapping()
    public ResponseEntity<ParticipationRequestDto> addRequest(
            @PathVariable @NotNull Long userId,
            @RequestParam @NotNull Long eventId) {
            ParticipationRequestDto dto = service.addRequest(userId, eventId);
            return ResponseEntity.status(HttpStatus.CREATED).body(dto);
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParticipationRequestDto> cancelRequest(
            @PathVariable @NotNull Long userId,
            @PathVariable @NotNull Long requestId) {
            ParticipationRequestDto dto = service.cancelRequest(userId, requestId);
            return ResponseEntity.ok(dto);

    }

    @GetMapping
    public ResponseEntity<List<ParticipationRequestDto>> getUserRequests(
            @PathVariable @NotNull Long userId) {
            List<ParticipationRequestDto> dtos = service.getUserRequests(userId);
            return ResponseEntity.ok(dtos);
    }

}
