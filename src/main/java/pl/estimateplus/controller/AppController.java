package pl.estimateplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.estimateplus.entity.User;
import pl.estimateplus.model.Messages;
import pl.estimateplus.repository.UserRepository;
import pl.estimateplus.model.Security;


import javax.servlet.http.HttpSession;

@Controller
public class AppController {

    private String incorrectLoginData = "Wrong user name or password";


    private final UserRepository userRepository;

    public AppController(UserRepository userRepository) {
        this.userRepository = userRepository;
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
        if(button.equals("login")) {
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
        }
        else {
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

    @GetMapping("/newaccount")
    @ResponseBody
    public String createAccount()
    {

        return Messages.UNDER_CONSTRUCTION;
    }

    @GetMapping("/addaccount")
    public String addAccount() {
        return "add-account";
    }


//    @GetMapping("")
//    public String loginPage(HttpSession httpSession,
//                            @RequestParam(required = false) String userId
//    ) {
//
//        if (userId!= null && userId.equals("1")) {
//            httpSession.setAttribute("user", userRepository.findById(Long.parseLong(userId)).get());
//            return "redirect:/user";
//        }
//        if (userId!= null && userId.equals("3")) {
//            httpSession.setAttribute("user", userRepository.findById(Long.parseLong(userId)).get());
//            return "redirect:/user";
//        }
//        if (userId!= null && userId.equals("2")) {
//            httpSession.setAttribute("user", userRepository.findById(Long.parseLong(userId)).get());
//            return "redirect:/admin";
//        }
//
//        return "login";
//    }

}
