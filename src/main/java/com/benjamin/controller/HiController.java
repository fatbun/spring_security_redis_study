package com.benjamin.controller;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by Ben Li.
 * Date: 2020/6/13
 */
@RestController
public class HiController {

    @RequestMapping("/hi")
    public String hi(){
        return "hi";
    }

}
