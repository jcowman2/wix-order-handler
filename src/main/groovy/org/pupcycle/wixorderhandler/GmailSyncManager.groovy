package org.pupcycle.wixorderhandler

import com.google.api.services.gmail.model.Message
import org.pupcycle.wixorderhandler.accessor.GmailAccessor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Manages message retrieval from Gmail and storing of most recent history ids.
 *
 * @author Joe Cowman
 */
@Component
class GmailSyncManager {

    private static final Logger LOG = LoggerFactory.getLogger(GmailSyncManager.class)

    @Autowired
    GmailAccessor gmailAccessor

    @Autowired
    HistoryManager historyManager

    /**
     * Retrieves all new messages since the last history id was recorded. If no history
     * id can be found, then the history id of the most recent message is used.
     * Finally, the history id of the most recent message is stored, if any were returned.
     *
     * @return the list of new messages
     */
    List<Message> getNewMessages() {
        //noinspection GroovyAssignabilityCheck
        String historyId = historyManager.getSavedHistoryId().orElse { -> gmailAccessor.getMostRecentHistoryId() }

        List<Message> messages = gmailAccessor.getMessagesAddedSinceHistoryId(historyId)

        if (messages) {
//            historyManager.setSavedHistoryId(messages.last().getHistoryId().toString())
        }

        return messages
    }

    /**
     * Syncs new messages on startup and every 10 seconds after.
     * Will eventually be moved into its own class.
     */
    //@Scheduled(initialDelay = 0L, fixedRate = 10000L)
    void scheduledSync() {
        LOG.info("Performing scheduled sync.")
        getNewMessages().each{EmailParser.parseEmail(it)}
    }

}
