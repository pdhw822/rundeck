package org.rundeck.features.output

import com.rundeck.feature.api.event.ActionCompleteEvent
import com.rundeck.feature.api.event.ActionStartEvent
import com.rundeck.feature.api.output.ActionOutputEvent
import org.rundeck.features.models.response.FeatureActionResponse

interface FeatureActionOutputStore {

    void storeOutput(ActionOutputEvent evt)
    void storeStart(ActionStartEvent evt)
    void storeCompletion(ActionCompleteEvent evt)

    //TODO: return a reactive type of some sort?
    FeatureActionResponse retrieveOutput(String actionId, int offset, int max)

}