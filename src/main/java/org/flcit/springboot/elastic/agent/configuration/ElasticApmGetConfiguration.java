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

package org.flcit.springboot.elastic.agent.configuration;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Map;
import java.util.logging.Logger;

import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import org.flcit.commons.core.util.PropertyUtils;
import org.flcit.commons.core.util.SystemPropertyUtils;
import org.flcit.springboot.elastic.agent.domain.ElasticApmCaptureBody;
import org.flcit.springboot.elastic.agent.domain.ElasticApmLogLevel;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class ElasticApmGetConfiguration {

    private static final Logger LOG = Logger.getLogger(ElasticApmGetConfiguration.class.getName());
    private static final String PROPERTIES_PREFIX = "elastic.apm.";

    private ElasticApmGetConfiguration() { }

    /**
     * @param environment
     * @return
     */
    public static final ElasticApmConfiguration readFromFile(ConfigurableEnvironment environment) {
        return readFromFile(getProperty(environment, "file-properties"));
    }

    /**
     * @return
     */
    public static final ElasticApmConfiguration readFromFile() {
        return readFromFile(getProperty("file_properties"));
    }

    @SuppressWarnings("java:S1168")
    private static final ElasticApmConfiguration readFromFile(String fileProperties) {
        if (!StringUtils.hasLength(fileProperties)) {
            return null;
        }
        final ElasticApmConfiguration config = new ElasticApmConfiguration();
        try {
            final Map<String, String> values = PropertyUtils.load(Paths.get(fileProperties));
            if (!CollectionUtils.isEmpty(values)) {
                config.putAll(values);
            }
        } catch (IOException e) {
            LOG.log(null, "The properties file cannot be open !", e);
        }
        return config;
    }

    /**
     * @param environment
     * @param mainApplicationClass
     * @return
     */
    public static final ElasticApmConfiguration read(ConfigurableEnvironment environment, Class<?> mainApplicationClass) {
        final ElasticApmConfiguration config = new ElasticApmConfiguration();
        config.setActive(getProperty(environment, "active", Boolean.class));
        config.setServerUrl(getProperty(environment, "server-url"));
        config.setSecretToken(getProperty(environment, "secret-token"));
        config.setApiKey(getProperty(environment, "api-key"));
        config.setHostname(getProperty(environment, "hostname"));
        config.setServiceName(getProperty(environment, "service-name"));
        config.setEnvironment(getProperty(environment, "environment"));
        config.setApplicationPackages(getProperty(environment, "application-packages", mainApplicationClass.getPackage().getName()));
        config.setCaptureBody(getProperty(environment, "capture-body", ElasticApmCaptureBody.class));
        config.setLogLevel(getProperty(environment, "log-level", ElasticApmLogLevel.class));
        return config;
    }

    /**
     * @param config
     * @param configsOverride
     * @return
     */
    @SuppressWarnings("java:S1168")
    public static final ElasticApmConfiguration override(ElasticApmConfiguration config, ElasticApmConfiguration... configsOverride) {
        if (config == null
                && ObjectUtils.isEmpty(configsOverride)) {
            return null;
        }
        int i = 0;
        if (config == null) {
            config = configsOverride[0];
            i = 1;
        }
        for (;i < configsOverride.length; i++) {
            config = config.override(configsOverride[i]);
        }
        return config;
    }

    /**
     * @param mainApplicationClass
     * @return
     */
    public static final ElasticApmConfiguration readFromSystemProperties(Class<?> mainApplicationClass) {
        final ElasticApmConfiguration config = new ElasticApmConfiguration();
        config.setActive(getProperty("active"));
        config.setServerUrl(getProperty("server_url"));
        config.setSecretToken(getProperty("secret_token"));
        config.setApiKey(getProperty("api_key"));
        config.setHostname(getProperty("hostname"));
        config.setServiceName(getProperty("service_name"));
        config.setEnvironment(getProperty("environment"));
        config.setApplicationPackages(getProperty("application_packages", mainApplicationClass.getPackage().getName()));
        config.setCaptureBody(getProperty("capture_body", ElasticApmCaptureBody.class));
        config.setLogLevel(getProperty("log_level", ElasticApmLogLevel.class));
        return config;
    }

    private static final <T> T getProperty(ConfigurableEnvironment environment, String name, Class<T> targetType) {
        return environment.getProperty(PROPERTIES_PREFIX + name, targetType);
    }

    private static final String getProperty(ConfigurableEnvironment environment, String name) {
        return environment.getProperty(PROPERTIES_PREFIX + name);
    }

    private static final String getProperty(ConfigurableEnvironment environment, String name, String defaultValue) {
        return environment.getProperty(PROPERTIES_PREFIX + name, defaultValue);
    }

    private static final <T extends Enum<T>> T getProperty(String name, Class<T> targetType) {
        return SystemPropertyUtils.getEnum(PROPERTIES_PREFIX + name, targetType);
    }

    private static final String getProperty(String name) {
        return SystemPropertyUtils.get(PROPERTIES_PREFIX + name);
    }

    private static final String getProperty(String name, String defaultValue) {
        return SystemPropertyUtils.get(PROPERTIES_PREFIX + name, defaultValue);
    }

}
