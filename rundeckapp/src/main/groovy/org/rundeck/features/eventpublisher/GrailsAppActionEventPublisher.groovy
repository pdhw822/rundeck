package org.rundeck.features.eventpublisher

import com.rundeck.feature.api.event.ActionCompleteEvent
import com.rundeck.feature.api.event.ActionEvent
import com.rundeck.feature.api.event.ActionEventPublisher
import com.rundeck.feature.api.event.ActionStartEvent
import com.rundeck.feature.api.event.ExecuteFeatureActionEvent
import com.rundeck.feature.api.output.ActionOutputEvent
import grails.events.bus.EventBus
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext
import org.springframework.context.ApplicationEventPublisher

class GrailsAppActionEventPublisher implements ActionEventPublisher {

    @Autowired
    EventBus eventBus

    @Autowired
    ApplicationContext applicationContext

    @Override
    void publish(String event, ActionEvent eventData) {
        eventBus.publish(event, eventData)
        applicationContext.publishEvent(eventData)
    }

    @Override
    void publishOutput(ActionOutputEvent eventData) {
        eventBus.publish(ActionEventPublisher.OUTPUT_EVENT_CHANNEL, eventData)
        applicationContext.publishEvent(eventData)
    }

    @Override
    void publishCompletion(ActionCompleteEvent eventData) {
        eventBus.publish(ActionEventPublisher.COMPLETION_EVENT_CHANNEL, eventData)
        applicationContext.publishEvent(eventData)
    }

    @Override
    void publishStart(ActionStartEvent eventData) {
        eventBus.publish(ActionEventPublisher.START_EVENT_CHANNEL, eventData)
        applicationContext.publishEvent(eventData)
    }

    @Override
    void publishExecuteFeatureAction(ExecuteFeatureActionEvent eventData) {
        eventBus.publish(ActionEventPublisher.EXECUTE_ACTION_EVENT_CHANNEL, eventData)
        applicationContext.publishEvent(eventData)
    }
}
