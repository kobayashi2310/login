package org.login.controller.admin;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.login.model.dto.UserDTO;
import org.login.service.AdminService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping
    public String admin(
            Model model,
            HttpSession session
    ) {

        String deleteMessage = (String) session.getAttribute("deleteMessage");
        if (deleteMessage != null) {
            model.addAttribute("message", deleteMessage);
            session.removeAttribute("deleteMessage");
        }

        List<UserDTO> users = adminService.findByUser();
        model.addAttribute("users", users);

        return "admin/admin";

    }

    @PostMapping("/delete/{id}")
    public String deleteUser(
            @PathVariable("id") Long id,
            HttpSession session
    ) {
        adminService.deleteUser(id);
        session.setAttribute("deleteMessage", "ユーザを正常に削除しました");
        return "redirect:/admin";
    }

}
