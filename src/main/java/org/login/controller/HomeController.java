package org.login.controller;

import lombok.RequiredArgsConstructor;
import org.login.model.Article;
import org.login.service.ArticleService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
@RequiredArgsConstructor
public class HomeController {


    private final ArticleService articleService;

    @GetMapping
    public String home(
            @AuthenticationPrincipal UserDetails userDetails,
            Model model
    ) {
        model.addAttribute("username", userDetails.getUsername());

        List<Article> articles = articleService.findByAll();
        model.addAttribute("articles", articles);

        return "home";
    }

}
