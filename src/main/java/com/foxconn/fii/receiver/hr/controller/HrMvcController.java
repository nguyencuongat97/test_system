package com.foxconn.fii.receiver.hr.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/hr")
public class HrMvcController {
    @Value("${server.domain}")
    private String domain;

    @RequestMapping("")
    public String index(Model model) {
        model.addAttribute("path", "hr-index");
        model.addAttribute("title", "HR Home");
        return "application";
    }

    @RequestMapping("/workcount")
    public String workCount(Model model) {
        model.addAttribute("path", "hr-workCount");
        model.addAttribute("title", "HR Work Count");
        return "application";
    }
    @RequestMapping("/foreign-work-count")
    public String foreignWorkCount(Model model) {
        model.addAttribute("path", "foreign-work-count");
        model.addAttribute("title", "HR Work Count");
        return "application";
    }
}
