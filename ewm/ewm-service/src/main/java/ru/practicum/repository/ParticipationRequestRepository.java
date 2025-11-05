package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    Optional<ParticipationRequest> findByUserIdAndEventId(Long userId, Long eventId);

    List<ParticipationRequest> findAllByUserId(Long userId);

    Integer countByEventId(Long eventId);
}
