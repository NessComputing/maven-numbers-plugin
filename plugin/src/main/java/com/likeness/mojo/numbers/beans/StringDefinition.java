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
import java.util.List;

import com.google.common.annotations.VisibleForTesting;

public class StringDefinition extends AbstractDefinition<StringDefinition>
{
    /**
     * Values for this string.
     */
    private List<String> values;

    /**
     * Whether a blank string is a valid value.
     */
    private boolean blankIsValid = true;

    /**
     * Default action on missing value.
     */
    private IWFEnum onMissingValue = IWFEnum.FAIL;

    @VisibleForTesting
    StringDefinition(
            final String id,
            final boolean skip,
            final boolean export,
            final String initialValue,
            final List<String> values,
            final boolean blankIsValid,
            final IWFEnum onMissingValue,
            final String propertyName,
            final File propertyFile,
            final IWFCEnum onMissingFile,
            final IWFCEnum onMissingProperty,
            final String format)
    {
        super(id, skip, export, initialValue, propertyName, propertyFile, onMissingFile, onMissingProperty, format);

        this.values = values;
        this.blankIsValid = blankIsValid;
        this.onMissingValue = onMissingValue;
    }

    public StringDefinition()
    {
        super();
    }

    public List<String> getValues()
    {
        return values;
    }

    public StringDefinition setValues(final List<String> values)
    {
        this.values = values;
        return this;
    }

    public boolean isBlankIsValid()
    {
        return blankIsValid;
    }

    public StringDefinition setBlankIsValid(final boolean blankIsValid)
    {
        this.blankIsValid = blankIsValid;
        return this;
    }

    public IWFEnum getOnMissingValue()
    {
        return onMissingValue;
    }

    public StringDefinition setOnMissingValue(final String onMissingValue)
    {
        this.onMissingValue = IWFEnum.forString(onMissingValue);
        return this;
    }
}
