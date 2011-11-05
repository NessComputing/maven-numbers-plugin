package com.likeness.maven.plugins.numbers.beans;

import org.junit.Assert;
import org.junit.Test;

public class TestNumberDefinition
{
    @Test
    public void testValid()
    {
        final NumberDefinition nd = new NumberDefinition("hello", false, "1", 0, 1, null, null, IWFCEnum.FAIL, IWFCEnum.FAIL);
        nd.check();
    }

    @Test
    public void testValid2()
    {
        final NumberDefinition nd = new NumberDefinition();
        nd.setId("hello");
        nd.check();
    }

    @Test
    public void testDefaults()
    {
        final NumberDefinition nd = new NumberDefinition();
        Assert.assertEquals("0", nd.getInitialValue());
        Assert.assertEquals(0, nd.getFieldNumber());
        Assert.assertEquals(1, nd.getIncrement());
        Assert.assertNull(nd.getPropertyName());
        Assert.assertNull(nd.getPropertyFile());
        Assert.assertEquals(IWFCEnum.FAIL, nd.getOnMissingFile());
        Assert.assertEquals(IWFCEnum.FAIL, nd.getOnMissingProperty());
    }

    @Test
    public void testPropNameOverridesId()
    {
        final NumberDefinition nd = new NumberDefinition();
        nd.setId("hello");
        nd.setPropertyName("world");
        Assert.assertEquals("hello", nd.getId());
        Assert.assertEquals("world", nd.getPropertyName());
    }

    @Test
    public void testIdSuppliesPropName()
    {
        final NumberDefinition nd = new NumberDefinition();
        nd.setId("hello");
        Assert.assertEquals("hello", nd.getId());
        Assert.assertEquals("hello", nd.getPropertyName());
    }

    @Test(expected = IllegalStateException.class)
    public void testNullInitialValue()
    {
        final NumberDefinition nd = new NumberDefinition();
        nd.setInitialValue(null);
        nd.check();
    }

    @Test(expected = IllegalStateException.class)
    public void testBlankInitialValue()
    {
        final NumberDefinition nd = new NumberDefinition();
        nd.setInitialValue("");
        nd.check();
    }

    @Test(expected = IllegalStateException.class)
    public void testBadFieldNumber()
    {
        final NumberDefinition nd = new NumberDefinition();
        nd.setFieldNumber(-1);
        nd.check();
    }
}
