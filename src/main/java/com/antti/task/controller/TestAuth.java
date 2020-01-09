package com.antti.task.controller;

import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Secured(value="testioikeus")
@Controller
public class TestAuth {

    /**
     * @return 
     * @todo: only for testing auth!
     */
    @RequestMapping("/test_auth")
    @ResponseBody
    public String testAuth() 
    {
        return "Test successful!";
    }
}
