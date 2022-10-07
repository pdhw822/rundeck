package org.rundeck.features.events

import com.rundeck.feature.api.output.ActionOutputEvent
import com.rundeck.feature.api.output.OutputLevel

class DefaultActionOutputEvent implements ActionOutputEvent {
    String actionId
    String user
    String producer
    Long timestamp
    OutputLevel level
    String message
}
