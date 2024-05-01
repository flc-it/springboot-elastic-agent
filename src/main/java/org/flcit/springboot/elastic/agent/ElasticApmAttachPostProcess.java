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

package org.flcit.springboot.elastic.agent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Profiles;

import co.elastic.apm.attach.ElasticApmAttacher;
import org.flcit.springboot.elastic.agent.configuration.ElasticApmConfiguration;
import org.flcit.springboot.elastic.agent.configuration.ElasticApmGetConfiguration;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class ElasticApmAttachPostProcess implements EnvironmentPostProcessor {

    private static boolean active = false;

    /**
     *
     */
    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        if (!isActive(environment)) {
            return;
        }
        final ElasticApmConfiguration config = ElasticApmGetConfiguration.override(ElasticApmGetConfiguration.read(environment, application.getMainApplicationClass()), ElasticApmGetConfiguration.readFromFile());
        if (config.isActive()) {
            setActive(true);
            ElasticApmAttacher.attach(config);
        }
    }

    private static final boolean isActive(ConfigurableEnvironment environment) {
        return environment.acceptsProfiles(Profiles.of("!doc"));
    }

    /**
     * @return
     */
    public static boolean isActive() {
        return active;
    }

    private static void setActive(boolean active) {
        ElasticApmAttachPostProcess.active = active;
    }

}
