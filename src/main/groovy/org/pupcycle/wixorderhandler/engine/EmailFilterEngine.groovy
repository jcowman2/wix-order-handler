package org.pupcycle.wixorderhandler.engine

import groovy.transform.CompileStatic
import org.pupcycle.wixorderhandler.model.Email
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * Logic for filtering emails by sender or subject.
 *
 * @author Joe Cowman
 */
@Component
@CompileStatic
class EmailFilterEngine {

    @Value('${filter.expected-sender}')
    private String expectedSender

    @Value('${filter.expected-subject}')
    private String expectedSubject

    /**
     * Filters emails by the configured sender and subject
     * @param emails    the emails to be filtered
     * @return the filtered emails
     */
    List<Email> filterEmails(List<Email> emails) {
        emails = filterBySender(emails, expectedSender)
        return filterBySubject(emails, expectedSubject)
    }

    /**
     * Filters emails by sender.
     * @param emails    the emails to be filtered
     * @param sender    the sender
     * @return the filtered emails
     */
    static List<Email> filterBySender(List<Email> emails, String sender) {
        emails.findAll { it.from.contains(sender) }
    }

    /**
     * Filters emails by subject.
     * @param emails    the emails to be filtered
     * @param subject   the subject
     * @return the filtered emails
     */
    static List<Email> filterBySubject(List<Email> emails, String subject) {
        emails.findAll { it.subject.contains(subject) }
    }

}
