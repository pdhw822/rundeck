package org.rundeck.features.models.response

import com.rundeck.feature.api.event.ActionCompleteEvent
import com.rundeck.feature.api.event.ActionStartEvent
import com.rundeck.feature.api.output.ActionOutputEvent

class FeatureActionResponse {
    String id
    List<ActionOutputEvent> events
    ActionStartEvent startEvent
    ActionCompleteEvent completeEvent
    int offset
    int max
    int total
}
