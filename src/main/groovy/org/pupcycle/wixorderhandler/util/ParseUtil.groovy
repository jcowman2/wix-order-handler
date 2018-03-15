package org.pupcycle.wixorderhandler.util

import java.util.regex.Matcher

/**
 * Contains utility methods for extracting data from email body text using Regex.
 *
 * @author Joe Cowman
 */
class ParseUtil {

    /**
     * Extracts a value from a single line of the text, given a leading pattern.
     * Example: extractValue('test val\n other text', 'test') == 'val'
     * @param text              the text from which the value is extracted.
     *                          Can be multiline.
     * @param lead              the leading pattern before the desired value.
     *                          Will not be included in the returned value.
     * @param requireNumeric    if true, only a numeric value will be matched
     * @param isRequired        if false, an exception will be thrown on no
     *                          match. Otherwise, an empty string will return.
     * @return  the matched value
     */
    static String extractValue(String text, String lead, boolean requireNumeric = false, boolean isRequired = true) {
        String pattern = /(?m)^${lead}\s*(${requireNumeric ? /\d/ : /./}*)/
        return possibleMatch(text =~ pattern, isRequired)
    }

    /**
     * Extracts a multiline value in the form of a list from the text,
     * given a leading and ending pattern. The returned list has no
     * empty strings or carriage returns, and spaces are inserted where
     * linebreaks do not have succeeding whitespace.
     * Commas at the ends of lines are removed.
     * Values surrounded by <...> are removed.
     * @param text          the text from which the value is returned
     * @param lead          the leading pattern before the desired value.
     *                      Will not be included in the returned value.
     * @param tail          the ending pattern after the desired value.
     *                      Will not be included in the desired value
     * @param isRequired    if false, an exception will be thrown on no
     *                      match. Otherwise, an empty list will return.
     * @return  the multiline value
     */
    static List<String> extractMultilineValue(String text, String lead, String tail, boolean isRequired = true) {
        String pattern = /(?ms)${lead}(.*)${tail}/
        String value = possibleMatch(text =~ pattern, isRequired)
        String cleanedValue = value.replaceAll('\r\n', '\n')
                                   .replaceAll('\n(\\S)', '\n $1')
                                   .replaceAll('<.*>', '')
                                   .replaceAll(',\n', '\n')
        return cleanedValue.split('\n').findAll { !it.isAllWhitespace() }*.trim()
    }

    /**
     * Returns the first matching value from the matcher, if exists.
     * If not, and {@code required} is true, an exception will be thrown.
     * Otherwise, an empty string will be returned.
     * @param m             the matcher
     * @param required      whether the match is required
     * @return  the matched value, if exists
     */
    static String possibleMatch(Matcher m, boolean required) {
        try {
            return m[0][1]
        } catch (IndexOutOfBoundsException e) {
            if (required) {
                throw new RuntimeException('No match found.', e) //todo more specific exception
            } else {
                return ''
            }
        }
    }

}
