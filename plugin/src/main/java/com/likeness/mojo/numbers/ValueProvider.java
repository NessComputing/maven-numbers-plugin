/**
 * Copyright (C) 2011 Ness Computing, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.likeness.mojo.numbers;

import java.util.Properties;

public interface ValueProvider
{
    String getValue();

    void setValue(String value);

    ValueProvider NULL_PROVIDER = new NullProvider();

    static class NullProvider implements ValueProvider
    {
        private NullProvider()
        {
        }

        @Override
        public void setValue(final String value)
        {
        }

        @Override
        public String getValue()
        {
            return null;
        }
    }

    static class PropertyProvider implements ValueProvider
    {
        private final Properties props;
        private final String propertyName;

        PropertyProvider(final Properties props, final String propertyName)
        {
            this.props = props;
            this.propertyName = propertyName;
        }

        @Override
        public void setValue(final String value)
        {
            props.setProperty(propertyName, value);
        }

        @Override
        public String getValue()
        {
            return props.getProperty(propertyName);
        }
    }
}

