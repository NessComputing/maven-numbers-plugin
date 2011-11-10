package com.likeness.maven.plugins.numbers;

import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.likeness.maven.plugins.numbers.beans.StringDefinition;

public class TestStringField
{
    @Test
    public void testSimple()
    {
        final StringDefinition f1 = new StringDefinition()
            .setId("hello")
            .setValues(ImmutableList.of("foo"));

        f1.check();

        final StringField sf1 = new StringField(f1, ValueProvider.NULL_PROVIDER);
        Assert.assertEquals("foo", sf1.getPropertyValue());
    }

    @Test
    public void testTwoValues()
    {
        final StringDefinition f1 = new StringDefinition()
            .setId("hello")
            .setValues(ImmutableList.of("foo", "bar", "baz"));

        f1.check();

        final StringField sf1 = new StringField(f1, ValueProvider.NULL_PROVIDER);
        Assert.assertEquals("foo", sf1.getPropertyValue());
    }

    @Test
    public void testIgnoreBlank()
    {
        final StringDefinition f1 = new StringDefinition()
            .setId("hello")
            .setValues(ImmutableList.of("", "      ", "baz"))
            .setBlankIsValid(false);

        f1.check();

        final StringField sf1 = new StringField(f1, ValueProvider.NULL_PROVIDER);
        Assert.assertEquals("baz", sf1.getPropertyValue());
    }

    @Test
    public void testAcceptBlank()
    {
        final StringDefinition f1 = new StringDefinition()
            .setId("hello")
            .setValues(ImmutableList.of("      ", "baz"))
            .setBlankIsValid(true);

        f1.check();

        final StringField sf1 = new StringField(f1, ValueProvider.NULL_PROVIDER);
        Assert.assertEquals("      ", sf1.getPropertyValue());
    }

    @Test
    public void testAcceptEmpty()
    {
        final StringDefinition f1 = new StringDefinition()
            .setId("hello")
            .setValues(ImmutableList.of("", "baz"))
            .setBlankIsValid(true);

        f1.check();

        final StringField sf1 = new StringField(f1, ValueProvider.NULL_PROVIDER);
        Assert.assertEquals("", sf1.getPropertyValue());
    }

    @Test
    public void testNullValueIsEmptyString()
    {
        final List<String> values = Lists.newArrayList();
        values.add(null);
        values.add("wibble");

        final StringDefinition f1 = new StringDefinition()
            .setId("hello")
            .setValues(values)
            .setBlankIsValid(true);

        f1.check();

        final StringField sf1 = new StringField(f1, ValueProvider.NULL_PROVIDER);
        Assert.assertEquals("", sf1.getPropertyValue());
    }

    @Test
    public void testSimpleProperty()
    {
        final StringDefinition f1 = new StringDefinition()
            .setId("hello");

        f1.check();

        final Properties props = new Properties();
        props.setProperty("hello", "foo");
        final StringField sf1 = new StringField(f1, new ValueProvider.PropertyProvider(props, f1.getPropertyName()));
        Assert.assertEquals("foo", sf1.getPropertyValue());
    }

    @Test
    public void testSimplePropertyWithDefault()
    {
        final StringDefinition f1 = new StringDefinition()
            .setId("hello")
            .setValues(ImmutableList.of("baz"));

        f1.check();

        final Properties props = new Properties();
        props.setProperty("hello", "foo");
        final StringField sf1 = new StringField(f1, new ValueProvider.PropertyProvider(props, f1.getPropertyName()));
        Assert.assertEquals("foo", sf1.getPropertyValue());
    }

    @Test
    public void testNoProperty()
    {
        final StringDefinition f1 = new StringDefinition()
            .setId("hello")
            .setValues(ImmutableList.of("baz"));

        f1.check();

        final Properties props = new Properties();
        props.setProperty("hello2", "foo");
        final StringField sf1 = new StringField(f1, new ValueProvider.PropertyProvider(props, f1.getPropertyName()));
        Assert.assertEquals("baz", sf1.getPropertyValue());
    }

    @Test
    public void testIgnoreBlankProperty()
    {
        final StringDefinition f1 = new StringDefinition()
            .setId("hello")
            .setBlankIsValid(false)
            .setValues(ImmutableList.of("baz"));

        f1.check();

        final Properties props = new Properties();
        props.setProperty("hello", "");
        final StringField sf1 = new StringField(f1, new ValueProvider.PropertyProvider(props, f1.getPropertyName()));
        Assert.assertEquals("baz", sf1.getPropertyValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testNothing()
    {
        final StringDefinition f1 = new StringDefinition()
            .setId("hello")
            .setOnMissingValue("fail")
            .setBlankIsValid(true);

        f1.check();

        final StringField sf1 = new StringField(f1, ValueProvider.NULL_PROVIDER);
        Assert.assertEquals("baz", sf1.getPropertyValue());
    }

    @Test
    public void testNothingIgnore()
    {
        final StringDefinition f1 = new StringDefinition()
            .setId("hello")
            .setOnMissingValue("ignore")
            .setBlankIsValid(true);

        f1.check();

        final StringField sf1 = new StringField(f1, ValueProvider.NULL_PROVIDER);
        Assert.assertEquals(null, sf1.getPropertyValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testMissingProperty()
    {
        final StringDefinition f1 = new StringDefinition()
            .setId("hello")
            .setOnMissingValue("ignore")
            .setOnMissingProperty("fail")
            .setBlankIsValid(true);

        f1.check();

        final PropertyCache propertyCache = new PropertyCache();
        final ValueProvider provider = propertyCache.findCurrentValue(new Properties(), f1);

        final StringField sf1 = new StringField(f1, provider);
        Assert.assertEquals(null, sf1.getPropertyValue());
    }

    @Test
    public void testBlankPropertyValue()
    {
        final StringDefinition f1 = new StringDefinition()
            .setId("hello")
            .setBlankIsValid(true);

        f1.check();

        final PropertyCache propertyCache = new PropertyCache();
        final Properties props = new Properties();
        props.setProperty("hello", "");
        final ValueProvider provider = propertyCache.findCurrentValue(props, f1);

        final StringField sf1 = new StringField(f1, provider);
        Assert.assertEquals("", sf1.getPropertyValue());
    }

    @Test
    public void testBlankValue()
    {
        final StringDefinition f1 = new StringDefinition()
            .setId("hello")
            .setValues(ImmutableList.of("", "foo"))
            .setBlankIsValid(true);

        f1.check();

        final StringField sf1 = new StringField(f1, ValueProvider.NULL_PROVIDER);
        Assert.assertEquals("", sf1.getPropertyValue());
    }
}
