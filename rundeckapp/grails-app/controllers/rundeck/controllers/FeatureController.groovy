package rundeck.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.rundeck.feature.api.context.FeatureActionContext
import com.rundeck.feature.api.event.ActionEventPublisher
import com.rundeck.feature.api.registry.FeatureRegistry
import grails.converters.JSON
import org.rundeck.features.context.DefaultFeatureActionContext
import org.rundeck.features.events.DefaultActionCompleteEvent
import org.rundeck.features.events.DefaultStartActionEvent
import org.rundeck.features.models.response.ExecuteFeatureActionResponse
import org.rundeck.features.output.FeatureActionOutputStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

class FeatureController {

    @Autowired
    FeatureRegistry featureRegistry

    @Autowired
    ActionEventPublisher actionEventPublisher

    @Autowired
    FeatureActionOutputStore featureActionOutputStore

    @Autowired
    ApplicationContext applicationContext

    @Autowired
    ObjectMapper mapper

    def ui() {

    }

    def executeFeatureAction() {
        //TODO: Move the action execution to a service that executes actions in a their own thread

        String featureName = params.featureName
        String actionName = params.actionName
        String actionExecUuid = UUID.randomUUID().toString();

        DefaultFeatureActionContext context = new DefaultFeatureActionContext(actionExecUuid,actionEventPublisher);

        try {
            def feature = featureRegistry.getFeature(featureName)
            def action = featureRegistry.getFeatureAction(featureName, actionName);
            def payload = request.inputStream.bytes
            if(action.getFeatureActionDataClass() != Void.class && payload != null && payload.length > 0) {
                def actionData = mapper.readValue(payload, action.getFeatureActionDataClass());
                context.put(FeatureActionContext.KEY_ACTION_DATA, actionData);
            }
            if(feature.configuration) {
                context.put(FeatureActionContext.KEY_FEATURE_CONFIG, feature.configuration)
            }
            context.put("spring-context", applicationContext)
            actionEventPublisher.publishStart(new DefaultStartActionEvent(actionId: actionExecUuid, feature: featureName, action: actionName, initiator: session.user))
            def completionStatus = action.execute(context);
            actionEventPublisher.publishCompletion(new DefaultActionCompleteEvent(actionId: actionExecUuid, status: completionStatus))
            render new ExecuteFeatureActionResponse(actionId: actionExecUuid) as JSON
        }  catch(Exception e) {
            e.printStackTrace();
            render new ExecuteFeatureActionResponse(actionId: actionExecUuid, error: e.cause?.message ?: e.message) as JSON
        }
    }

    def featureActionDataDefinition() {
        String featureName = params.featureName
        String actionName = params.actionName

        def action = featureRegistry.getFeatureAction(featureName, actionName);
        render action.sampleActionData as JSON
    }

    def featureOutput() {
        String actionId = params.id
        int offset = params.offset ? params.offset.toInteger() : 0
        int max = params.max ? params.max.toInteger() : 200

        render featureActionOutputStore.retrieveOutput(actionId, offset, max) as JSON
    }


}
