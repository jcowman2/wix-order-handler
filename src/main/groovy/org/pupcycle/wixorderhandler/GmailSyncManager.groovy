package org.pupcycle.wixorderhandler

import com.google.api.services.gmail.Gmail
import com.google.api.services.gmail.model.ListMessagesResponse
import com.google.api.services.gmail.model.Message
import groovy.transform.CompileStatic
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
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
     * Retrieves all emails in the user's account. Not needed at the moment.
     * @return A list of all messages, from newest to oldest
     */
    List<Message> fullSync() {
        LOG.info("Attempting full synchronization of user messages.")

        Gmail.Users.Messages.List listRequest = gmailService.users().messages().list("me")
        ListMessagesResponse response = listRequest.execute()
        List<Message> messages = []

        while (response.getMessages()) {
            messages += response.getMessages()

            if (response.getNextPageToken()) {
                String pageToken = response.getNextPageToken()
                response = listRequest.setPageToken(pageToken).execute()
            } else {
                break
            }
        }

        LOG.info("Synchronized user messages. Retrieved ${messages.size()} messages. Most recent id: ${messages.first().getId()}")

        return messages
    }

    /**
     * Gets the id of the most recently received message in the Gmail inbox.
     * @return  the message id
     */
    String getMostRecentMessageId() {
        ListMessagesResponse response = gmailService.users().messages().list("me").setMaxResults(1L).execute()
        Message message = response.getMessages().first()
        LOG.info("Retrieved most recent message id. {id: ${message.getId()}}")
        return message.getId()
    }

    /**
     * Gets a single message from the Gmail account by the message's id. This method is
     * required because the messages returned by {@code messages.list} don't contain a historyId.
     * @param id    the message id
     * @return      the message resource
     */
    Message getMessage(String id) {
        Message message = gmailService.users().messages().get("me", id).execute()
        LOG.info("Retrieved message with {id: ${message.getId()}, historyId: ${message.getHistoryId()}}.")
        return message
    }

}
