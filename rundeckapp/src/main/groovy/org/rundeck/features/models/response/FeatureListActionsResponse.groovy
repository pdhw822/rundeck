package org.rundeck.features.models.response

class FeatureListActionsResponse {
    String feature
    List<ActionInfo> actions = []

    static class ActionInfo {
        String name
        String description
        Object sampleData
    }
}
