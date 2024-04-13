package org.rama.googleapi.dto;

import lombok.Data;

import java.util.List;

@Data
public class GoogleApiDto {
    private String spreadSheetId;
    private String range;
    private String sheetName;
    private List<List<Object>> dataToBeUpload;
}
