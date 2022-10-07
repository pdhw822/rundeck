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

    String barf() {
        StringBuilder out = new StringBuilder()
        List<ActionStartEvent> events = new ArrayList(storedStartEvents.values())
        events.sort { a, b -> a.timestamp <=> b.timestamp }
        events.each { se ->
            out << "${se.timestamp} ${se.actionId} ${se.producer} ${se.user} ${se.feature} ${se.action}\n"
            storedEvents[se.actionId].sort { a, b -> a.timestamp <=> b.timestamp }.each { e ->
                out << "${e.timestamp} ${e.actionId} ${e.producer} ${e.user} ${e.level} ${e.message}\n"
            }
            def ce = storedCompletionEvents[se.actionId]
            if(ce) out << "${ce.timestamp} ${ce.actionId} ${ce.producer} ${ce.user} ${ce.status}\n\n"
        }
        return out.toString()
    }
}
