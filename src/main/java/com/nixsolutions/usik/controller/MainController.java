package com.nixsolutions.usik.controller;

import com.nixsolutions.usik.constants.ViewsContent;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class MainController {

    @RequestMapping(value = {"/", "/home"})
    public String home() {
        return ViewsContent.JSP_HOME;
    }

    @RequestMapping(value = "/accessDenied")
    public String accessDenied() {
        return ViewsContent.JSP_ACCESS_DENIED;
    }

}
