package com.likeness.maven.plugins.numbers.beans;

import org.junit.Assert;
import org.junit.Test;

public class TestAbstractDefinition
{
    @Test
    public void testValidId()
    {
        final AbstractDefinition ad = new AbstractDefinition() {};

        ad.setId("hello");
        ad.check();
    }

    @Test(expected=IllegalStateException.class)
    public void testUnsetId()
    {
        final AbstractDefinition ad = new AbstractDefinition() {};
        ad.check();
    }

    @Test(expected=IllegalStateException.class)
    public void testBlankId()
    {
        final AbstractDefinition ad = new AbstractDefinition() {};
        ad.setId("");
        ad.check();
    }

    @Test
    public void testDefaults()
    {
        final AbstractDefinition ad = new AbstractDefinition() {};
        Assert.assertNull(ad.getId());
        Assert.assertFalse(ad.isSkip());
    }

}
