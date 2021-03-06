package org.pupcycle.wixorderhandler

import groovy.transform.CompileStatic
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

/**
 * Application that retrieves incoming emails sent to a Gmail account from Wix and
 * parses the data into a Google Sheets document for bookkeeping.
 *
 * @author Joe Cowman
 */
@CompileStatic
@SpringBootApplication
@EnableScheduling
class WixOrderHandlerApplication {

	static void main(String[] args) {
		SpringApplication.run(WixOrderHandlerApplication, args)
	}

}
