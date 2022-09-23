package org.rundeck.features.models.response

class FeatureListResponse {
    List<FeatureInfo> features = []

    static class FeatureInfo {
        String name
        String description
        boolean enabled
    }
}
