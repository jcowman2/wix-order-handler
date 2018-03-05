package org.pupcycle.wixorderhandler

import com.google.api.services.gmail.model.Message
import com.google.api.services.gmail.model.MessagePart
import groovy.transform.CompileStatic
import org.pupcycle.wixorderhandler.model.Email

/**
 * Utility for parsing Gmail messages into simpler objects.
 *
 * @author Joe Cowman
 */
@CompileStatic
class EmailParser {

    /**
     * Parses a Gmail {@code Message} into an Email model.
     * @param message       the message to be parsed
     * @return the email
     */
    static Email parseEmail(Message message) {
        MessagePart payload = message.getPayload()

        String to = getHeader(payload, 'To')
        String from = getHeader(payload, 'From')
        String subject = getHeader(payload, 'Subject')
        String date = getHeader(payload, 'Date')

        String body = getBody(payload)

        return new Email(to: to, from: from, subject: subject, date: date, body: body)
    }

    /**
     * Gets value of the given email header.
     * @param payload   the root email part
     * @param key       the header's name
     * @return the value of that header
     */
    private static String getHeader(MessagePart payload, String key) {
        return payload.getHeaders().find{ (it.getName() == key) }.getValue()
    }

    /**
     * Gets decoded contents of body from email. If the email contains only one part,
     * then that data is returned. Otherwise, the data from the first part with
     * mimeType equal to 'text/plain' is returned. If there are no 'text/plain' parts,
     * then the first part is returned, regardless of mimeType.
     *
     * @param payload   the root email part
     * @return the decoded body text
     */
    private static String getBody(MessagePart payload) {
        MessagePart part = (payload.getBody().getData()) ?
                payload : //Single part body, or:
                payload.getParts().stream()
                        .filter{it.getMimeType() == 'text/plain'} //Text part of multipart body
                        .findFirst()
                        .orElse(payload.getParts().first()) //If no text part, just first part

        return new String(part.getBody().decodeData())
    }
}
