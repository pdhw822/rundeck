package org.rundeck.features.eventlistener

import com.rundeck.feature.api.event.ActionEventPublisher
import com.rundeck.feature.api.event.ExecuteFeatureActionEvent
import grails.events.annotation.Subscriber
import grails.events.bus.EventBusAware
import org.rundeck.features.service.FeatureActionExecutorService
import org.springframework.beans.factory.annotation.Autowired

class EventBusFeatureActionExecutorService implements EventBusAware {

    @Autowired
    FeatureActionExecutorService featureActionExecutorService

    @Subscriber(ActionEventPublisher.EXECUTE_ACTION_EVENT_CHANNEL)
    void onExecuteFeatureAction(ExecuteFeatureActionEvent evt) {
        featureActionExecutorService.execute(evt)
    }
}
