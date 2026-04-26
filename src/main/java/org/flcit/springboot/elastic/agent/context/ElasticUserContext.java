/*
 * Copyright 2002-2023 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.flcit.springboot.elastic.agent.context;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class ElasticUserContext {

    private String id;
    private String email;
    private String username;
    private String domain;

    public String getId() {
        return id;
    }
    public ElasticUserContext setId(String id) {
        this.id = id;
        return this;
    }
    public String getEmail() {
        return email;
    }
    public ElasticUserContext setEmail(String email) {
        this.email = email;
        return this;
    }
    public String getUsername() {
        return username;
    }
    public ElasticUserContext setUsername(String username) {
        this.username = username;
        return this;
    }
    public String getDomain() {
        return domain;
    }
    public ElasticUserContext setDomain(String domain) {
        this.domain = domain;
        return this;
    }

}
