package org.pupcycle.wixorderhandler.engine

import com.google.api.services.sheets.v4.model.ValueRange
import groovy.transform.CompileStatic
import org.pupcycle.wixorderhandler.accessor.SheetsAccessor
import org.pupcycle.wixorderhandler.config.SpreadsheetInfo
import org.pupcycle.wixorderhandler.model.Order
import org.pupcycle.wixorderhandler.model.OrderSpreadsheetEntryFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Records orders in the spreadsheet.
 *
 * @author Joe Cowman
 */
@Component
@CompileStatic
class RecordOrderEngine {

    private static final Logger LOG = LoggerFactory.getLogger(RecordOrderEngine.class)

    private SheetsAccessor sheetsAccessor
    private OrderSpreadsheetEntryFactory entryFactory
    private SpreadsheetInfo sheetInfo

    private String range

    /**
     * Constructs a RecordOrderEngine.
     * @param sheetsAccessor        the sheets accessor
     * @param entryFactory          the entry factory
     * @param sheetInfo             the spreadsheet info
     */
    @Autowired
    RecordOrderEngine(SheetsAccessor sheetsAccessor, OrderSpreadsheetEntryFactory entryFactory, SpreadsheetInfo sheetInfo) {
        this.sheetsAccessor = sheetsAccessor
        this.entryFactory = entryFactory
        this.sheetInfo = sheetInfo
        range = calculateCellRange(entryFactory.columnNames().size())
    }

    /**
     * Records an order in the spreadsheet.
     * @param order     the order
     */
    void recordOrder(Order order) {
        sheetsAccessor.appendRow(
                sheetInfo.spreadsheetId,
                "${sheetInfo.sheetName}!$range",
                getValueRange(entryFactory.toEntry(order)))
        LOG.info("Recorded order {number: ${order.orderNumber}} in spreadsheet {name: ${sheetInfo.sheetName}}.")
    }

    /**
     * Generates a ValueRange given the row of values
     * @param values    the row of values
     * @return  a ValueRange
     */
    ValueRange getValueRange(List<String> values) {
        new ValueRange().with {
            setValues( [values] )
        }
    }

    /**
     * Calculates the cell range string expected by the Sheets API
     * @param length    the length of the cell range
     * @return a cell range string (something like 'A1:AB1')
     */
    static String calculateCellRange(int length) {
        String s = ''
        while (--length > 0) {
            char c = (('A' as char) + (length % 26)) as char
            s = c.toString() + s
            length /= 26
        }
        return 'A1:' + s + '1'
    }

}
