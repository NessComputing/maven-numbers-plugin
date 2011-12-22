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

import com.google.common.annotations.VisibleForTesting;

public class DateDefinition extends AbstractDefinition<DateDefinition>
{
    /** Timezone for this date. */
    private String timezone = null;

    /** Value for this date. */
    private Long value = null;

    @VisibleForTesting
    DateDefinition(final String id,
                   final boolean skip,
                   final boolean export,
                   final String initialValue,
                   final String timezone,
                   final Long value,
                   final String propertyName,
                   final File propertyFile,
                   final IWFCEnum onMissingFile,
                   final IWFCEnum onMissingProperty,
                   final String format)
    {
        super(id, skip, export, initialValue, propertyName, propertyFile, onMissingFile, onMissingProperty, format);

        this.timezone = timezone;
        this.value = value;
    }

    public DateDefinition()
    {
        super();
    }

    public String getTimezone()
    {
        return timezone;
    }

    public DateDefinition setTimezone(final String timezone)
    {
        this.timezone = timezone;
        return this;
    }

    public Long getValue()
    {
        return value;
    }

    public DateDefinition setValue(final Long value)
    {
        this.value = value;
        return this;
    }
}
