package de.frubumi.dance.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DanceController {

    @RequestMapping("/list")
    public String index(Model model) {
        model.addAttribute("name", "Gast");
        return "list";
    }
}
