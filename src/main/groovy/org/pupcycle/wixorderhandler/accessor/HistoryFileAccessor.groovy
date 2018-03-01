package org.pupcycle.wixorderhandler.accessor

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

/**
 * I/O operations for the file that contains the Gmail history data.
 *
 * @author Joe Cowman
 */
@Component
class HistoryFileAccessor {

    @Value('${history.path}')
    private String historyFilePath

    /**
     * Attempts to return the contents of the history file, if it exists.
     * If the file does not exist, the file is created and an {@code Optional.empty} is returned.
     * If the file exists but is empty, an {@code Optional.empty} is returned.
     *
     * @return an optional with the contents of the file, if present
     */
    Optional<String> getSavedHistoryId() {
        File f = new File(historyFilePath)
        return f.createNewFile() ? Optional.empty() : Optional.of(f.text).filter{!!it}
    }

    /**
     * Records the value to the history file.
     * Will attempt to generate the file if it does not exist, but not guaranteed.
     *
     * @param id     the value to be recorded
     */
    void setSavedHistoryId(String id) {
        new File(historyFilePath).text = id
    }

}
