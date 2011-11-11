package com.likeness.maven.plugins.numbers.beans;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class TestPropertyGroup
{
    @Test
    public void testConstant()
    {
        final Properties props = new Properties();
        props.setProperty("hello", "world");

        PropertyGroup pg = new PropertyGroup("hello", true, true, IWFEnum.FAIL, IWFEnum.FAIL, props);

        final List<String> propNames = Lists.newArrayList(pg.getPropertyNames());
        Assert.assertEquals(1, propNames.size());
        Assert.assertEquals("hello", propNames.get(0));

        final String propValue = pg.getPropertyValue("hello", Collections.<String, String>emptyMap());
        Assert.assertEquals("world", propValue);
    }

    @Test
    public void testRenderSingle()
    {
        final Properties props = new Properties();
        props.setProperty("hello", "{world}");

        PropertyGroup pg = new PropertyGroup("hello", true, true, IWFEnum.FAIL, IWFEnum.FAIL, props);

        final List<String> propNames = Lists.newArrayList(pg.getPropertyNames());
        Assert.assertEquals(1, propNames.size());
        Assert.assertEquals("hello", propNames.get(0));

        final String propValue = pg.getPropertyValue("hello", ImmutableMap.of("world", "pizza"));
        Assert.assertEquals("pizza", propValue);
    }

    @Test(expected=IllegalStateException.class)
    public void testRenderEmptyFail()
    {
        final Properties props = new Properties();
        props.setProperty("hello", "{world}");

        PropertyGroup pg = new PropertyGroup("hello", true, true, IWFEnum.FAIL, IWFEnum.FAIL, props);

        final List<String> propNames = Lists.newArrayList(pg.getPropertyNames());
        Assert.assertEquals(1, propNames.size());
        Assert.assertEquals("hello", propNames.get(0));

        final String propValue = pg.getPropertyValue("hello", Collections.<String, String>emptyMap());
        Assert.assertEquals("", propValue);
    }

    @Test
    public void testRenderEmptyOk()
    {
        final Properties props = new Properties();
        props.setProperty("hello", "nice-{world}-hat");

        PropertyGroup pg = new PropertyGroup("hello", true, true, IWFEnum.FAIL, IWFEnum.IGNORE, props);

        final List<String> propNames = Lists.newArrayList(pg.getPropertyNames());
        Assert.assertEquals(1, propNames.size());
        Assert.assertEquals("hello", propNames.get(0));

        final String propValue = pg.getPropertyValue("hello", Collections.<String, String>emptyMap());
        Assert.assertEquals("nice--hat", propValue);
    }
}
