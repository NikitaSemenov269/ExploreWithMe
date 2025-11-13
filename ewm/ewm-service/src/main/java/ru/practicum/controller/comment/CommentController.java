package ru.practicum.controller.comment;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.service.CommentService;

@RestController
@RequiredArgsConstructor
@RequestMapping()
public class CommentController {
    private final CommentService commService;


}