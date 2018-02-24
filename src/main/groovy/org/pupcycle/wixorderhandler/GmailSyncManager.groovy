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
     * Retrieves all emails in the user's account.
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

}
