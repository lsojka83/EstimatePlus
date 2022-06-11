package pl.estimateplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.estimateplus.repository.UserRepository;

import javax.servlet.http.HttpSession;

@Controller
public class AppController {


    private final UserRepository userRepository;

    public AppController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("")
    public String loginPage(HttpSession httpSession,
                            @RequestParam(required = false) String userId
    ) {

        if (userId!= null && userId.equals("1")) {
            httpSession.setAttribute("user", userRepository.findById(Long.parseLong(userId)).get());
            return "redirect:/user";
        }
        if (userId!= null && userId.equals("3")) {
            httpSession.setAttribute("user", userRepository.findById(Long.parseLong(userId)).get());
            return "redirect:/user";
        }
        if (userId!= null && userId.equals("2")) {
            httpSession.setAttribute("user", userRepository.findById(Long.parseLong(userId)).get());
            return "redirect:/admin";
        }

        return "login";
    }

    @GetMapping("/logout")
    public String logout(
            HttpSession httpSession
    ) {
        httpSession.invalidate();
        return "redirect:/";
    }

    @GetMapping("/addaccount")
    public String addAccount() {
        return "add-account";
    }

}
