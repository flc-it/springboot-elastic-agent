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

import java.util.HashMap;

import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import org.flcit.commons.core.util.EnumUtils;
import org.flcit.springboot.elastic.agent.domain.ElasticApmCaptureBody;
import org.flcit.springboot.elastic.agent.domain.ElasticApmLogLevel;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public final class ElasticApmConfiguration extends HashMap<String, String> {

    private static final String APPLICATION_PACKAGES = "application_packages";
    private static final String ACTIVE = "active";
    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public ElasticApmConfiguration() {
        super(6);
    }

    @Override
    public String put(String key, String value) {
        if (!StringUtils.hasLength(value)) {
            return super.remove(key);
        } else {
            return super.put(key, value);
        }
    }

    /**
     * @return
     */
    public String getServerUrl() {
        return get("server_url");
    }

    /**
     * @param serverUrl
     */
    public void setServerUrl(String serverUrl) {
        put("server_url", serverUrl);
    }

    /**
     * @return
     */
    public String getSecretToken() {
        return get("secret_token");
    }

    /**
     * @param secretToken
     */
    public void setSecretToken(String secretToken) {
        put("secret_token", secretToken);
    }

    /**
     * @return
     */
    public String getApiKey() {
        return get("api_key");
    }

    /**
     * @param apiKey
     */
    public void setApiKey(String apiKey) {
        put("api_key", apiKey);
    }

    /**
     * @return
     */
    public String getHostname() {
        return get("hostname");
    }

    /**
     * @param hostname
     */
    public void setHostname(String hostname) {
        put("hostname", hostname);
    }

    /**
     * @return
     */
    public String getEnvironment() {
        return get("environment");
    }

    /**
     * @param environment
     */
    public void setEnvironment(String environment) {
        put("environment", environment);
    }

    /**
     * @return
     */
    public String getApplicationPackages() {
        return get(APPLICATION_PACKAGES);
    }

    /**
     * @param applicationPackages
     */
    public void setApplicationPackages(String[] applicationPackages) {
        if (ObjectUtils.isEmpty(applicationPackages)) {
            remove(APPLICATION_PACKAGES);
        } else {
            setApplicationPackages(String.join(",", applicationPackages));
        }
    }

    /**
     * @param applicationPackages
     */
    public void setApplicationPackages(String applicationPackages) {
        put(APPLICATION_PACKAGES, applicationPackages);
    }

    /**
     * @return
     */
    public String getServiceName() {
        return get("service_name");
    }

    /**
     * @param serviceName
     */
    public void setServiceName(String serviceName) {
        put("service_name", serviceName);
    }

    /**
     * @return
     */
    public ElasticApmCaptureBody getCaptureBody() {
        return ElasticApmCaptureBody.valueOf(getOrDefault("capture_body", EnumUtils.toString(ElasticApmCaptureBody.OFF)).toUpperCase());
    }

    /**
     * @param captureBody
     */
    public void setCaptureBody(ElasticApmCaptureBody captureBody) {
        setCaptureBody(EnumUtils.toString(captureBody));
    }

    private void setCaptureBody(String captureBody) {
        put("capture_body", captureBody);
    }

    /**
     * @return
     */
    public ElasticApmLogLevel getLogLevel() {
        final String logLevel = get("log_level");
        return !StringUtils.hasLength(logLevel) ? null : ElasticApmLogLevel.valueOf(logLevel.toUpperCase());
    }

    /**
     * @param logLevel
     */
    public void setLogLevel(ElasticApmLogLevel logLevel) {
        setLogLevel(EnumUtils.toString(logLevel));
    }

    private void setLogLevel(String logLevel) {
        put("log_level", logLevel);
    }

    /**
     * @param active
     */
    public void setActive(Boolean active) {
        put(ACTIVE, active == null ? null : String.valueOf(active));
    }

    /**
     * @param active
     */
    public void setActive(String active) {
        put(ACTIVE, active);
    }

    /**
     * @return
     */
    public boolean isActive() {
        return Boolean.valueOf(getOrDefault(ACTIVE, "true"))
                && StringUtils.hasLength(getServerUrl());
    }

    /**
     * @param config
     * @return
     */
    public ElasticApmConfiguration override(ElasticApmConfiguration config) {
        if (config == null) {
            return this;
        }
        if (StringUtils.hasLength(config.getServerUrl())) {
            this.setServerUrl(config.getServerUrl());
        }
        if (StringUtils.hasLength(config.getSecretToken())) {
            this.setSecretToken(config.getSecretToken());
        }
        if (StringUtils.hasLength(config.getApiKey())) {
            this.setApiKey(config.getApiKey());
        }
        if (StringUtils.hasLength(config.getApplicationPackages())) {
            this.setApplicationPackages(config.getApplicationPackages());
        }
        if (StringUtils.hasLength(config.getEnvironment())) {
            this.setEnvironment(config.getEnvironment());
        }
        if (StringUtils.hasLength(config.getHostname())) {
            this.setHostname(config.getHostname());
        }
        if (StringUtils.hasLength(config.getServiceName())) {
            this.setServiceName(config.getServiceName());
        }
        if (config.getCaptureBody() != null) {
            this.setCaptureBody(config.getCaptureBody());
        }
        if (config.getLogLevel() != null) {
            this.setLogLevel(config.getLogLevel());
        }
        return this;
    }

}
