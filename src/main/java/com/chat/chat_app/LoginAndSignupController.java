package com.chat.chat_app;

import com.chat.chat_app.user.LoginAndSignupService;
import com.chat.chat_app.user.UserInputsErrors;
import com.chat.chat_app.user.UserLoginDTO;
import com.chat.chat_app.user.UserSignupDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

@Controller
public class LoginAndSignupController {

    private final LoginAndSignupService loginAndSignupService;

    public LoginAndSignupController(LoginAndSignupService loginAndSignupService) {
        this.loginAndSignupService = loginAndSignupService;
    }
    @GetMapping("/signup")
    public String getSignupPage(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies!= null) {
            Optional<Cookie> username = Arrays.stream(cookies).filter(cookie -> Objects.equals(cookie.getName(), "username")).findFirst();
            if (username.isPresent() && !username.get().getValue().trim().isEmpty())
                return "redirect:/chat";
        }
        return  "signup";
    }
    @PostMapping("/signup")
    public String signup(@Valid @ModelAttribute UserSignupDTO user, Model model) throws NoSuchAlgorithmException {
        UserInputsErrors userInputsErrors = loginAndSignupService.signup(user);
        if(userInputsErrors == null)
        {
            model.addAttribute("username",user.getUsername());
            return "redirect:/chat";
        }
        model.addAttribute("errors",userInputsErrors);
        return "signup";
    }
    @GetMapping("/login")
    public String getLoginPage(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if (cookies!= null) {
            Optional<Cookie> username = Arrays.stream(cookies).filter(cookie -> Objects.equals(cookie.getName(), "username")).findFirst();
            if (username.isPresent() && !username.get().getValue().trim().isEmpty())
                return "redirect:/chat";
        }
        return  "login";
    }
    @PostMapping("/login")
    public String login(@Valid @ModelAttribute UserLoginDTO user,Model model, RedirectAttributes redirectAttributes, HttpServletResponse response) throws NoSuchAlgorithmException, IOException {
        UserInputsErrors userInputsErrors = loginAndSignupService.login(user,response);
        if(userInputsErrors == null)
        {
            redirectAttributes.addFlashAttribute("username",user.getUsername());
            return "redirect:/chat";
        }
        model.addAttribute("errors",userInputsErrors);
        return "login";
    }
}
