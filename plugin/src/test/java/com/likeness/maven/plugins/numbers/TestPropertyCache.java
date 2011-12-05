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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.google.common.io.Closeables;
import com.likeness.maven.plugins.numbers.beans.NumberDefinition;

public class TestPropertyCache
{
    private PropertyCache pc = null;
    private File propFile = null;
    private FileWriter writer = null;
    private final Properties props = new Properties();

    @Before
    public void setUp()
            throws IOException
    {
        Assert.assertNull(pc);
        pc = new PropertyCache();

        Assert.assertNull(propFile);
        propFile = File.createTempFile("test", null);
        propFile.deleteOnExit();

        Assert.assertNull(writer);
        writer = new FileWriter(propFile);
    }

    @After
    public void tearDown()
            throws IOException
    {
        Assert.assertNotNull(pc);
        Assert.assertNotNull(propFile);
        Assert.assertNotNull(writer);
        Closeables.closeQuietly(writer);
    }

    @Test
    public void testEphemeralDefault()
            throws IOException
    {
        final NumberDefinition ephemeral = new NumberDefinition().setId("hello");
        ephemeral.check();
        ValueProvider valueProvider = pc.getPropertyValue(ephemeral);
        Assert.assertEquals(ephemeral.getInitialValue(), valueProvider.getValue());
    }

    @Test(expected = IllegalStateException.class)
    @edu.umd.cs.findbugs.annotations.SuppressWarnings(value="DMI_HARDCODED_ABSOLUTE_FILENAME", justification="unit test")
    public void testMissingPropertyFileFail()
            throws IOException
    {
        final NumberDefinition fileBacked = new NumberDefinition()
                .setId("hello")
                .setOnMissingFile("FAIL")
                .setOnMissingProperty("IGNORE")
                .setPropertyFile(new File("/does/not/exist"));
        fileBacked.check();
        pc.getPropertyValue(fileBacked);
    }

    @Test
    public void testEmptyPropertyFileCreate()
            throws IOException
    {
        props.store(writer, null);
        writer.flush();
        writer.close();

        final NumberDefinition fileBacked = new NumberDefinition()
                .setId("hello")
                .setOnMissingFile("FAIL")
                .setOnMissingProperty("CREATE")
                .setPropertyFile(propFile);
        fileBacked.check();
        ValueProvider valueProvider = pc.getPropertyValue(fileBacked);
        Assert.assertEquals(fileBacked.getInitialValue(), valueProvider.getValue());
    }

    @Test
    public void testEmptyPropertyFileIgnore()
            throws IOException
    {
        props.store(writer, null);
        writer.flush();
        writer.close();

        final NumberDefinition fileBacked = new NumberDefinition()
                .setId("hello")
                .setOnMissingFile("FAIL")
                .setOnMissingProperty("IGNORE")
                .setPropertyFile(propFile);
        fileBacked.check();
        ValueProvider valueProvider = pc.getPropertyValue(fileBacked);
        Assert.assertNull(valueProvider.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testEmptyPropertyFileFail()
            throws IOException
    {
        final Properties props = new Properties();
        final FileWriter writer = new FileWriter(propFile);
        props.store(writer, null);
        writer.flush();
        writer.close();

        final NumberDefinition fileBacked = new NumberDefinition()
                .setId("hello")
                .setOnMissingFile("FAIL")
                .setOnMissingProperty("FAIL")
                .setPropertyFile(propFile);
        fileBacked.check();
        pc.getPropertyValue(fileBacked);
    }

    public void testLoadProperty()
            throws IOException
    {
        final Properties props = new Properties();
        final FileWriter writer = new FileWriter(propFile);
        final String propValue = "12345";

        props.setProperty("hello", propValue);
        props.store(writer, null);
        writer.flush();
        writer.close();

        final NumberDefinition fileBacked = new NumberDefinition()
                .setId("hello")
                .setOnMissingFile("FAIL")
                .setOnMissingProperty("FAIL")
                .setPropertyFile(propFile);
        fileBacked.check();
        ValueProvider valueProvider = pc.getPropertyValue(fileBacked);
        Assert.assertEquals(propValue, valueProvider.getValue());
    }

    public void testIgnoreCreate()
            throws IOException
    {
        final Properties props = new Properties();
        final FileWriter writer = new FileWriter(propFile);
        final String propValue = "12345";

        props.setProperty("hello", propValue);
        props.store(writer, null);
        writer.flush();
        writer.close();

        final NumberDefinition fileBacked = new NumberDefinition()
                .setId("hello")
                .setOnMissingFile("FAIL")
                .setOnMissingProperty("CREATE")
                .setPropertyFile(propFile);
        fileBacked.check();
        ValueProvider valueProvider = pc.getPropertyValue(fileBacked);
        Assert.assertEquals(propValue, valueProvider.getValue());
    }

    public void samePropertyObject()
        throws IOException
    {
        final Properties props = new Properties();
        final FileWriter writer = new FileWriter(propFile);

        props.setProperty("hello", "hello");
        props.setProperty("world", "world");
        props.store(writer, null);
        writer.flush();
        writer.close();

        final NumberDefinition n1 = new NumberDefinition()
                .setId("hello")
                .setOnMissingFile("FAIL")
                .setOnMissingProperty("FAIL")
                .setPropertyFile(propFile);
        final NumberDefinition n2 = new NumberDefinition()
                .setId("world")
                .setOnMissingFile("FAIL")
                .setOnMissingProperty("FAIL")
                .setPropertyFile(propFile);

        n1.check();
        n2.check();
        Assert.assertEquals("hello", pc.getPropertyValue(n1).getValue());
        Assert.assertEquals("world", pc.getPropertyValue(n2).getValue());

        Assert.assertSame(pc.getProperties(n1), pc.getProperties(n2));
    }
}
