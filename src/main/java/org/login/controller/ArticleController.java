package org.login.controller;

import lombok.RequiredArgsConstructor;
import org.login.model.dto.form.ArticleFormDTO;
import org.login.service.ArticleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/articles")
@RequiredArgsConstructor
public class ArticleController {

    private final ArticleService articleService;

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
