package org.login.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.login.model.Article;
import org.login.model.Comment;
import org.login.model.User;
import org.login.model.dto.form.ArticleFormDTO;
import org.login.model.dto.form.CommentDto;
import org.login.model.dto.form.CommentFormDTO;
import org.login.repository.ArticleRepository;
import org.login.repository.CommentRepository;
import org.login.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;

    @Value( "${file.upload-dir}")
    private String uploadDir;

    public List<Article> findByAll() {
        return articleRepository.findAll();
    }

    public Article findById(Long id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Article Not Found:" + id));
    }

    public void createArticle(
            ArticleFormDTO articleForm,
            MultipartFile imageFile
    ) throws RuntimeException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        User currentUser = userRepository.findByName(currentUserName)
                .orElseThrow(() -> new RuntimeException("User Not Found:" + currentUserName));

        Article article = new Article();
        article.setTitle(articleForm.getTitle());
        article.setContent(articleForm.getContent());
        article.setUser(currentUser);

        if (imageFile != null && !imageFile.isEmpty()) {
            try {
                String fileName = storeImage(imageFile);
                article.setImagePath(fileName);
            } catch (IOException e) {
                throw new RuntimeException("Image File IO Error");
            }
        }

        // createdAtとupdatedAtは@CreationTimestamp/@UpdateTimestampで自動設定される

        articleRepository.save(article);

    }

    private String storeImage(MultipartFile imageFile) throws IOException {
        String originalFilename = imageFile.getOriginalFilename();
        if (originalFilename == null) {
            throw new IllegalArgumentException("Image File is Empty");
        }

        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String storedFilename = UUID.randomUUID() + extension;

        Path destinationFile = Paths.get(uploadDir).resolve(storedFilename).toAbsolutePath();

        Files.createDirectories(destinationFile.getParent());

        try(InputStream inputStream = imageFile.getInputStream()) {
            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
        }

        return storedFilename;
    }

    public CommentDto createComment(
            CommentFormDTO commentForm,
            Long articleId
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        User currentUser = userRepository.findByName(currentUserName)
                .orElseThrow(() -> new RuntimeException("User Not Found:" + currentUserName));

        Article article = findById(articleId);

        Comment comment = new Comment();
        comment.setContent(commentForm.getContent());
        comment.setUser(currentUser);
        comment.setArticle(article);

        Comment savedComment = commentRepository.save(comment);

        return CommentDto.fromEntity(savedComment);

    }

    @Transactional
    public List<CommentDto> findCommentDosByArticleId(Long articleId) {
        return commentRepository.findByArticleIdOrderByCreatedAtDesc(articleId)
                .stream()
                .map(CommentDto::fromEntity)
                .toList();
    }

}
