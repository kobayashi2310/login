package org.login.controller;

import lombok.RequiredArgsConstructor;
import org.login.model.Article;
import org.login.model.Comment;
import org.login.model.dto.form.ArticleFormDTO;
import org.login.model.dto.form.CommentFormDTO;
import org.login.service.ArticleService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.nio.file.AccessDeniedException;
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
            @RequestParam("imageFile") MultipartFile imageFile
    ) {

        if (bindingResult.hasErrors()) {
            return "articles/new";
        }

        articleService.createArticle(articleForm, imageFile);
        return "redirect:/home";

    }

    @GetMapping("/edit/{id}")
    public String showEditForm(
            @PathVariable("id") Long id,
            Model model,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        Article article = articleService.findById(id);

        if (!article.getUser().getName().equals(userDetails.getUsername())
                && userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("この記事を編集する権限がありません");
        }

        ArticleFormDTO articleForm = new ArticleFormDTO();
        articleForm.setTitle(article.getTitle());
        articleForm.setContent(article.getContent());

        model.addAttribute("articleForm", articleForm);
        model.addAttribute("article", article);
        return "articles/edit";
    }

    @PostMapping("/edit/{id}")
    public String updateArticle(
            @PathVariable("id") Long id,
            @Validated @ModelAttribute ArticleFormDTO articleForm,
            BindingResult bindingResult,
            @RequestParam("imageFile") MultipartFile imageFile,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes,
            Model model
    ) {
        Article article = articleService.findById(id);

        if (!article.getUser().getName().equals(userDetails.getUsername())
                && userDetails.getAuthorities().stream().noneMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("この記事を編集する権限がありません");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("article", article);
            return "articles/edit";
        }

        articleService.updateArticle(article, articleForm, imageFile);

        redirectAttributes.addFlashAttribute("successMessage", "記事を更新しました。");
        return "redirect:/articles/{id}";
    }

    @PostMapping("/delete/{id}")
    public String deleteArticle(
            @PathVariable("id") Long id,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes
    ) {
        try {
            articleService.deleteArticle(id, userDetails);
            redirectAttributes.addFlashAttribute("successMessage", "記事を削除しました");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "記事の削除に失敗しました。");
            return "redirect:/articles/" + id;
        }
        return "redirect:/home";
    }

}
