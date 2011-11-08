package com.likeness.maven.plugins.numbers;

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

