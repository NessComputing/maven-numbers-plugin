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
package com.paywholesail.mojo.numbers.beans;

import java.io.File;

import org.apache.commons.lang3.StringUtils;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

public class NumberDefinition extends AbstractDefinition<NumberDefinition>
{
    public static final String INITIAL_VALUE = "0";

    /** If a multi-number, which field to increment. */
    private int fieldNumber = 0;

    /** Increment of the property when changing it. */
    private int increment = 1;

    @VisibleForTesting
    NumberDefinition(final String id,
                     final boolean skip,
                     final boolean export,
                     final String initialValue,
                     final int fieldNumber,
                     final int increment,
                     final String propertyName,
                     final File propertyFile,
                     final IWFCEnum onMissingFile,
                     final IWFCEnum onMissingProperty,
                     final String format)
    {
        super(id, skip, export,
              MoreObjects.firstNonNull(initialValue, INITIAL_VALUE),
              propertyName, propertyFile, onMissingFile, onMissingProperty, format);

        this.fieldNumber = fieldNumber;
        this.increment = increment;
    }

    public NumberDefinition()
    {
        super();
        setInitialValue(INITIAL_VALUE);
    }

    public int getFieldNumber()
    {
        return fieldNumber;
    }

    public NumberDefinition setFieldNumber(final int fieldNumber)
    {
        this.fieldNumber = fieldNumber;
        return this;
    }

    public int getIncrement()
    {
        return increment;
    }

    public NumberDefinition setIncrement(final int increment)
    {
        this.increment = increment;
        return this;
    }

    @Override
    public void check()
    {
        super.check();

        Preconditions.checkState(StringUtils.isNotBlank(getInitialValue()), "the initial value must not be empty");
        Preconditions.checkState(fieldNumber >= 0, "the field number must be >= 0");
    }
}
