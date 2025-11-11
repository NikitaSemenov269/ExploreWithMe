package ru.practicum.service;

import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.dto.request.ParticipationRequestDto;
import ru.practicum.enumeration.ParticipationStatus;
import ru.practicum.exception.ConflictException;
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
        if (event.getInitiator().getId().equals(userId)) {
            throw new ConflictException("User cannot request participation in their own event");
        }
        if (event.getPublishedOn() == null || event.getPublishedOn().toString().trim().isEmpty()) {
            throw new ConflictException("Cannot participate in an unpublished event");
        }
        Integer participantLimit = event.getParticipantLimit();
        Integer confirmedRequests = event.getConfirmedRequests();
        if (participantLimit != 0 && confirmedRequests >= participantLimit) {
            throw new ConflictException("Participation request limit reached for event id=" + eventId);
        }
        if (requestRepository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ConflictException("Duplicate participation request");
        }
        ParticipationRequest request = new ParticipationRequest();
        request.setRequester(requester);
        request.setEvent(event);
        if (Boolean.FALSE.equals(event.getRequestModeration()) || participantLimit == 0) {
            request.setStatus(ParticipationStatus.CONFIRMED);
        } else {
            request.setStatus(ParticipationStatus.PENDING);
        }
        request.setCreated(LocalDateTime.now());
        ParticipationRequest savedRequest = requestRepository.save(request);
        if (savedRequest.getStatus() == ParticipationStatus.CONFIRMED) {
            event.setConfirmedRequests(confirmedRequests + 1);
            eventRepository.save(event);
            log.info("Updated confirmedRequests for event {} to {}", eventId, event.getConfirmedRequests());
        }
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
        List<ParticipationRequestDto> requests = requestRepository.findAllByRequesterId(existingUser.getId())
                .stream()
                .map(ParticipationRequestMapper.INSTANCE::toDto)
                .toList();
        log.info("Event requests was found: {}", requests.size());
        return requests;
    }
}
