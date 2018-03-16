package org.pupcycle.wixorderhandler

import groovy.json.JsonOutput
import groovy.transform.CompileStatic
import org.pupcycle.wixorderhandler.engine.EmailFilterEngine
import org.pupcycle.wixorderhandler.engine.GmailSyncEngine
import org.pupcycle.wixorderhandler.engine.OrderEngine
import org.pupcycle.wixorderhandler.model.Email
import org.pupcycle.wixorderhandler.model.Order
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Top-level class for all operations involving email.
 * Regularly polls Gmail account for any new messages and parses relevant data.
 *
 * @author Joe Cowman
 */
@Component
@CompileStatic
class EmailManager {

    private static final Logger LOG = LoggerFactory.getLogger(EmailManager.class)

    @Autowired
    private GmailSyncEngine gmailSyncEngine

    @Autowired
    private EmailFilterEngine emailFilterEngine

    @Autowired
    private OrderEngine orderEngine

    /**
     * Syncs new messages on startup and every 10 seconds after.
     */
//    @Scheduled(initialDelay = 0L, fixedRate = 10000L)
    @Scheduled(initialDelay = 0L, fixedRate = 1000000000000L) //todo revert
    void scheduledSync() {
        LOG.info("Performing scheduled sync.")

        List<Email> emails = gmailSyncEngine.syncEmails()
        emails = emailFilterEngine.filterEmails(emails)

        System.out.println(emails)

        List<Order> orders = emails.collect { orderEngine.createOrder(it) }

        System.out.println(JsonOutput.prettyPrint(JsonOutput.toJson(orders)))
    }

}
