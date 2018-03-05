package org.pupcycle.wixorderhandler.engine

import com.google.api.services.gmail.model.Message
import groovy.transform.CompileStatic
import org.pupcycle.wixorderhandler.accessor.GmailAccessor
import org.pupcycle.wixorderhandler.accessor.HistoryFileAccessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Manages message retrieval from Gmail and storing of most recent history ids.
 *
 * @author Joe Cowman
 */
@Component
@CompileStatic
class GmailSyncEngine {

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
        historyId = messages.isEmpty() ? historyId : messages.last().getHistoryId().toString()

        historyFileAccessor.setSavedHistoryId(historyId)

        return messages
    }

}
