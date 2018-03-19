package org.pupcycle.wixorderhandler.model

import groovy.transform.CompileStatic

/**
 * Interface for the structure of entries in the spreadsheet.
 *
 * @author Joe Cowman
 */
@CompileStatic
interface OrderSpreadsheetEntryFactory {

    /**
     * Return the list of columns in the spreadsheet.
     * @return  the column names
     */
    List columnNames()

    /**
     * Generate an entry row for the given order.
     * Should match the column names.
     * @param order     the order
     * @return  the list of row values
     */
    List toEntry(Order order)

}
