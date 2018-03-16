package org.pupcycle.wixorderhandler.exception

import groovy.transform.CompileStatic
import groovy.transform.InheritConstructors

/**
 * Exception thrown when no Regex match is found in a body of text.
 *
 * @author Joe Cowman
 */
@CompileStatic
@InheritConstructors
class NoMatchFoundException extends RuntimeException {
}
