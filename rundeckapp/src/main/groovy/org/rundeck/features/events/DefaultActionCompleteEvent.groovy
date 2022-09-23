package org.rundeck.features.events

import com.rundeck.feature.api.event.ActionCompleteEvent
import com.rundeck.feature.api.model.CompletionStatus
import com.rundeck.feature.api.output.OutputLevel

class DefaultActionCompleteEvent implements ActionCompleteEvent {
    String actionId
    CompletionStatus status
    Long timestamp = System.nanoTime()
}
