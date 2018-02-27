package org.pupcycle.wixorderhandler

import groovy.transform.CompileStatic
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

    Optional<String> getSavedHistoryId() {
        return historyFileAccessor.getHistoryIfExists()
    }

    void setSavedHistoryId(String id) {
        historyFileAccessor.writeValue(id)
    }

}
