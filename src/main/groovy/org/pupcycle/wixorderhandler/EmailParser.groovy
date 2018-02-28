package org.pupcycle.wixorderhandler

import com.google.api.services.gmail.model.Message
import com.google.api.services.gmail.model.MessagePart
import com.google.api.services.gmail.model.MessagePartBody

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
     * mimeType equal to 'text/plain' is returned.
     *
     * @param payload   the root email part
     * @return the decoded body text
     */
    private static String getBody(MessagePart payload) {
        MessagePartBody body = (payload.getBody().getData()) ? payload.getBody()
                : payload.getParts().find{it.getMimeType() == 'text/plain'}.getBody()
        return new String(body.decodeData())
    }
}
