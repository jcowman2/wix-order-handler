package org.pupcycle.wixorderhandler

import com.google.api.services.gmail.model.Message
import com.google.api.services.gmail.model.MessagePart

class EmailParser {

    static void parseEmail(Message message) {
        MessagePart payload = message.getPayload()

        String to = getHeader(payload, 'To')
        String from = getHeader(payload, 'From')
        String subject = getHeader(payload, 'Subject')

        String body = getBody(payload)
        System.out.println(body)
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
