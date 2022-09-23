package org.rundeck.features.menu

import com.dtolabs.rundeck.core.authorization.UserAndRolesAuthContext
import grails.web.mapping.LinkGenerator
import org.rundeck.app.gui.AuthMenuItem
import org.springframework.beans.factory.annotation.Autowired

class FeatureMenuItem implements AuthMenuItem {
    @Autowired
    LinkGenerator grailsLinkGenerator

    @Override
    MenuType getType() {
        return MenuType.SYSTEM_CONFIG
    }

    @Override
    String getTitleCode() {
        return "feature.menu.title"
    }

    @Override
    String getTitle() {
        return "Features"
    }

    @Override
    boolean isEnabled(UserAndRolesAuthContext auth) {
        return true
    }

    @Override
    String getHref() {
        return grailsLinkGenerator.link(
                action: "ui",
                controller: "feature"
        )
    }
}
