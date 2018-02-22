package org.pupcycle.wixorderhandler

import com.google.api.services.gmail.Gmail
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


/**
 * Methods to manage notification and email sync with Gmail.
 *
 * @author Joe Cowman
 */
@Component
@CompileStatic
class GmailSyncManager {

    private static final Logger LOG = LoggerFactory.getLogger(GmailSyncManager.class)

    @Autowired
    Gmail gmailService

    /**
     * Submits Watch request to Google Cloud Pub/Sub Gmail topic.
     * Runs on startup, and then again every 24 hours.
     */
    @Scheduled(initialDelay = 0L, fixedRate = 3000L) //placeholder, tests every 3 seconds
    void submitWatchRequest() {
        LOG.info("Watch request submitted.")
    }
}
