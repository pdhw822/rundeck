package org.rundeck.features.events

import com.rundeck.feature.api.event.ExecuteFeatureActionEvent

class DefaultExecuteFeatureActionEvent implements ExecuteFeatureActionEvent {
    String actionId
    String user
    String producer
    Long timestamp = System.nanoTime()
    String feature
    String action
    String actionDataJson
}
