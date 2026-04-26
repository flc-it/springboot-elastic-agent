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

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @since 
 * @author Florian Lestic
 */
public class MappedValues {

    private Map<String, Number> numbers;
    private Map<String, String> strings;
    private Map<String, Boolean> booleans;

    public Map<String, Number> getNumbers() {
        return numbers;
    }

    public Map<String, String> getStrings() {
        return strings;
    }

    public Map<String, Boolean> getBooleans() {
        return booleans;
    }

    public void add(String key, Object value) {
        if (value instanceof Number number) {
            this.numbers = add(this.numbers, key, number);
        } else if (value instanceof String string) {
            this.strings = add(this.strings, key, string);
        } else if (value instanceof Boolean bool) {
            this.booleans = add(this.booleans, key, bool);
        }
        throw new IllegalStateException("The value must be a Number, String or boolean");
    }

    private static final <T> Map<String, T> add(Map<String, T> map, String key, T value) {
        if (map == null) {
            map = new HashMap<>(1);
        }
        map.put(key, value);
        return map;
    }

}
