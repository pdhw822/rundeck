package org.rundeck.features.config

import org.rundeck.features.models.FeatureSetConfig

interface FeatureSetConfigProvider {
    FeatureSetConfig getFeatureSetConfig()

}