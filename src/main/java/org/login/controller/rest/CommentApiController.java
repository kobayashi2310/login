package org.login.controller.rest;

import lombok.RequiredArgsConstructor;
import org.login.model.dto.form.CommentDto;
import org.login.model.dto.form.CommentFormDTO;
import org.login.service.ArticleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles/{articledId}/comments")
@RequiredArgsConstructor
public class CommentApiController {

    private final ArticleService articleService;

    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments(
            @PathVariable("articledId") Long articleId
    ) {
        List<CommentDto> comments = articleService.findCommentDosByArticleId(articleId);
        return ResponseEntity.ok(comments);
    }

    @PostMapping
    public ResponseEntity<?> createCommetns(
            @PathVariable("articledId") Long articleId,
            @Validated @RequestBody CommentFormDTO commentForm,
            BindingResult bindingResult
    ) {
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }

        CommentDto createComment = articleService.createComment(commentForm, articleId);
        return new ResponseEntity<>(createComment, HttpStatus.CREATED);
    }

}
