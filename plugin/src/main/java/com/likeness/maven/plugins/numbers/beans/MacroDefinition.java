package com.likeness.maven.plugins.numbers.beans;

import java.util.Properties;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.annotations.VisibleForTesting;

public class MacroDefinition extends AbstractDefinition
{
    /** Macro type. */
    private String macroType = null;

    /** Class for this macro. */
    private String macroClass = null;

    /** Macro specific properties. */
    private Properties properties = null;

    @VisibleForTesting
    MacroDefinition(final String id,
                    final boolean skip,
                    final String macroType,
                    final String macroClass,
                    final Properties properties)
    {
        super(id, skip);

        this.macroType = macroType;
        this.macroClass = macroClass;
        this.properties = properties;
    }

    public MacroDefinition()
    {
        super();
    }

    public String getMacroType()
    {
        return macroType;
    }

    public void setMacroType(String macroType)
    {
        this.macroType = macroType;
    }

    public String getMacroClass()
    {
        return macroClass;
    }

    public void setMacroClass(String macroClass)
    {
        this.macroClass = macroClass;
    }

    public Properties getProperties()
    {
        return properties;
    }

    public void setProperties(Properties properties)
    {
        this.properties = properties;
    }

    @Override
    public boolean equals(final Object other)
    {
        if (!(other instanceof MacroDefinition))
            return false;
        MacroDefinition castOther = (MacroDefinition) other;
        return new EqualsBuilder().append(macroType, castOther.macroType).append(macroClass, castOther.macroClass).append(properties, castOther.properties).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(macroType).append(macroClass).append(properties).toHashCode();
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("macroType", macroType).append("macroClass", macroClass).append("properties", properties).toString();
    }


}
