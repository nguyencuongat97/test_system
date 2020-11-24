package com.foxconn.fii.receiver.test.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/ef-management")
public class TestEFManagementMvcController {

    @RequestMapping("/overall")
    public String efOverall(Model model) {
        model.addAttribute("path", "ef-overall");
        return "application";
    }

}
