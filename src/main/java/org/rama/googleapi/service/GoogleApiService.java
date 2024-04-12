package org.rama.googleapi.service;

import org.rama.googleapi.GoogleApiDto;
import org.rama.googleapi.config.GoogleApiConfig;
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
}
