package ru.practicum.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.enumeration.ParticipationStatus;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.ParticipationRequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.ParticipationRequest;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.ParticipationRequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ParticipationRequestService {

    private final ParticipationRequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Transactional
    public ParticipationRequestDto addRequest(Long userId, Long eventId) {
        log.info("Creating a request for event: {} by user: {}", eventId, userId);
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User with ID {} not found", userId);
                    return new NotFoundException("User with id: " + userId + "was not found");
                });
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> {
                    log.warn("Event with ID {} not found", eventId);
                    return new NotFoundException("Event with id: " + eventId + "was not found");
                });
        ParticipationRequest request = new ParticipationRequest();
        request.setRequester(requester);
        request.setEvent(event);
        request.setStatus(ParticipationStatus.PENDING);
        request.setCreated(LocalDateTime.now());
        ParticipationRequest savedRequest = requestRepository.save(request);
        log.info("The request was successfully created: {}", savedRequest);
        return ParticipationRequestMapper.INSTANCE.toDto(savedRequest);
    }

    @Transactional
    public ParticipationRequestDto cancelRequest(Long userId, Long requestId) {
        log.info("Creating a cancellation request: {} for event by user: {}", requestId, userId);
        ParticipationRequest request = requestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request with id: " + requestId + " was not found"));
        User requester = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User with ID {} not found", userId);
                    return new NotFoundException("User with id: " + userId + "was not found");
                });
        if (!request.getRequester().getId().equals(requester.getId())) {
            throw new NotFoundException("Request with id=" + requestId + " does not belong to user " + userId);
        }
        request.setStatus(ParticipationStatus.CANCELED);
        ParticipationRequest savedRequest = requestRepository.save(request);
        log.info("The request: {} was successfully rejected", savedRequest);
        return ParticipationRequestMapper.INSTANCE.toDto(savedRequest);
    }

    public List<ParticipationRequestDto> getUserRequests(Long userId) {
        log.info("Searching event requests for the user with id: {}", userId);
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> {
                    log.warn("User with ID {} not found", userId);
                    return new NotFoundException("User with id: " + userId + "was not found");
                });
        List<ParticipationRequestDto> requests = requestRepository.findAllByUserId(existingUser.getId())
                .stream()
                .map(ParticipationRequestMapper.INSTANCE::toDto)
                .toList();
        log.info("Event requests was found: {}", requests.size());
        return requests;
    }
}
