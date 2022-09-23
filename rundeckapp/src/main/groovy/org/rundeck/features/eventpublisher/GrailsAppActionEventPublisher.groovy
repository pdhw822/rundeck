package org.rundeck.features.eventpublisher

import com.rundeck.feature.api.event.ActionCompleteEvent
import com.rundeck.feature.api.event.ActionEvent
import com.rundeck.feature.api.event.ActionEventPublisher
import com.rundeck.feature.api.event.ActionStartEvent
import com.rundeck.feature.api.output.ActionOutputEvent
import grails.events.bus.EventBus
import org.springframework.beans.factory.annotation.Autowired

class GrailsAppActionEventPublisher implements ActionEventPublisher {

    @Autowired
    EventBus eventBus

    @Override
    void publish(String event, ActionEvent eventData) {
        eventBus.publish(event, eventData)
    }

    @Override
    void publishOutput(ActionOutputEvent eventData) {
        eventBus.publish(ActionEventPublisher.OUTPUT_EVENT_CHANNEL, eventData)
    }

    @Override
    void publishCompletion(ActionCompleteEvent eventData) {
        eventBus.publish(ActionEventPublisher.COMPLETION_EVENT_CHANNEL, eventData)
    }

    @Override
    void publishStart(ActionStartEvent eventData) {
        eventBus.publish(ActionEventPublisher.START_EVENT_CHANNEL, eventData)
    }

}
