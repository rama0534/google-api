package org.rama.googleapi.exceptions;

import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import org.apache.coyote.BadRequestException;
import org.springframework.stereotype.Component;

@Component
public class HandleException {
    public void handleRuntimeException(GoogleJsonResponseException e) throws GoogleJsonResponseException, BadRequestException {
        if (e.getStatusCode() == 404) {
            throw new NoSpreadSheetFoundException(" No Spreadsheet found " + e);
        } else if (e.getStatusCode() == 403) {
            throw new NotAuthorizedException(" Forbidden: " + e);
        } else if (e.getStatusCode() == 400) {
            throw new BadRequestException(" Bad request: " + e);
        } else {
            throw e;
        }
    }
}
