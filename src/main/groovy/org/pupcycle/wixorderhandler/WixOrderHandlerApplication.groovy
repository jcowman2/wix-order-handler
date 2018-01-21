package org.pupcycle.wixorderhandler

import groovy.transform.CompileStatic
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
@CompileStatic
class WixOrderHandlerApplication {

	static void main(String[] args) {
		SpringApplication.run WixOrderHandlerApplication, args
	}

}
