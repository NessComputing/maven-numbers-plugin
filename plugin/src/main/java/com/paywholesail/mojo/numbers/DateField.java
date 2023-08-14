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
package com.paywholesail.mojo.numbers;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import com.google.common.collect.Lists;
import com.paywholesail.mojo.numbers.beans.DateDefinition;

public class DateField implements PropertyElement
{
    private final DateDefinition dateDefinition;
    private final ValueProvider valueProvider;

    public static List<DateField> createDates(final PropertyCache propertyCache, final DateDefinition[] dateDefinitions)
        throws IOException
    {
        final List<DateField> result = Lists.newArrayList();

        if (!ArrayUtils.isEmpty(dateDefinitions)) {
            for (final DateDefinition dateDefinition : dateDefinitions) {
                dateDefinition.check();
                final ValueProvider dateValue = propertyCache.getPropertyValue(dateDefinition);
                final DateField dateField = new DateField(dateDefinition, dateValue);
                result.add(dateField);
            }
        }
        return result;
    }

    public DateField(final DateDefinition dateDefinition, final ValueProvider valueProvider)
    {
        this.dateDefinition = dateDefinition;
        this.valueProvider = valueProvider;
    }

    @Override
    public String getPropertyName()
    {
        return dateDefinition.getId();
    }

    @Override
    public String getPropertyValue()
    {
        final ZoneId timeZone = dateDefinition.getTimezone() == null ? ZoneId.systemDefault() : ZoneId.of(dateDefinition.getTimezone());

        ZonedDateTime date = getDateTime(valueProvider.getValue(), timeZone);

        if (date == null && dateDefinition.getValue() != null) {
            date = ZonedDateTime.ofInstant(Instant.ofEpochMilli(dateDefinition.getValue()), timeZone);
        }

        if (date == null) {
            date = ZonedDateTime.now(timeZone);
        }

        final String format = dateDefinition.getFormat();

        if (format == null) {
            return date.toString();
        }
        else {
            final DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format);
            return formatter.format(date);
        }
    }

    private ZonedDateTime getDateTime(final String value, final ZoneId timeZone)
    {
        if (value == null) {
            return null;
        }
        try {
            return ZonedDateTime.ofInstant(Instant.ofEpochMilli(Long.parseLong(value)), timeZone);
        }
        catch (final NumberFormatException nfe) {
            return ZonedDateTime.ofInstant(DateTimeFormatter.ISO_DATE_TIME.parse(value, Instant::from), timeZone);
        }
    }

    @Override
    public boolean isExport()
    {
        return dateDefinition.isExport();
    }

    @Override
    public String toString()
    {
        return getPropertyValue();
    }
}
