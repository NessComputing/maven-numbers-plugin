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

import javax.annotation.Nonnull;

import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.PlexusContainer;
import org.easymock.EasyMock;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.likeness.maven.plugins.numbers.beans.MacroDefinition;
import com.likeness.maven.plugins.numbers.macros.DemoMacro;
import com.likeness.maven.plugins.numbers.macros.MacroType;

public class TestMacroField
{
    private PlexusContainer fakeContainer = null;
    private MavenProject fakeProject = null;
    private AbstractNumbersMojo fakeMojo = null;

    @Before
    public void setUp() throws Exception
    {
        fakeContainer = EasyMock.createNiceMock(PlexusContainer.class);
        EasyMock.expect(fakeContainer.lookup(MacroType.ROLE, "demo")).andReturn(new DemoMacro()).anyTimes();
        fakeProject = EasyMock.createNiceMock(MavenProject.class);
        fakeMojo = EasyMock.createNiceMock(AbstractNumbersMojo.class);
        EasyMock.expect(fakeMojo.getProject()).andReturn(fakeProject).anyTimes();
        EasyMock.expect(fakeMojo.getContainer()).andReturn(fakeContainer).anyTimes();
        EasyMock.replay(fakeContainer, fakeProject, fakeMojo);
    }

    @After
    public void tearDown()
    {
        EasyMock.verify(fakeContainer, fakeProject, fakeMojo);
    }

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

        final MacroField sm1 = new MacroField(m1, ValueProvider.NULL_PROVIDER, fakeMojo);
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

        final MacroField sm1 = new MacroField(m1, ValueProvider.NULL_PROVIDER, fakeMojo);
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
        final MacroField sm1 = new MacroField(m1, new ValueProvider.PropertyProvider(props, m1.getPropertyName()), fakeMojo);
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

        final MacroField sm1 = new MacroField(m1, ValueProvider.NULL_PROVIDER, fakeMojo);
        Assert.assertEquals("TestValue", sm1.getPropertyValue());
    }

    public static class TestMacro implements MacroType
    {
        @Override
        public String getValue(@Nonnull final MacroDefinition macroDefinition,
                               @Nonnull final ValueProvider valueProvider,
                               @Nonnull final AbstractNumbersMojo mojo)
        {
            return "TestValue";
        }
    }
}
