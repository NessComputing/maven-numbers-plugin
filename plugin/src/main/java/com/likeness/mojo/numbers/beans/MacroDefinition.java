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
package com.likeness.mojo.numbers.beans;

import java.io.File;
import java.util.Properties;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.google.common.annotations.VisibleForTesting;

public class MacroDefinition extends AbstractDefinition<MacroDefinition>
{
    /**
     * Macro type.
     */
    private String macroType = null;

    /**
     * Class for this macro.
     */
    private String macroClass = null;

    /**
     * Macro specific properties.
     */
    private Properties properties = null;

    @VisibleForTesting
    MacroDefinition(
        final String id,
        final boolean skip,
        final boolean export,
        final String initialValue,
        final String macroType,
        final String macroClass,
        final Properties properties,
        final String propertyName,
        final File propertyFile,
        final IWFCEnum onMissingFile,
        final IWFCEnum onMissingProperty,
        final String format)
    {
        super(id, skip, export, initialValue, propertyName, propertyFile, onMissingFile, onMissingProperty, format);

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

    public MacroDefinition setMacroType(final String macroType)
    {
        this.macroType = macroType;
        return this;
    }

    public String getMacroClass()
    {
        return macroClass;
    }

    public MacroDefinition setMacroClass(final String macroClass)
    {
        this.macroClass = macroClass;
        return this;
    }

    public Properties getProperties()
    {
        return properties;
    }

    public MacroDefinition setProperties(final Properties properties)
    {
        this.properties = properties;
        return this;
    }

    @Override
    public boolean equals(final Object other)
    {
        if (!(other instanceof MacroDefinition)) {
            return false;
        }
        MacroDefinition castOther = (MacroDefinition) other;
        return new EqualsBuilder()
            .append(macroType, castOther.macroType)
            .append(macroClass, castOther.macroClass)
            .append(properties, castOther.properties)
            .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(macroType).append(macroClass).append(properties).toHashCode();
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this)
            .appendSuper(super.toString())
            .append("macroType", macroType)
            .append("macroClass", macroClass)
            .append("properties", properties)
            .toString();
    }
}
