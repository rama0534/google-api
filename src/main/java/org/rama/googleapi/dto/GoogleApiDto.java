package org.rama.googleapi.dto;

import lombok.Data;

@Data
public class GoogleApiDto {
    private String spreadSheetId;
    private String range;
    private String sheetName;
}
