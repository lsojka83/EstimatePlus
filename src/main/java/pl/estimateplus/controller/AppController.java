package pl.estimateplus.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppController {

    @GetMapping("")
    public String loginPage()
    {
        return "login";
    }

    @GetMapping("/addaccount")
    public String addAccount()
    {
        return "add-account";
    }

}
