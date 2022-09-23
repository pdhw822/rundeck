package rundeck.controllers

import grails.converters.JSON
import org.rundeck.features.models.request.ToggleFeatureEnabledRequest
import org.rundeck.features.models.response.FeatureListActionsResponse
import org.rundeck.features.models.response.FeatureListResponse
import org.rundeck.features.models.response.ToggleFeatureMessageResponse
import org.rundeck.features.registry.SpringFeatureRegistry
import org.springframework.beans.factory.annotation.Autowired

class FeatureAdminController {

    @Autowired
    SpringFeatureRegistry featureRegistry;

    def listFeatures() {
        def rsp = new FeatureListResponse()
        featureRegistry.listFeatures().each { ftr ->
            rsp.features.add(new FeatureListResponse.FeatureInfo(name: ftr.name, description: ftr.description, enabled: ftr.enabled))
        }
        render rsp as JSON
    }

    def listActions() {
        def rsp = new FeatureListActionsResponse()
        featureRegistry.getFeature(params.featureName).actions.each { act ->
            rsp.feature = params.featureName
            rsp.actions.add(new FeatureListActionsResponse.ActionInfo(name:act.name, description: act.description, sampleData: act.sampleActionData))
        }
        render rsp as JSON
    }

    def toggleEnabled(ToggleFeatureEnabledRequest rq) {
        featureRegistry.toggleFeatureEnabled(params.featureName, rq.enabled)
        render new ToggleFeatureMessageResponse(message: "${params.featureName} is ${rq.enabled?"enabled":"disabled"}") as JSON

    }

}
