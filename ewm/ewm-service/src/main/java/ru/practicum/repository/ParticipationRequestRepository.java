package ru.practicum.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.enumeration.ParticipationStatus;
import ru.practicum.model.ParticipationRequest;

import java.util.List;
import java.util.Optional;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    Optional<ParticipationRequest> findByRequesterIdAndEventId(Long userId, Long eventId);

    List<ParticipationRequest> findAllByRequesterId(Long userId);

    Integer countByEventId(Long eventId);

    List<ParticipationRequest> findAllByEventId(Long eventId);

    List<ParticipationRequest> findAllByEventIdAndIdIn(Long eventId, List<Long> requestIds);

    List<ParticipationRequest> findAllByEventIdAndStatus(Long eventId, ParticipationStatus status);

    long countByEventIdAndStatus(Long eventId, ParticipationStatus status);

    @Modifying
    @Query("UPDATE ParticipationRequest r SET r.status = :status " +
            "WHERE r.event.id = :eventId AND r.id IN :requestIds AND r.status = 'PENDING'")
    void bulkUpdateStatus(@Param("eventId") Long eventId,
                          @Param("requestIds") List<Long> requestIds,
                          @Param("status") ParticipationStatus status);

    @Modifying
    @Query("UPDATE ParticipationRequest r SET r.status = :status " +
            "WHERE r.event.id = :eventId AND r.status = 'PENDING'")
    void rejectAllPendingRequests(@Param("eventId") Long eventId,
                                  @Param("status") ParticipationStatus status);
}
