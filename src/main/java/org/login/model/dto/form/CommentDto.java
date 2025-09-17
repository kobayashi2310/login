package org.login.model.dto.form;

import org.login.model.Comment;
import org.login.model.dto.UserDTO;

import java.time.LocalDateTime;

public record CommentDto(
  Long id,
  String content,
  LocalDateTime createdAt,
  UserDTO user
) {

    public static CommentDto fromEntity(Comment comment) {
        if (comment == null) return null;
        return new CommentDto(
                comment.getId(),
                comment.getContent(),
                comment.getCreatedAt(),
                UserDTO.fromEntity(comment.getUser())
        );
    }

}
