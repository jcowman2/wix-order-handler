package org.pupcycle.wixorderhandler

import com.google.api.services.gmail.Gmail
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
    Gmail gmailService

    @Autowired
    GmailSyncManager syncManager

    @Autowired
    HistoryManager historyManager

    @Override
    void run(String... args) throws Exception {
        LOG.info("OrderHandlerRunner started successfully.")

//        String messageId = syncManager.getMostRecentMessageId()
//        String historyId = syncManager.getMessage(messageId).getHistoryId()
        String historyId = "822440"

//        List<Message> m = syncManager.getMessagesAddedSinceHistoryId(historyId)
        System.out.println(historyManager.getHistoryFileAccessor().getHistoryIfExists())
    }

}
