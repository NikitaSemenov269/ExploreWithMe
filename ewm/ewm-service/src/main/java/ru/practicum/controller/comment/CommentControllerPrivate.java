package ru.practicum.controller.comment;

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
public class CommentControllerPrivate {
    /*
     * добавление комментариев;
     * удаление комментариев;
     *  */
    private final CommentService commService;

    @PostMapping
    public ResponseEntity<CommentDto> addComment(
            @RequestParam @Min(1) Long userId,
            @RequestBody NewCommentDto newCommentDto) {

        return ResponseEntity.ok(commService.addComment(userId, newCommentDto));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @RequestParam @Min(1) Long userId,
            @PathVariable @Min(1) Long commentId) {

        commService.deleteComment(userId, commentId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
