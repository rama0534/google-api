package org.rama.googleapi.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Dashboard {

    @GetMapping("/home")
    public String home() {
        return "Welcome to Spring Boot Google API application";
    }
}
