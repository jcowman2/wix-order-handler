package org.pupcycle.wixorderhandler

import groovy.transform.CompileStatic
import org.pupcycle.wixorderhandler.engine.EmailFilterEngine
import org.pupcycle.wixorderhandler.engine.GmailSyncEngine
import org.pupcycle.wixorderhandler.engine.RecordOrderEngine
import org.pupcycle.wixorderhandler.model.Email
import org.pupcycle.wixorderhandler.model.Order
import org.pupcycle.wixorderhandler.util.OrderBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Top-level class for all order handling operations.
 *
 * Regularly polls the Gmail account for any new messages and generates an order,
 * then adds the order to the Sheets spreadsheet.
 *
 * @author Joe Cowman
 */
@Component
@CompileStatic
class Handler {

    private static final Logger LOG = LoggerFactory.getLogger(Handler.class)

    @Autowired
    private GmailSyncEngine gmailSyncEngine

    @Autowired
    private EmailFilterEngine emailFilterEngine

    @Autowired
    private RecordOrderEngine recordOrderEngine

    /**
     * Retrieves new emails, parses any orders, and saves them.
     * Syncs new messages on startup and every 10 seconds after.
     */
    @Scheduled(initialDelay = 0L, fixedRate = 10000L)
    void scheduledSync() {
        LOG.info("Performing scheduled sync.")

        List<Email> emails = gmailSyncEngine.syncEmails()
        emails = emailFilterEngine.filterEmails(emails)

        LOG.info("After filtering, {count: ${emails.size()}} emails remain.")

        if (emails.size() > 0) {
            List<Order> orders = emails.collect { OrderBuilder.createOrder(it) }
            orders.each { recordOrderEngine.recordOrder(it) }
        }

        LOG.info("Sync completed.")
    }

}
