package com.assignment.postblog.controller;

import com.assignment.postblog.security.MemberDetailsImpl;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @GetMapping("/")
    public String home(Model model, @AuthenticationPrincipal MemberDetailsImpl memberDetails) {
        model.addAttribute("nickname", memberDetails.getUsername());
        return "index";
    }
}
