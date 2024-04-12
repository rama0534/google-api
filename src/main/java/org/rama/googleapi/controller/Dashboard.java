package org.rama.googleapi.controller;

import com.google.api.services.sheets.v4.model.ValueRange;
import org.rama.googleapi.GoogleApiDto;
import org.rama.googleapi.service.GoogleApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
public class Dashboard {

    @Autowired
    private GoogleApiService googleApiService;

    @GetMapping("/home")
    public String home() {
        return "Welcome to Spring Boot Google API application";
    }

    @GetMapping("/getdata")
    public ValueRange readDataFromGoogleSheet(@RequestBody GoogleApiDto request) throws GeneralSecurityException, IOException {
        return googleApiService.readDataFromGoogleSheet(request);
    }



}
