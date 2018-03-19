package org.pupcycle.wixorderhandler.accessor

import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.AppendValuesResponse
import com.google.api.services.sheets.v4.model.ValueRange
import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Data operations for Google Sheets.
 *
 * @author Joe Cowman
 */
@Component
@CompileStatic
class SheetsAccessor {

    static final String USER_ENTERED = 'USER_ENTERED'
    static final String INSERT_ROWS = 'INSERT_ROWS'

    @Autowired
    Sheets sheetService

    /**
     * Appends a row of data to the spreadsheet.
     * @param spreadsheetId     the Google sheet id
     * @param cellRange         the range of cells, including the sheet name
     * @param values            the values to be appended
     * @return  the response
     */
    AppendValuesResponse appendRow(String spreadsheetId, String cellRange, ValueRange values) {
        sheetService.spreadsheets().values().append(spreadsheetId, cellRange, values).with {
            setValueInputOption(USER_ENTERED)
            setInsertDataOption(INSERT_ROWS)
            execute()
        }
    }

}
