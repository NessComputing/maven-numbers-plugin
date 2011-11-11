package com.likeness.maven.plugins.numbers.beans;

import org.junit.Assert;
import org.junit.Test;

public class TestAbstractDefinition
{
    @Test
    public void testValidId()
    {
        final BasicDefinition ad = new BasicDefinition();

        ad.setId("hello");
        ad.check();
    }

    @Test(expected=IllegalStateException.class)
    public void testUnsetId()
    {
        final BasicDefinition ad = new BasicDefinition();
        ad.check();
    }

    @Test(expected=IllegalStateException.class)
    public void testBlankId()
    {
        final BasicDefinition ad = new BasicDefinition();
        ad.setId("");
        ad.check();
    }

    @Test
    public void testDefaults()
    {
        final BasicDefinition ad = new BasicDefinition();
        Assert.assertNull(ad.getId());
        Assert.assertFalse(ad.isSkip());
    }

    public static class BasicDefinition extends AbstractDefinition<BasicDefinition>
    {
        public BasicDefinition() {
        }
    }
}
