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
