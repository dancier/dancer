package de.frubumi.dance.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DanceController {

    @RequestMapping("/")
    public String index() {
        return "Let's dance!";
    }
}
