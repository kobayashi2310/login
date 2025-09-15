package org.login.service;

import lombok.RequiredArgsConstructor;
import org.login.model.Article;
import org.login.model.User;
import org.login.model.dto.form.ArticleFormDTO;
import org.login.repository.ArticleRepository;
import org.login.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final UserRepository userRepository;

    public List<Article> findByAll() {
        return articleRepository.findAll();
    }

    public void createArticle(ArticleFormDTO articleForm) throws RuntimeException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUserName = authentication.getName();

        User currentUser = userRepository.findByName(currentUserName)
                .orElseThrow(() -> new RuntimeException("User Not Found:" + currentUserName));

        Article article = new Article();
        article.setTitle(articleForm.getTitle());
        article.setContent(articleForm.getContent());
        article.setUser(currentUser);

        // createdAtとupdatedAtは@CreationTimestamp/@UpdateTimestampで自動設定される

        articleRepository.save(article);

    }

}
