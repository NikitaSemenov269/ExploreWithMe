package ru.practicum.controller.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {
    private final CommentService commService;

    @PostMapping
    public ResponseEntity<CommentDto> addComment(
            @PathVariable @Min(1) Long userId,
            @RequestBody @Valid NewCommentDto newCommentDto) {

        return ResponseEntity.status(HttpStatus.CREATED).body(commService.addComment(userId, newCommentDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable @Min(1) Long userId,
            @PathVariable @Min(1) Long commentId) {

        commService.deleteComment(userId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
