package com.likeness.maven.plugins.numbers;

import com.likeness.maven.plugins.numbers.NumberField;
import com.likeness.maven.plugins.numbers.beans.NumberDefinition;
import org.junit.Assert;
import org.junit.Test;

public class TestNumberField
{
    @Test
    public void testSimple()
    {
        final NumberDefinition f1 = new NumberDefinition()
            .setId("hello")
            .setFieldNumber(0);
        f1.check();

        final NumberField nf1 = new NumberField(f1, "100");
        Assert.assertEquals(100L, nf1.getNumberValue().longValue());
    }

    @Test
    public void testThreeElements()
    {
        final NumberDefinition f1 = new NumberDefinition()
            .setId("hello")
            .setFieldNumber(0);
        final NumberDefinition f2 = new NumberDefinition()
            .setId("hello")
            .setFieldNumber(1);
        final NumberDefinition f3 = new NumberDefinition()
            .setId("hello")
            .setFieldNumber(2);
        f1.check();
        f2.check();
        f3.check();

        final String value = "4.8.15";
        final NumberField nf1 = new NumberField(f1, value);
        final NumberField nf2 = new NumberField(f2, value);
        final NumberField nf3 = new NumberField(f3, value);
        Assert.assertEquals(4L, nf1.getNumberValue().longValue());
        Assert.assertEquals(8L, nf2.getNumberValue().longValue());
        Assert.assertEquals(15L, nf3.getNumberValue().longValue());

        Assert.assertEquals(value, nf1.toString());
        Assert.assertEquals(value, nf2.toString());
        Assert.assertEquals(value, nf3.toString());
    }

    @Test
    public void testItsComplicated()
    {
        final NumberDefinition f1 = new NumberDefinition()
            .setId("hello")
            .setFieldNumber(0);
        final NumberDefinition f2 = new NumberDefinition()
            .setId("hello")
            .setFieldNumber(1);
        final NumberDefinition f3 = new NumberDefinition()
            .setId("hello")
            .setFieldNumber(2);
        final NumberDefinition f4 = new NumberDefinition()
            .setId("hello")
            .setFieldNumber(3);
        f1.check();
        f2.check();
        f3.check();
        f4.check();

        final String value = "3.2-alpha-1-test-4";
        final NumberField nf1 = new NumberField(f1, value);
        final NumberField nf2 = new NumberField(f2, value);
        final NumberField nf3 = new NumberField(f3, value);
        final NumberField nf4 = new NumberField(f4, value);

        Assert.assertEquals(3L, nf1.getNumberValue().longValue());
        Assert.assertEquals(2L, nf2.getNumberValue().longValue());
        Assert.assertEquals(1L, nf3.getNumberValue().longValue());
        Assert.assertEquals(4L, nf4.getNumberValue().longValue());

        Assert.assertEquals(value, nf1.toString());
        Assert.assertEquals(value, nf2.toString());
        Assert.assertEquals(value, nf3.toString());
        Assert.assertEquals(value, nf4.toString());
    }
}
    
