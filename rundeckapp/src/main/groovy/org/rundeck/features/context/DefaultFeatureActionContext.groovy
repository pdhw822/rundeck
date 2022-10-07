package org.rundeck.features.context

import com.rundeck.feature.api.context.FeatureActionContext
import com.rundeck.feature.api.event.ActionEventPublisher

class DefaultFeatureActionContext implements FeatureActionContext {

    String actionId;
    String user;
    String producer;
    Map<String, Object> context = new HashMap<>();
    ActionEventPublisher eventPublisher;

    DefaultFeatureActionContext(String actionId, String user, String producer, ActionEventPublisher eventPublisher) {
        this.actionId = actionId
        this.user = user
        this.producer = producer
        this.eventPublisher = eventPublisher
    }

    public void put(String key, Object contextObject) {
        context.put(key, contextObject);
    }

    @Override
    public <T> T get(String key, Class<T> objectType) {
        return (T)context.get(key);
    }
}
