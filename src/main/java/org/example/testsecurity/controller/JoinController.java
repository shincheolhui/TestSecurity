package org.example.testsecurity.controller;

import org.example.testsecurity.dto.JoinDto;
import org.example.testsecurity.service.JoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class JoinController {

    @Autowired
    private JoinService joinService;

    @GetMapping("/join")
    public String join() {
        return "join";
    }

    @PostMapping("/joinProc")
    public String joinProc(JoinDto joinDto) {
        System.out.println("JoinController.joinProc");
        System.out.println("joinDto = " + joinDto);

        joinService.joinProcess(joinDto);

        return "redirect:/login";
    }
}
