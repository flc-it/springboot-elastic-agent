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
import java.util.function.BiConsumer;

import org.flcit.springboot.elastic.agent.ElasticApmAttachPostProcess;
import org.flcit.springboot.elastic.agent.context.ElasticSetContext;
import org.flcit.springboot.elastic.agent.context.ElasticUserContext;
import org.flcit.springboot.elastic.agent.context.MappedValues;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import co.elastic.apm.api.ElasticApm;
import co.elastic.apm.api.Transaction;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public class ElasticApmUserFilter extends OncePerRequestFilter {

    private final ElasticSetContext setContext;

    public ElasticApmUserFilter(ElasticSetContext elasticSetContext) {
        this.setContext = elasticSetContext;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        if (ElasticApmAttachPostProcess.isAttached()) {
            final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
            final Transaction transaction = ElasticApm.currentTransaction();
            if (authentication != null && authentication.isAuthenticated()) {
                setUser(transaction, setContext.getUserContext(authentication));
            }
            setValues(setContext.getCustoms(authentication, requestAttributes), transaction::addCustomContext, transaction::addCustomContext, transaction::addCustomContext);
            setValues(setContext.getLabels(authentication, requestAttributes), transaction::setLabel, transaction::setLabel, transaction::setLabel);
        }
        filterChain.doFilter(request, response);
    }

    private static final void setUser(final Transaction transaction, final ElasticUserContext userContext) {
        if (userContext == null) {
            return;
        }
        transaction.setUser(
                userContext.getId(),
                userContext.getEmail(),
                userContext.getUsername(),
                userContext.getDomain());
    }

    private static final void setValues(MappedValues mappedValues, BiConsumer<String, Number> number, BiConsumer<String, String> string, BiConsumer<String, Boolean> bool) {
        if (mappedValues == null) {
            return;
        }
        if (mappedValues.getNumbers() != null) {
            mappedValues.getNumbers().forEach(number);
        }
        if (mappedValues.getStrings() != null) {
            mappedValues.getStrings().forEach(string);
        }
        if (mappedValues.getBooleans() != null) {
            mappedValues.getBooleans().forEach(bool);
        }
    }

}
