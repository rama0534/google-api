package org.rama.googleapi.controller;

import org.rama.googleapi.dto.GoogleApiDto;
import org.rama.googleapi.dto.SpreadsheetLite;
import org.rama.googleapi.exceptions.NotAuthorizedException;
import org.rama.googleapi.service.GoogleApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@RestController
public class Dashboard {

    @Autowired
    private GoogleApiService googleApiService;

    @GetMapping("/home")
    public String home() {
        return "Welcome to Spring Boot Google API application";
    }

    @GetMapping("/getdata")
    public List<List<Object>> readDataFromGoogleSheet(@RequestBody GoogleApiDto request) throws GeneralSecurityException, IOException, NotAuthorizedException {
        return googleApiService.readDataFromGoogleSheet(request);
    }

    @PostMapping("/create")
    public SpreadsheetLite createSheet(@RequestBody GoogleApiDto request){
        return googleApiService.createSheet(request);
    }

    @PostMapping("/update")
    public List<List<Object>> updateSheet(@RequestBody GoogleApiDto request) throws IOException {
        return googleApiService.updateSheet(request);
    }
}
