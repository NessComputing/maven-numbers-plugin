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

import java.util.Properties;

import com.paywholesail.mojo.numbers.DateField;
import com.paywholesail.mojo.numbers.ValueProvider;
import com.paywholesail.mojo.numbers.beans.DateDefinition;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;


public class TestDateField
{
    @Test
    public void testSimple()
    {
        final DateDefinition d1 = new DateDefinition()
            .setId("hello")
            .setTimezone("UTC")
            .setValue(0L)
            .setFormat("yyyyMMdd_HHmmss");

        d1.check();

        final DateField sd1 = new DateField(d1, ValueProvider.NULL_PROVIDER);
        Assert.assertEquals("19700101_000000", sd1.getPropertyValue());
    }

    @Test
    public void testProperty()
    {
        final String format = "yyyyMMdd_HHmmss";
        final DateDefinition d1 = new DateDefinition()
            .setId("hello")
            .setFormat(format);

        d1.check();

        final long now = System.currentTimeMillis();
        final Properties props = new Properties();
        props.setProperty("hello", Long.toString(now));
        final DateField sd1 = new DateField(d1, new ValueProvider.PropertyProvider(props, d1.getPropertyName()));

        final String value = DateTimeFormatter.ofPattern(format).format(ZonedDateTime.ofInstant(Instant.ofEpochMilli(now), ZoneId.systemDefault()));

        Assert.assertEquals(value, sd1.getPropertyValue());
    }

    @Test
    public void testNow()
    {
        final String format = "yyyyMMdd_HHmmss";
        final DateDefinition d1 = new DateDefinition()
            .setId("hello")
            .setFormat(format);

        d1.check();

        final DateField sd1 = new DateField(d1, ValueProvider.NULL_PROVIDER);
        final String value = sd1.getPropertyValue();

        final LocalDateTime propTime = DateTimeFormatter.ofPattern(format).parse(value, LocalDateTime::from);
        final ZonedDateTime now = ZonedDateTime.now();
        final Duration d = Duration.between(propTime, now);
        Assert.assertTrue(d.getSeconds() < 1);
    }
}
