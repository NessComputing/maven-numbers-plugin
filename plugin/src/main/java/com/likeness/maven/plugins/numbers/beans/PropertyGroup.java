package com.likeness.maven.plugins.numbers.beans;

import java.io.StringWriter;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import org.stringtemplate.v4.NoIndentWriter;
import org.stringtemplate.v4.ST;
import org.stringtemplate.v4.STErrorListener;
import org.stringtemplate.v4.STGroup;
import org.stringtemplate.v4.misc.ErrorType;
import org.stringtemplate.v4.misc.STMessage;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Functions;
import com.google.common.base.Objects;
import com.google.common.collect.Iterators;
import com.likeness.maven.plugins.numbers.util.Log;

public class PropertyGroup
{
    private static final Log LOG = Log.findLog();

    /** Property group id. */
    private String id;

    /** Activate the group if the current project version does not contain SNAPSHOT- */
    private boolean activeOnRelease = true;

    /** Activate the group if the current project version contains SNAPSHOT- */
    private boolean activeOnSnapshot = true;

    /** Action if this property group defines a duplicate property. */
    private IWFEnum onDuplicateProperty = IWFEnum.FAIL;

    /** Action if any property from that group could not be defined. */
    private IWFEnum onMissingProperty = IWFEnum.FAIL;

    /** Property definitions in this group. */
    private Properties properties = null;

    private final STGroup stGroup;

    @VisibleForTesting
    PropertyGroup(final String id,
                  final boolean activeOnRelease,
                  final boolean activeOnSnapshot,
                  final IWFEnum onDuplicateProperty,
                  final IWFEnum onMissingProperty,
                  final Properties properties)
    {
        this();

        this.id = id;
        this.activeOnRelease = activeOnRelease;
        this.activeOnSnapshot = activeOnSnapshot;
        this.onDuplicateProperty = onDuplicateProperty;
        this.onMissingProperty = onMissingProperty;
        this.properties = properties;

    }

    public PropertyGroup()
    {
        stGroup = new STGroup('{', '}');
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public boolean isActiveOnRelease()
    {
        return activeOnRelease;
    }

    public void setActiveOnRelease(final boolean activeOnRelease)
    {
        this.activeOnRelease = activeOnRelease;
    }

    public boolean isActiveOnSnapshot()
    {
        return activeOnSnapshot;
    }

    public void setActiveOnSnapshot(final boolean activeOnSnapshot)
    {
        this.activeOnSnapshot = activeOnSnapshot;
    }

    public IWFEnum getOnDuplicateProperty()
    {
        return onDuplicateProperty;
    }

    public void setOnDuplicateProperty(final String onDuplicateProperty)
    {
        this.onDuplicateProperty = IWFEnum.forString(onDuplicateProperty);
    }

    public IWFEnum getOnMissingProperty()
    {
        return onMissingProperty;
    }

    public void setOnMissingProperty(final String onMissingProperty)
    {
        this.onMissingProperty = IWFEnum.forString(onMissingProperty);
    }

    public Properties getProperties()
    {
        return properties;
    }

    public void setProperties(final Properties properties)
    {
        this.properties = properties;
    }

    public void check()
    {
    }

    public Iterator<String> getPropertyNames()
    {
        return Iterators.transform(properties.keySet().iterator(), Functions.toStringFunction());
    }

    public String getPropertyValue(final String propertyName, final Map<String, String> propElements)
    {
        final String propertyValue = Objects.firstNonNull(properties.getProperty(propertyName), "");
        final ST st = new ST(stGroup, propertyValue);

        for (Map.Entry<String, String> entry : propElements.entrySet()) {
            st.add(entry.getKey(), entry.getValue());
        }

        final PropertyGroupErrorListener errorListener = new PropertyGroupErrorListener();
        final StringWriter writer = new StringWriter();
        st.write(new NoIndentWriter(writer), Locale.ENGLISH, errorListener);
        errorListener.throwException();
        return writer.toString();
    }

    public class PropertyGroupErrorListener implements STErrorListener
    {
        private IllegalStateException ise = null;

        public void throwException()
        {
            if (ise != null) {
                try {
                    throw ise;
                }
                finally {
                    this.ise = null;
                }
            }
        }

        @Override
        public void compileTimeError(STMessage msg)
        {
            LOG.error("%s", msg);
        }

        @Override
        public void runTimeError(STMessage msg)
        {
            if (msg.error == ErrorType.NO_SUCH_ATTRIBUTE) {
                try {
                    IWFEnum.checkState(onMissingProperty, false, String.valueOf(msg.arg));
                }
                catch (IllegalStateException ise) {
                    if (this.ise == null) {
                        this.ise = ise;
                    }
                }
            }
            else {
                LOG.error("%s", msg);
            }
        }

        @Override
        public void IOError(STMessage msg)
        {
            LOG.error("%s", msg);
        }

        @Override
        public void internalError(STMessage msg)
        {
            LOG.error("%s", msg);
        }
    }
}
