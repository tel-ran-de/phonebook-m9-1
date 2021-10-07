package com.telran.phonebookapi.controller;

import io.swagger.v3.oas.annotations.Hidden;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@Hidden
public class RedirectUiController {

    @RequestMapping(value = {"{_:^(?!index\\.html|api)[^.]*$}", "/**/{_:[^.]*}"})
    public String toRedirect() {
        return "forward:/";
    }
}
