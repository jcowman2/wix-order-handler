package org.pupcycle.wixorderhandler

import com.google.api.services.sheets.v4.Sheets
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.stereotype.Component

/**
 * Operations to be run on application start.
 *
 * @author Joe Cowman
 */
@Component
@CompileStatic
class OrderHandlerRunner implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(OrderHandlerRunner.class)

    @Autowired
    Sheets sheetsService

    @Override
    void run(String... args) throws Exception {
        LOG.info("OrderHandlerRunner started successfully.")
//
//        Spreadsheet requestBody = new Spreadsheet()
//        requestBody.properties = new SpreadsheetProperties(title: 'Test Sheet')
//
//        def request = sheetsService.spreadsheets().create(requestBody)
//
//        Spreadsheet response = request.execute()
//
//        System.out.println(response)
    }

}
