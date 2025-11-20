package ru.practicum.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.dto.comment.CommentDto;
import ru.practicum.dto.comment.NewCommentDto;
import ru.practicum.model.Comment;

@Mapper(componentModel = "spring")
public interface CommentMapper {

    Comment toEntity(NewCommentDto newCommentDto);

    @Mapping(target = "authorId", source = "author.id")
    @Mapping(target = "authorName", source = "author.name")
    @Mapping(target = "createdOn", source = "createdOn", dateFormat = "yyyy-MM-dd HH:mm:ss")
    CommentDto toDto(Comment comment);
}
