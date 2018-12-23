package com.arma.inz.compcal;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AppController {

    @RequestMapping( method = {RequestMethod.OPTIONS, RequestMethod.GET}, path = {"/path1/**", "/path2/**", "/"} )
    public String forwardAngularPaths() {
        return "forward:/index.html";
    }
}
