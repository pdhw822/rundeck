package org.rundeck.features.registry

import com.fasterxml.jackson.databind.ObjectMapper
import com.rundeck.feature.api.Feature
import com.rundeck.feature.api.action.FeatureAction
import com.rundeck.feature.api.exception.FeatureActionNotFoundException
import com.rundeck.feature.api.exception.FeatureNotEnabledException
import com.rundeck.feature.api.exception.FeatureNotFoundException
import com.rundeck.feature.api.registry.FeatureRegistry
import org.rundeck.features.config.FeatureSetConfigProvider
import org.rundeck.features.models.FeatureConfig
import org.rundeck.features.models.FeatureSetConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationContext

import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

class SpringFeatureRegistry implements FeatureRegistry {

    @Autowired
    ApplicationContext appContext;

    @Autowired
    FeatureSetConfigProvider featureSetConfigProvider

    @Autowired
    ObjectMapper mapper;

    Map<String, Feature<?>> features = new HashMap<>();
    FeatureSetConfig featureSetConfig = new FeatureSetConfig();

    @PostConstruct
    void loadFeatures() {
        loadFeatureSetConfig();
        println "loading features"
        appContext.getBeansOfType(Feature.class).forEach((s, f) -> {
            println "feature: " + f.getName()
            if(featureSetConfig.features.containsKey(f.getName())) {
                FeatureConfig cfg = featureSetConfig.features.get(f.getName());
                configureFeature(f, cfg);
                if(cfg.enabled) f.enable();
            }

            features.put(f.getName(),f);
        });
    }

    private void configureFeature(Feature f, FeatureConfig featureConfig) {
        if(featureConfig == null) return;
        try {
            f.configure(mapper.convertValue(featureConfig.config, f.getConfigClass()));
        } catch (Exception ex) {
            System.out.println("Failed to configure feature: "+ f.getName());
            ex.printStackTrace();
        }
    }

    private void loadFeatureSetConfig() {
        try {
            featureSetConfig = featureSetConfigProvider.getFeatureSetConfig()
        } catch (Exception ex) {
            System.out.println("Failed to load feature set config");
            ex.printStackTrace();
        }
    }

    @Override
    public Collection<Feature<?>> listFeatures() {
        return features.values();
    }

    @Override
    public Feature<?> getFeature(String feature) {
        if(!features.containsKey(feature)) throw new FeatureNotFoundException(feature);
        return features.get(feature);
    }

    @Override
    public FeatureAction<?> getFeatureAction(String featureName, String actionName) {
        var feature = getFeature(featureName);
        if(!feature.isEnabled()) throw new FeatureNotEnabledException(featureName);
        return feature.getActionByName(actionName).orElseThrow(() -> new FeatureActionNotFoundException(featureName,actionName));
    }

    @Override
    void toggleFeatureEnabled(String feature, boolean enable) {
        var f = getFeature(feature);
        if(f.isEnabled() == enable) return;
        if(enable) f.enable();
        else f.disable();
    }

    @PreDestroy
    void cleanupFeatures() {
        features.values().forEach(f -> f.cleanup());
    }
}
