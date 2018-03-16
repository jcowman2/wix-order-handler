package org.pupcycle.wixorderhandler.model

import groovy.transform.CompileStatic
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString

/**
 * Email model.
 *
 * @author Joe Cowman
 */
@EqualsAndHashCode
@ToString
@CompileStatic
class Email {
    String to
    String from
    String subject
    String date
    String body
}
