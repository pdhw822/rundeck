package org.rundeck.features.events

import com.rundeck.feature.api.event.ActionStartEvent

class DefaultStartActionEvent implements ActionStartEvent {
    String actionId
    String user
    String producer
    String feature
    String action
    Long timestamp = System.nanoTime()
}
