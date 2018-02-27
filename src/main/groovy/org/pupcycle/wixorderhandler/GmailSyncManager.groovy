package org.pupcycle.wixorderhandler

import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.model.ListHistoryResponse
import com.google.api.services.gmail.model.ListMessagesResponse
import com.google.api.services.gmail.model.Message
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Methods to manage notification and email sync with Gmail. //todo break into accessor layer
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
     * Gets a single message from the Gmail account by the message's id. This method is
     * required because the messages returned by {@code messages.list} don't contain a historyId.
     * @param id    the message id
     * @return the message resource
     */
    Message getMessage(String id) {
        Message message = gmailService.users().messages().get("me", id).execute()
        LOG.info("Retrieved message with {id: ${message.getId()}, historyId: ${message.getHistoryId()}}.")
        return message
    }

    /**
     * Gets the id of the most recently received message in the Gmail inbox.
     * @return the message id
     */
    String getMostRecentMessageId() {
        ListMessagesResponse response = gmailService.users().messages().list("me").setMaxResults(1L).execute()
        Message message = response.getMessages().first()
        LOG.info("Retrieved most recent message id. {id: ${message.getId()}}")
        return message.getId()
    }

    /**
     * Gets the history id of the most recently received message in the Gmail inbox.
     * @return the history id
     */
    String getMostRecentHistoryId() {
        return getMessage(getMostRecentMessageId()).getHistoryId()
    }

    /**
     * Gets all messages received since the given history id.
     * @param historyId     the history id
     * @return a list of new messages
     */
    List<Message> getMessagesAddedSinceHistoryId(String historyId) {
        LOG.info("Attempting partial synchronization of user messages since {historyId: $historyId}.")

        Gmail.Users.History.List listRequest = gmailService.users().history().list("me")
                .setHistoryTypes(["messageAdded"]).setStartHistoryId(historyId as BigInteger)
        ListHistoryResponse response = listRequest.execute()
        List<Message> messages = []

        while (response.getHistory()) {
            response.getHistory().each { h ->
                messages += h.getMessages()
            }

            if (response.getNextPageToken()) {
                String pageToken = response.getNextPageToken()
                response = listRequest.setPageToken(pageToken).execute()
            } else {
                break
            }
        }

        LOG.info("Synchronized user messages. Returned {count: ${messages.size()}} messages.")

        messages = messages.collect { getMessage(it.getId()) }

        return messages
    }

}
