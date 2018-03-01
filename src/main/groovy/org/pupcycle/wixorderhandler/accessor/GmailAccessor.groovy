package org.pupcycle.wixorderhandler.accessor

import com.google.api.client.googleapis.json.GoogleJsonResponseException
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
 * Retrieves messages and edit history from the Gmail client.
 *
 * @author Joe Cowman
 */
@Component
@CompileStatic
class GmailAccessor {

    private static final Logger LOG = LoggerFactory.getLogger(GmailAccessor.class)

    @Autowired
    Gmail gmailService

    /**
     * Gets a single message from the Gmail account by the message's id. This method is
     * required because the messages returned by {@code messages.list} don't contain a historyId.
     *
     * If the message id provided isn't found (such as in the case of a draft), the error will
     * be logged and an empty optional will be returned.
     *
     * @param id    the message id
     * @return the message resource, if present
     */
    Optional<Message> getMessage(String id) {
        Optional<Message> returnMessage

        try {
            Message message = gmailService.users().messages().get("me", id).execute()
            LOG.debug("Retrieved message with {id: ${message.getId()}, historyId: ${message.getHistoryId()}}.")
            returnMessage = Optional.of(message)
        } catch (GoogleJsonResponseException e) {
            if (e.details.getCode() == 404) {
                LOG.error("Message with {id: $id} not found. It may be a draft.")
                returnMessage = Optional.empty()
            } else {
                throw e
            }
        }

        return returnMessage
    }

    /**
     * Gets the id of the most recently added message in the Gmail inbox, or
     * nth-most recently added message if offset is provided.
     * @param offset    if the most recent message was a draft, the offset
     *                  should be incremented to get the most recently received
     *                  email
     * @return the message id
     */
    String getMostRecentMessageId(int offset = 0) {
        ListMessagesResponse response = gmailService.users().messages().list("me").setMaxResults(1L + offset).execute()
        Message message = response.getMessages().last()
        LOG.info("Retrieved most recent message id. {id: ${message.getId()}}")
        return message.getId()
    }

    /**
     * Gets the history id of the most recently added message in the Gmail inbox.
     * If the most recently added message is a draft, then it retries until finding
     * a received message.
     *
     * @return the history id
     */
    String getMostRecentHistoryId() {
        Optional<Message> optional = Optional.empty()
        int offset = 0

        while (!optional.isPresent()) {
            optional = getMessage(getMostRecentMessageId(offset))
            offset++
        }

        return optional.get().getHistoryId()
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

        messages = messages.collect { getMessage(it.getId()).orElse(null) }
        messages.removeAll { it == null }

        return messages
    }

}
