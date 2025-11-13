package ru.practicum.controller.comment;

import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/events/{eventId}/comments")
public class CommentController {
    private final CommentService commService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getEventComments(
            @PathVariable @NotNull Long eventId) {

        List<CommentDto> dtos = commService.findAllByIdEvent(eventId);
        return ResponseEntity.ok(dtos);
    }

}