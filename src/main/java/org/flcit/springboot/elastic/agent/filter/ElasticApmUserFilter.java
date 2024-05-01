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

package org.flcit.springboot.elastic.agent.filter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import co.elastic.apm.api.ElasticApm;
import org.flcit.commons.core.util.ObjectUtils;
import org.flcit.commons.core.util.ReflectionUtils;
import org.flcit.springboot.elastic.agent.ElasticApmAttachPostProcess;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public class ElasticApmUserFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (ElasticApmAttachPostProcess.isActive()) {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null
                    && authentication.isAuthenticated()) {
                ElasticApm.currentTransaction().setUser(
                        ObjectUtils.getOrDefault(getValue(authentication, "id"), authentication.getName()),
                        getValue(authentication, "email"),
                        ObjectUtils.getOrDefault(getValue(authentication, "username"), authentication.getName()));
            }
        }
        filterChain.doFilter(request, response);
    }

    private static final String getValue(final Authentication authentication, String field) {
        String value = getValue(authentication.getPrincipal(), field);
        if (value == null) {
            value = getValue(authentication.getDetails(), field);
        }
        return value;
    }

    private static final String getValue(final Object object, String field) {
        if (object == null) {
            return null;
        }
        return ReflectionUtils.getFieldValue(object, field, String.class);
    }

}
