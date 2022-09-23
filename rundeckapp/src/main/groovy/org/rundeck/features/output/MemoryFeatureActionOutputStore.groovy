package org.rundeck.features.output

import com.rundeck.feature.api.event.ActionCompleteEvent
import com.rundeck.feature.api.event.ActionStartEvent
import com.rundeck.feature.api.output.ActionOutputEvent
import org.rundeck.features.models.response.FeatureActionResponse

class MemoryFeatureActionOutputStore implements FeatureActionOutputStore {
    Map<String, List<ActionOutputEvent>> storedEvents = [:]
    Map<String, ActionCompleteEvent> storedCompletionEvents = [:]
    Map<String, ActionStartEvent> storedStartEvents = [:]

    @Override
    void storeOutput(ActionOutputEvent evt) {
        storedEvents.putIfAbsent(evt.actionId, [])
        storedEvents[evt.actionId].add(evt)
    }

    @Override
    void storeStart(ActionStartEvent evt) {
        storedStartEvents[evt.actionId] = evt
    }

    @Override
    void storeCompletion(ActionCompleteEvent evt) {
        storedCompletionEvents[evt.actionId] = evt
    }

    @Override
    FeatureActionResponse retrieveOutput(String actionId, int offset, int max) {
        List<ActionOutputEvent> events = storedEvents[actionId] ?: []
        int sidx = events.size() > offset ? offset : events.size() - 1
        int eidx = events.size() > offset+max ? offset+max : events.size()
        FeatureActionResponse rsp = new FeatureActionResponse()
        rsp.id = actionId
        rsp.offset = offset
        rsp.max = max
        rsp.total = events.size()
        rsp.events = offset >= events.size() ? [] : storedEvents[actionId]?.subList(sidx, eidx)
        rsp.startEvent = storedStartEvents[actionId]
        rsp.completeEvent = storedCompletionEvents[actionId]
        return rsp
    }


}
