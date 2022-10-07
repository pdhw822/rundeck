package org.rundeck.features.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.rundeck.feature.api.context.ContextKeys
import com.rundeck.feature.api.event.ActionEventPublisher
import com.rundeck.feature.api.event.ExecuteFeatureActionEvent
import com.rundeck.feature.api.model.CompletionStatus
import com.rundeck.feature.api.output.OutputLevel
import com.rundeck.feature.api.registry.FeatureRegistry
import org.rundeck.features.context.DefaultFeatureActionContext
import org.rundeck.features.events.DefaultActionCompleteEvent
import org.rundeck.features.events.DefaultActionOutputEvent
import org.rundeck.features.events.DefaultStartActionEvent
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

class FeatureActionExecutorService {
    @Autowired
    ActionEventPublisher actionEventPublisher
    @Autowired
    FeatureRegistry featureRegistry
    @Autowired
    ApplicationContext applicationContext
    @Autowired
    ObjectMapper mapper

    String serverId = System.getProperty("rundeck.server.uuid")

    void execute(ExecuteFeatureActionEvent evt) {
        String aid = evt.actionId ?: UUID.randomUUID().toString()
        DefaultFeatureActionContext context = new DefaultFeatureActionContext(aid, evt.user, serverId, actionEventPublisher);
        actionEventPublisher.publishStart(new DefaultStartActionEvent(actionId: aid, feature: evt.feature, action: evt.action, user: evt.user, producer: serverId))
        def completionStatus = CompletionStatus.ERROR
        try {
            def feature = featureRegistry.getFeature(evt.feature)
            def action = featureRegistry.getFeatureAction(evt.feature, evt.action);
            def payload = evt.actionDataJson
            if(action.getFeatureActionDataClass() != Void.class && payload != null && payload?.length() > 0) {
                def actionData = mapper.readValue(payload, action.getFeatureActionDataClass());
                context.put(ContextKeys.ACTION_DATA, actionData);
            }
            if(feature.configuration) {
                context.put(ContextKeys.FEATURE_CONFIG, feature.configuration)
            }
            context.put("spring-context", applicationContext)

            completionStatus = action.execute(context);
        }  catch(Exception e) {
            e.printStackTrace();
            actionEventPublisher.publishOutput(new DefaultActionOutputEvent(actionId: aid, level: OutputLevel.ERROR, message: e.getMessage(), user: evt.user, producer: serverId, timestamp: System.nanoTime()))
        }
        actionEventPublisher.publishCompletion(new DefaultActionCompleteEvent(actionId: aid, user:evt.user, producer: serverId, status: completionStatus))

    }
}
