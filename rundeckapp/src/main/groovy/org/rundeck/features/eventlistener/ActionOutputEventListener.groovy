package org.rundeck.features.eventlistener

import com.rundeck.feature.api.event.ActionCompleteEvent
import com.rundeck.feature.api.event.ActionEventPublisher
import com.rundeck.feature.api.event.ActionStartEvent
import com.rundeck.feature.api.output.ActionOutputEvent
import grails.events.annotation.Subscriber
import grails.events.bus.EventBusAware
import org.rundeck.features.output.FeatureActionOutputStore
import org.springframework.beans.factory.annotation.Autowired

class ActionOutputEventListener implements EventBusAware {

    @Autowired
    FeatureActionOutputStore featureActionOutputStore

    @Subscriber(ActionEventPublisher.OUTPUT_EVENT_CHANNEL)
    void onLogEvent(ActionOutputEvent evt) {
        featureActionOutputStore.storeOutput(evt)
    }

    @Subscriber(ActionEventPublisher.START_EVENT_CHANNEL)
    onStartEvent(ActionStartEvent evt) {
        featureActionOutputStore.storeStart(evt)
    }

    @Subscriber(ActionEventPublisher.COMPLETION_EVENT_CHANNEL)
    onCompleteEvent(ActionCompleteEvent evt) {
        featureActionOutputStore.storeCompletion(evt)
    }
}
