package org.pupcycle.wixorderhandler

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
     * If the file exists but is empty, an {@code Coptiona.empty} is returned.
     *
     * @return the contents of the file, if present
     */
    Optional<String> getHistoryIfExists() {
        File f = new File(historyFilePath)
        return f.createNewFile() ? Optional.empty() : Optional.of(f.text).filter{!!it}
    }
}
