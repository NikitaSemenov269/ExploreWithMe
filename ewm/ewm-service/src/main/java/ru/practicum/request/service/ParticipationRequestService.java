package ru.practicum.request.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.enumeration.ParticipationStatus;
import ru.practicum.request.ParticipationRequest;
import ru.practicum.request.mapper.ParticipationRequestMapper;
import ru.practicum.request.repository.ParticipationRequestRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;

    @Transactional
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        ParticipationRequest request = new ParticipationRequest();
        request.setRequester(userId);
        request.setEvent(eventId);
        request.setStatus(ParticipationStatus.PENDING);
        request.setCreated(LocalDateTime.now());
        ParticipationRequest savedRequest = requestRepository.save(request);
        return ParticipationRequestMapper.INSTANCE.toDto(savedRequest);
    }

    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        request.setStatus(ParticipationStatus.CANCELED);
        ParticipationRequest savedRequest = requestRepository.save(request);
        return ParticipationRequestMapper.INSTANCE.toDto(savedRequest);
    }

    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        List<ParticipationRequest> requests = requestRepository.findAllByUserId(userId);
        return requests.stream()
                .map(ParticipationRequestMapper.INSTANCE::toDto)
                .toList();
    }
}
