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
package com.likeness.maven.plugins.numbers;

import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.likeness.maven.plugins.numbers.beans.MacroDefinition;
import com.likeness.maven.plugins.numbers.macros.MacroType;

public class TestMacroField
{
    @Test
    public void testSimpleStatic()
            throws Exception
    {
        final Properties mp = new Properties();
        mp.put("type","static");

        final MacroDefinition m1 = new MacroDefinition()
            .setId("hello")
            .setMacroType("demo")
            .setProperties(mp);

        m1.check();

        final MacroField sm1 = new MacroField(m1, ValueProvider.NULL_PROVIDER);
        Assert.assertEquals("static-value", sm1.getPropertyValue());
    }

    @Test
    public void testSimpleConfig()
            throws Exception
    {
        final Properties mp = new Properties();
        mp.put("type","other");
        mp.put("value", "brains");

        final MacroDefinition m1 = new MacroDefinition()
            .setId("hello")
            .setMacroType("demo")
            .setProperties(mp);

        m1.check();

        final MacroField sm1 = new MacroField(m1, ValueProvider.NULL_PROVIDER);
        Assert.assertEquals("brains", sm1.getPropertyValue());
    }

    @Test
    public void testSimpleProps()
            throws Exception
    {
        final Properties mp = new Properties();
        mp.put("type","property");

        final MacroDefinition m1 = new MacroDefinition()
            .setId("hello")
            .setMacroType("demo")
            .setProperties(mp);

        m1.check();

        final Properties props = new Properties();
        props.setProperty("hello", "foo");
        final MacroField sm1 = new MacroField(m1, new ValueProvider.PropertyProvider(props, m1.getPropertyName()));
        Assert.assertEquals("foo", sm1.getPropertyValue());
    }

    @Test
    public void testMyClass()
            throws Exception
    {
        final MacroDefinition m1 = new MacroDefinition()
            .setId("hello")
            .setMacroClass(TestMacro.class.getName());

        m1.check();

        final MacroField sm1 = new MacroField(m1, ValueProvider.NULL_PROVIDER);
        Assert.assertEquals("TestValue", sm1.getPropertyValue());
    }

    public static class TestMacro implements MacroType
    {
        @Override
        public String getValue(final MacroDefinition macroDefinition, final ValueProvider valueProvider)
        {
            return "TestValue";
        }
    }
}
