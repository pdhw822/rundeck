package org.rundeck.features.eventpublisher

import com.rundeck.feature.api.event.ActionCompleteEvent
import com.rundeck.feature.api.event.ActionEvent
import com.rundeck.feature.api.event.ActionEventPublisher
import com.rundeck.feature.api.event.ActionStartEvent
import com.rundeck.feature.api.output.ActionOutputEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationEventPublisher

class SpringAppActionEventPublisher implements ActionEventPublisher {

    @Autowired
    ApplicationEventPublisher eventPublisher;

    @Override
    public void publish(String s, ActionEvent actionEvent) {
        eventPublisher.publishEvent(actionEvent);
    }

    @Override
    public void publishOutput(ActionOutputEvent actionEvent) {
        eventPublisher.publishEvent(actionEvent);
    }

    @Override
    public void publishCompletion(ActionCompleteEvent actionCompleteEvent) {
        eventPublisher.publishEvent(actionCompleteEvent);
    }

    @Override
    void publishStart(ActionStartEvent eventData) {
        eventPublisher.publishEvent(eventData)
    }
}
