/*
 * Copyright 2018 Rundeck, Inc. (http://rundeck.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dtolabs.rundeck.app.api.tokens

import com.dtolabs.rundeck.app.api.FormattedDate
import com.dtolabs.rundeck.app.api.marshall.ApiResource
import com.dtolabs.rundeck.app.api.marshall.ApiVersion
import com.dtolabs.rundeck.app.api.marshall.CollectionElement
import com.dtolabs.rundeck.app.api.marshall.Ignore
import com.dtolabs.rundeck.app.api.marshall.XmlAttribute
import com.dtolabs.rundeck.core.authentication.tokens.AuthTokenMode
import io.swagger.v3.oas.annotations.media.Schema
import rundeck.AuthToken

/**
 * @author greg
 * @since 3/23/17
 */
@ApiResource
@Schema
class Token {

    @ApiVersion(37)
    @Ignore(onlyIfNull = true)
    @XmlAttribute
    @Schema(description = "since: v37")
    String name;

    @XmlAttribute
    @Schema(description = "unique ID")
    String id;

    @ApiVersion(19)
    @Ignore(onlyIfNull = true)
    @XmlAttribute
    @Schema(description = "Token value (only available at creation time). since: v19")
    String token;

    @ApiVersion(19)
    @XmlAttribute
    @Schema(description = "Token creator. since: v19")
    String creator;

    @XmlAttribute
    @Schema(description = "Token effective username")
    String user;

    @ApiVersion(19)
    @CollectionElement('role')
    @Schema(description = "since: v19")
    Set<String> roles;

    @Ignore(onlyIfNull = true)
    @ApiVersion(19)
    @Schema(description = "since: v19", implementation = String, format = 'iso')
    Date expiration

    @ApiVersion(19)
    @Schema(description = "since: v19")
    Boolean expired;

    Token(AuthToken authToken, boolean masked = true, boolean legacyApiMode=false) {
        this.name = authToken.name
        this.id = authToken.uuid ?: authToken.id
        this.token = masked ? null :
                     (authToken.tokenMode == null || authToken.tokenMode == AuthTokenMode.LEGACY) ? authToken.token :
                     authToken.clearToken
        if(legacyApiMode){
            this.id = this.token
        }
        this.creator = authToken.creator
        this.user = authToken.user.login
        this.roles = authToken.authRolesSet()
        this.expiration = authToken.expiration
        this.expired = authToken.tokenIsExpired()
    }
}
