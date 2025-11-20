package ru.practicum.controller.comment;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.service.CommentService;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/events/{eventId}/comments")
public class PublicCommentController {
    private final CommentService commService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getEventComments(
            @PathVariable @Min(1) Long eventId) {
        return ResponseEntity.ok(commService.findAllByIdEvent(eventId));
    }
}