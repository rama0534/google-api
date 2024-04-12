package org.rama.googleapi.dto;


import com.google.api.services.sheets.v4.model.Spreadsheet;
import lombok.Data;

@Data
public class SpreadsheetLite {

    final String spreadsheetId;
    final String spreadsheetUrl;
    public SpreadsheetLite(Spreadsheet spreadsheet) {
        this.spreadsheetId = spreadsheet.getSpreadsheetId();
        this.spreadsheetUrl = spreadsheet.getSpreadsheetUrl();
    }
}
