package org.rama.googleapi.service;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.model.*;
import org.apache.coyote.BadRequestException;
import org.rama.googleapi.dto.GoogleApiDto;
import org.rama.googleapi.config.GoogleApiConfig;
import org.rama.googleapi.dto.SpreadsheetLite;
import org.rama.googleapi.exceptions.NoDataFoundException;
import org.rama.googleapi.exceptions.NoSpreadSheetFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.List;

@Service
public class GoogleApiService {

    @Autowired
    private GoogleApiConfig googleApiConfig;


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
    public List<List<Object>> readDataFromGoogleSheet(GoogleApiDto request) throws GeneralSecurityException, IOException {
        try {
            final String spreadsheetId = request.getSpreadSheetId();
            final String range = request.getRange();
            Sheets service =  googleApiConfig.getService();

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
    public SpreadsheetLite createSheet(GoogleApiDto request) {
        try {
            Sheets service = googleApiConfig.getService();

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
     * Returns spreadsheet ID and URL.
     *
     * @param request SheetName - Name of the spreadsheet and sheet.
     * @return SpreadSheet ID and URL
     */
    public List<List<Object>> updateSheet(GoogleApiDto request) throws IOException {
        try {
            Sheets service = googleApiConfig.getService();
            // assigning uploading data to valueRange object.
            ValueRange valueRange = new ValueRange().setValues(request.getDataToBeUpload());

            // Update values.
            service.spreadsheets().values().update(request.getSpreadSheetId(), request.getRange(), valueRange).setValueInputOption("RAW").execute();

            // To display whole sheet changing the range to sheet level
            String stringOne = request.getRange();
            String newRange = stringOne.substring(0, stringOne.indexOf('!'));
            request.setRange(newRange);

            // Fetching New data from spreadsheet.
            List<List<Object>> values = readDataFromGoogleSheet(request);
            System.out.println(values);
            return values;
        }   catch (GoogleJsonResponseException e) {
                if (e.getStatusCode() == 404) {
                    throw new NoSpreadSheetFoundException("No Spreadsheet found " + e);
                } else if (e.getStatusCode() == 400) {
                    throw new BadRequestException("Bad request: " + e);
                } else {
                    throw e;
            }
        }  catch (IOException | GeneralSecurityException e) {
             throw new RuntimeException("Error accessing Google Sheets API", e);
        }
    }
}
