package ru.practicum.controller.comment;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class CommentControllerPrivate {
    private final CommentService commService;

    @PostMapping
    public ResponseEntity<CommentDto> addComment(
            @RequestParam @Min(1) Long userId,
            @RequestBody @Valid NewCommentDto newCommentDto) {
        return ResponseEntity.ok(commService.addComment(userId, newCommentDto));
        /*
         * добавление комментариев;
         * удаление комментариев;
         *  */
    }
}
