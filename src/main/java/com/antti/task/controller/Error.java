package com.antti.task.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class Error {

    @RequestMapping("/404")
    public String error404() {
        return "404";
    }
}
