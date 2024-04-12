package org.rama.googleapi.service;

import com.google.api.services.sheets.v4.model.ValueRange;
import org.rama.googleapi.GoogleApiDto;
import org.rama.googleapi.config.GoogleApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;

@Service
public class GoogleApiService {

    @Autowired
    private GoogleApiConfig googleApiConfig;


    public ValueRange readDataFromGoogleSheet(GoogleApiDto request) throws GeneralSecurityException, IOException {
        return googleApiConfig.getDataFromSheet(request);
    }
}
