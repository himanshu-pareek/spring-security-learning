package dev.javarush.security.spring_security_learn.greeting;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class GreetingController {

    @GetMapping("/")
    public String publicPage() {
        return "public";
    }

    @GetMapping("/private")
    public String privatePage(Model model, Authentication authentication) {
        model.addAttribute("name", getName(authentication));
        return "private";
    }

    private String getName(Authentication authentication) {
        return authentication.getName();
    }
}
