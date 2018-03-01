package org.pupcycle.wixorderhandler

import com.google.api.services.gmail.model.Message
import org.pupcycle.wixorderhandler.accessor.GmailAccessor
import org.pupcycle.wixorderhandler.accessor.HistoryFileAccessor
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

/**
 * Manages message retrieval from Gmail and storing of most recent history ids.
 *
 * @author Joe Cowman
 */
@Component
class GmailSyncEngine {

    private static final Logger LOG = LoggerFactory.getLogger(GmailSyncEngine.class)

    @Autowired
    GmailAccessor gmailAccessor

    @Autowired
    HistoryFileAccessor historyFileAccessor

    /**
     * Retrieves all new messages since the last history id was recorded. If no history
     * id can be found, then the history id of the most recent message is used.
     * Finally, the history id of the most recent message is stored, if any were returned.
     *
     * @return the list of new messages
     */
    List<Message> syncNewMessages() {
        //noinspection GroovyAssignabilityCheck
        String historyId = historyFileAccessor.getSavedHistoryId().orElseGet { -> gmailAccessor.getMostRecentHistoryId() }

        List<Message> messages = gmailAccessor.getMessagesAddedSinceHistoryId(historyId)

        if (messages) {
            historyFileAccessor.setSavedHistoryId(messages.last().getHistoryId().toString()) //todo this isn't saving
        }

        return messages
    }

    /**
     * Syncs new messages on startup and every 10 seconds after.
     * Will eventually be moved into its own class.
     */
    @Scheduled(initialDelay = 0L, fixedRate = 10000L)
    void scheduledSync() {
        LOG.info("Performing scheduled sync.")
        syncNewMessages().each{EmailParser.parseEmail(it)}
    }

}
