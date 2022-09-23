package org.rundeck.features.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.rundeck.features.models.FeatureSetConfig
import org.springframework.beans.factory.annotation.Autowired
import rundeck.services.ConfigurationService

class ConfigurationServiceFeatureSetConfigProvider implements FeatureSetConfigProvider {

    @Autowired
    ConfigurationService cfgService

    @Autowired
    ObjectMapper objectMapper


    @Override
    FeatureSetConfig getFeatureSetConfig() {
        def featureSetMap = cfgService.getConfig("featureSet")
        return featureSetMap ? objectMapper.convertValue(featureSetMap, FeatureSetConfig) : new FeatureSetConfig()
    }
}
