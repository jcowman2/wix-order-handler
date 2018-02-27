package org.pupcycle.wixorderhandler

import groovy.transform.CompileStatic
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
@CompileStatic
class HistoryManager {

    @Autowired
    HistoryFileAccessor historyFileAccessor

}
