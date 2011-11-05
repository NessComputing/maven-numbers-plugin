package com.likeness.maven.plugins.numbers.beans;

import org.junit.Assert;
import org.junit.Test;

public class TestIWFCEnum
{
    @Test
    public void testValid()
    {
        IWFCEnum value = IWFCEnum.forString("fail");
        Assert.assertSame(IWFCEnum.FAIL, value);
        value = IWFCEnum.forString("warn");
        Assert.assertSame(IWFCEnum.WARN, value);
        value = IWFCEnum.forString("ignore");
        Assert.assertSame(IWFCEnum.IGNORE, value);
        value = IWFCEnum.forString("create");
        Assert.assertSame(IWFCEnum.CREATE, value);
    }

    @Test
    public void testValidCases()
    {
        IWFCEnum value = IWFCEnum.forString("fail");
        Assert.assertSame(IWFCEnum.FAIL, value);
        value = IWFCEnum.forString("FAIL");
        Assert.assertSame(IWFCEnum.FAIL, value);
        value = IWFCEnum.forString("Fail");
        Assert.assertSame(IWFCEnum.FAIL, value);
        value = IWFCEnum.forString("FaIl");
        Assert.assertSame(IWFCEnum.FAIL, value);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadValue()
    {
        IWFCEnum.forString("foobar");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullValue()
    {
        IWFCEnum.forString(null);
    }

    @Test
    public void testCheckState()
    {
        boolean value = IWFCEnum.checkState(IWFCEnum.FAIL, true, "");
        Assert.assertFalse(value);
        value = IWFCEnum.checkState(IWFCEnum.IGNORE, false, "");
        Assert.assertFalse(value);
        value = IWFCEnum.checkState(IWFCEnum.WARN, false, "");
        Assert.assertFalse(value);
        value = IWFCEnum.checkState(IWFCEnum.CREATE, false, "");
        Assert.assertTrue(value);
    }

    @Test(expected = IllegalStateException.class)
    public void testCheckStateFail()
    {
        IWFCEnum.checkState(IWFCEnum.FAIL, false, "");
    }
}
