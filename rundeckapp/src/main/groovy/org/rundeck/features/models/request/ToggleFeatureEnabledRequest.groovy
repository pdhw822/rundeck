package org.rundeck.features.models.request

import grails.validation.Validateable

class ToggleFeatureEnabledRequest implements Validateable {
    boolean enabled;
}
