package org.pupcycle.wixorderhandler

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
class OrderHandlerRunner implements CommandLineRunner {

    private static final Logger LOG = LoggerFactory.getLogger(OrderHandlerRunner.class)

    @Autowired
    GmailSyncEngine syncManager

    @Override
    void run(String... args) throws Exception {
        LOG.info("OrderHandlerRunner started successfully.")
//        syncManager.scheduledSync()
    }

}
