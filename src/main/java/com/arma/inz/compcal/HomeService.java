package com.arma.inz.compcal;

import com.arma.inz.compcal.users.BaseUserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.NoHandlerFoundException;

@Controller
@AllArgsConstructor
@CrossOrigin(origins = "http://46.101.227.24:8080", maxAge = 3600)
public class HomeService {
    private BaseUserService baseUserService;

    @RequestMapping(path = "")
    public String getHome() {
        return "redirect:/";
    }

    @RequestMapping(path = "/")
    public String getAngular() {
        return "/index.html";
        // your index.html built by angular should be in resources/static folder
        // if it is in resources/static/dist/index.html,
        // change the return statement to "/dist/index.html"
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public String handle(Exception ex) { return "redirect:/"; }

    @CrossOrigin
    @GetMapping("/activate/{hash}")
    public String authorizeOld(@PathVariable String hash) {
        return baseUserService.authorizeOld(hash);
    }
}

