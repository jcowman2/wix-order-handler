package org.pupcycle.wixorderhandler.config

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Bean with the active spreadsheet's information.
 *
 * @author Joe Cowman
 */
@Component
@CompileStatic
class SpreadsheetInfo {

    @Value('${spreadsheet.id}')
    String spreadsheetId

    @Value('${spreadsheet.name}')
    String sheetName

}
