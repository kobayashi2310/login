package org.login.controller;

import lombok.RequiredArgsConstructor;
import org.login.model.Article;
import org.login.model.Comment;
import org.login.model.dto.form.ArticleFormDTO;
import org.login.model.dto.form.CommentFormDTO;
import org.login.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

    @GetMapping({"/{id}"})
    public String showArticle(
            @PathVariable("id") Long id,
            Model model
    ) {
        Article article = articleService.findById(id);
        model.addAttribute("article", article);
        return "articles/show";
    }

    @GetMapping("/new")
    public String newArticleForm(Model model) {
        model.addAttribute("articleForm", new ArticleFormDTO());
        return "articles/new";
    }

    // @Validatedでバリデーションチャックを行う
    // BindingResult に結果が入る
    @PostMapping("/new")
    public String createArticle(
            @Validated @ModelAttribute ArticleFormDTO articleForm,
            BindingResult bindingResult,
            Model model
    ) {

        if (bindingResult.hasErrors()) {
            return "articles/new";
        }

        articleService.createArticle(articleForm);

        return "redirect:/home";
    }

}
