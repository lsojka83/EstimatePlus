package pl.estimateplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.estimateplus.entity.User;
import pl.estimateplus.model.Messages;
import pl.estimateplus.repository.UserRepository;
import pl.estimateplus.model.Security;
import pl.estimateplus.validator.PasswordValidator;


import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class AppController {

    private String incorrectLoginData = "Wrong user name or password";


    private final UserRepository userRepository;
    private final PasswordValidator passwordValidator;


    public AppController(UserRepository userRepository, PasswordValidator passwordValidator) {
        this.userRepository = userRepository;
        this.passwordValidator = passwordValidator;
    }

    @GetMapping("")
    public String loginPage(HttpSession httpSession,
                            @RequestParam(required = false) String userId
    ) {
        if (userId != null) {  //temporary!!!
            httpSession.setAttribute("user", userRepository.findById(Long.parseLong(userId)).get());
            if (userRepository.findById(Long.parseLong(userId)).get().isAdmin()) {
                return "redirect:/admin";
            } else {
                return "redirect:/user";
            }
        }

        return "login";
    }

    @PostMapping("")
    public String loginPage(
            HttpSession httpSession,
            @RequestParam String userName,
            @RequestParam String password,
            Model model,
            @RequestParam(required = false) String userId, //temporary!!!l
            @RequestParam String button

    ) {
        if (button.equals("login")) {
            User user = null;
            if (userRepository.findByUserName(userName) == null) {
                model.addAttribute("incorrectLoginData", incorrectLoginData);
                return "login";
            }

            user = userRepository.findByUserName(userName);

            if (!Security.checkPassword(password, user.getPassword())) {
                model.addAttribute("incorrectLoginData", incorrectLoginData);
                return "login";
            }
            httpSession.setAttribute("user", user);

            if (user.isAdmin()) {
                return "redirect:/admin";
            } else {
                return "redirect:/user";
            }
        } else {
            return "redirect:/newaccount";
        }
    }

    @GetMapping("/logout")
    public String logout(
            HttpSession httpSession
    ) {
        httpSession.invalidate();
        return "redirect:/";
    }

    @GetMapping("/addaccount")
    public String addAccount(Model model) {

        model.addAttribute("user", new User());
        return "add-account";
    }

    @PostMapping("/addaccount")
    public String addAccount(Model model,
                             @Valid User user,
                             BindingResult bindingResult) {

        if (!passwordValidator.isValid(user.getPassword(), null)) {
            model.addAttribute("invalidPassword", Messages.INVALID_PASSWORD);
        }
        if (bindingResult.hasErrors()) {
            return "add-account";
        }

        if (model.getAttribute("invalidPassword") != null) {
            return "add-account";
        }

        user.setPasswordUnhashed(user.getPassword());
        user.setPassword(Security.hashPassword(user.getPassword()));
        userRepository.save(user);
        return "redirect:/";
    }
}
