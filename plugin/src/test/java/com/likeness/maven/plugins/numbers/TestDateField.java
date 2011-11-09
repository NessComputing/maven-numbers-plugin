package com.likeness.maven.plugins.numbers;

import java.util.Properties;

import com.likeness.maven.plugins.numbers.beans.DateDefinition;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.Duration;
import org.joda.time.Interval;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
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

        final String value = DateTimeFormat.forPattern(format).print(now);

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

        final DateTime propTime = DateTimeFormat.forPattern(format).parseDateTime(value);
        final DateTime now = new DateTime();
        final Duration d = new Duration(propTime, now);
        Assert.assertTrue(d.getStandardSeconds() < 1);
    }
}
