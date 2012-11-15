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
package com.nesscomputing.mojo.numbers.beans;

import org.junit.Assert;
import org.junit.Test;

import com.nesscomputing.mojo.numbers.beans.IWFCEnum;
import com.nesscomputing.mojo.numbers.beans.NumberDefinition;


public class TestNumberDefinition
{
    @Test
    public void testValid()
    {
        final NumberDefinition nd = new NumberDefinition("hello", false, true, "1", 0, 1, null, null, IWFCEnum.FAIL, IWFCEnum.FAIL, null);
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
        Assert.assertFalse(nd.isExport());
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
