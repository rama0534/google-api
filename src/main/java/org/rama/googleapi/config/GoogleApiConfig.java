package org.rama.googleapi.config;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.*;
import org.apache.coyote.BadRequestException;
import org.rama.googleapi.dto.GoogleApiDto;
import org.rama.googleapi.dto.SpreadsheetLite;
import org.rama.googleapi.exceptions.NoDataFoundException;
import org.rama.googleapi.exceptions.NoSpreadSheetFoundException;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Component
public class GoogleApiConfig {

    private final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();

    /**
     * Global instance of the scopes required by this Google Api.
     * If modifying these scopes, delete your previously saved tokens/ folder.
     */
    private final List<String> SCOPES =
            Arrays.asList(SheetsScopes.SPREADSHEETS, SheetsScopes.DRIVE);


    /**
     * Creates an authorized Credential object.
     *
     * @param HTTP_TRANSPORT The network HTTP Transport.
     * @return An authorized Credential object.
     * @throws IOException If the credentials.json file cannot be found.
     */
    private Credential getCredentials(final NetHttpTransport HTTP_TRANSPORT)
            throws IOException {
        // Load local secrets.
        String CREDENTIALS_FILE_PATH = "/credentials.json";
        InputStream in = GoogleApiConfig.class.getResourceAsStream(CREDENTIALS_FILE_PATH);
        if (in == null) {
            throw new FileNotFoundException("Resource not found: " + CREDENTIALS_FILE_PATH);
        }
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        String TOKENS_DIRECTORY_PATH = "tokens";
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                .setDataStoreFactory(new FileDataStoreFactory(new java.io.File(TOKENS_DIRECTORY_PATH)))
                .setAccessType("offline")
                .build();
        LocalServerReceiver receiver = new LocalServerReceiver.Builder().setPort(8888).build();
        return new AuthorizationCodeInstalledApp(flow, receiver).authorize("user");
    }

    /**
     * Returns a range of values from a spreadsheet.
     *
     * @param request spreadSheetId - ID of the spreadsheet.
     * @param request range        - Range of cells of the spreadsheet.
     * @return Values in the range
     * @throws IOException - if credentials file not found.
     * @throws BadRequestException - if send any bad request like range.
     * @throws NoDataFoundException - if no data found.
     */
    public List<List<Object>> getDataFromSheet(GoogleApiDto request) throws IOException {
       try {
            final String spreadsheetId = request.getSpreadSheetId();
            final String range = request.getRange();
            Sheets service = getService();

            // Return Spread Sheet values in the given range
            ValueRange response = service.spreadsheets().values()
                    .get(spreadsheetId, range)
                    .execute();
            List<List<Object>> values = response.getValues();
            if (values == null || values.isEmpty()) {
                throw new NoDataFoundException("No data found in the specified range: " + range);
            }
            return values;
        } catch (GoogleJsonResponseException e) {
           if (e.getStatusCode() == 400) {
               throw new BadRequestException("Bad request: " + e);
           } else {
               throw e;
           }
       }catch (IOException | GeneralSecurityException e) {
           throw new RuntimeException("Error accessing Google Sheets API", e);
       }
    }

    /**
     * Returns spreadsheet ID and URL.
     *
     * @param request SheetName - Name of the spreadsheet and sheet.
     * @return SpreadSheet ID and URL
     */
    public SpreadsheetLite createSheet(GoogleApiDto request){
        try {
            Sheets service = getService();

            // Creating Sheet Properties
            SheetProperties sheetProperties = new SheetProperties();
            sheetProperties.setTitle(request.getSheetName());
            Sheet sheet = new Sheet().setProperties(sheetProperties);

            // Creating SpreadSheet Properties
            SpreadsheetProperties spreadsheetProperties = new SpreadsheetProperties();
            spreadsheetProperties.setTitle(request.getSheetName());
            Spreadsheet spreadsheet = new Spreadsheet().setProperties(spreadsheetProperties).setSheets(Collections.singletonList(sheet));

            // Creating a spread Sheet
            Spreadsheet createResponse = service.spreadsheets().create(spreadsheet).execute();

            return new SpreadsheetLite(createResponse);
        } catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("Error Creating Google Sheets API", e);
        }
    }

    /**
     * Update and Returns values from a spreadsheet.
     * @param request spreadSheetId - ID of the spreadsheet.
     * @param request range        - Range of cells of the spreadsheet.
     * @return all Values in the spreadsheet
     * @throws IOException - if credentials file not found.
     * @throws NoSpreadSheetFoundException - if no spreadsheet found.
     */
    public List<List<Object>> updateSheet(GoogleApiDto request) throws IOException {
        try {
            Sheets service = getService();
            ValueRange valueRange = new ValueRange().setValues(request.getDataToBeUpload());
            service.spreadsheets().values().update(request.getSpreadSheetId(), request.getRange(), valueRange).setValueInputOption("RAW").execute();
            String stringOne = request.getRange();
            String newRange = stringOne.substring(0, stringOne.indexOf('!'));
            System.out.println(newRange);
            request.setRange(newRange);
            List<List<Object>> values = getDataFromSheet(request);
            System.out.println(values);
            return values;
        }  catch (GoogleJsonResponseException e) {
            if (e.getStatusCode() == 404) {
                throw new NoSpreadSheetFoundException("No Spreadsheet found " + e);
            } else {
                throw e;
            }
        }catch (IOException | GeneralSecurityException e) {
            throw new RuntimeException("Error accessing Google Sheets API", e);
        }
    }

    // Build a new authorized API client service.
    private Sheets getService() throws GeneralSecurityException, IOException {
        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
        String APPLICATION_NAME = "Google Sheets API";
        return new Sheets.Builder(HTTP_TRANSPORT, JSON_FACTORY, getCredentials(HTTP_TRANSPORT))
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

}
