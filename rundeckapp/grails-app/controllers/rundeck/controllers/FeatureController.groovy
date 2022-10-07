package rundeck.controllers

import com.fasterxml.jackson.databind.ObjectMapper
import com.rundeck.feature.api.context.ContextKeys
import com.rundeck.feature.api.event.ActionEventPublisher
import com.rundeck.feature.api.registry.FeatureRegistry
import grails.converters.JSON
import org.rundeck.features.context.DefaultFeatureActionContext
import org.rundeck.features.events.DefaultActionCompleteEvent
import org.rundeck.features.events.DefaultExecuteFeatureActionEvent
import org.rundeck.features.events.DefaultStartActionEvent
import org.rundeck.features.models.response.ExecuteFeatureActionResponse
import org.rundeck.features.output.FeatureActionOutputStore
import org.rundeck.features.output.MemoryFeatureActionOutputStore
import org.springframework.beans.factory.annotation.Autowired

class FeatureController {

    @Autowired
    ActionEventPublisher actionEventPublisher

    @Autowired
    FeatureActionOutputStore featureActionOutputStore

    @Autowired
    FeatureRegistry featureRegistry

    def ui() {

    }

    def executeFeatureAction() {
        def execFeatureEvt = new DefaultExecuteFeatureActionEvent()
        execFeatureEvt.actionId = UUID.randomUUID().toString()
        execFeatureEvt.feature = params.featureName
        execFeatureEvt.action = params.actionName
        execFeatureEvt.actionDataJson = request.inputStream.text
        execFeatureEvt.user = session.user
        actionEventPublisher.publishExecuteFeatureAction(execFeatureEvt)
        render new ExecuteFeatureActionResponse(actionId: execFeatureEvt.actionId) as JSON
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

    def barf() {
        render text: ((MemoryFeatureActionOutputStore)featureActionOutputStore).barf(), contentType: "text/plain;utf-8"
    }

}
