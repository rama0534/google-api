package org.rama.googleapi.service;

import org.rama.googleapi.dto.GoogleApiDto;
import org.rama.googleapi.config.GoogleApiConfig;
import org.rama.googleapi.dto.SpreadsheetLite;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;

@Service
public class GoogleApiService {

    @Autowired
    private GoogleApiConfig googleApiConfig;


    public List<List<Object>> readDataFromGoogleSheet(GoogleApiDto request) throws GeneralSecurityException, IOException {
        return googleApiConfig.getDataFromSheet(request);
    }

    public SpreadsheetLite createSheet(GoogleApiDto request) throws GeneralSecurityException, IOException {
        return googleApiConfig.createSheet(request);
    }
}
