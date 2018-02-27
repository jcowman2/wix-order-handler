package org.pupcycle.wixorderhandler

import groovy.transform.CompileStatic
import org.pupcycle.wixorderhandler.accessor.HistoryFileAccessor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

/**
 * Manages the {@code HistoryFileAccessor}.
 *
 * @author Joe Cowman
 */
@Component
@CompileStatic
class HistoryManager {

    @Autowired
    private HistoryFileAccessor historyFileAccessor

    /**
     * Gets the saved history id, if exists.
     * @return the optional history id
     */
    Optional<String> getSavedHistoryId() {
        return historyFileAccessor.getHistoryIfExists()
    }

    /**
     * Saves the history id.
     * @param id    the history id
     */
    void setSavedHistoryId(String id) {
        historyFileAccessor.writeValue(id)
    }

}
