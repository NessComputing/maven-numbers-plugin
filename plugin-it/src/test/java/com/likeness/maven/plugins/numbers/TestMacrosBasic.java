package com.likeness.maven.plugins.numbers;

import java.io.File;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests basic functionality of the Macros elements.
 */
public class TestMacrosBasic
{
    @Test
    public void testNumber1() throws Exception
    {
        final File jarLocation = TestUtils.runPackageGoal("macros", "number1");

        final JarFile jarFile = new JarFile(jarLocation);
        final Manifest manifest = jarFile.getManifest();
        final Attributes attributes = manifest.getMainAttributes();

        final String value = attributes.getValue("static");
        Assert.assertEquals("static-value", value);
    }

    @Test
    public void testNumber2() throws Exception
    {
        final File jarLocation = TestUtils.runPackageGoal("macros", "number2");

        final JarFile jarFile = new JarFile(jarLocation);
        final Manifest manifest = jarFile.getManifest();
        final Attributes attributes = manifest.getMainAttributes();

        final String value = attributes.getValue("property");
        Assert.assertEquals("property-value", value);
    }

    @Test
    public void testNumber3() throws Exception
    {
        final File jarLocation = TestUtils.runPackageGoal("macros", "number3");

        final JarFile jarFile = new JarFile(jarLocation);
        final Manifest manifest = jarFile.getManifest();
        final Attributes attributes = manifest.getMainAttributes();

        final String value = attributes.getValue("config");
        Assert.assertEquals("config-value", value);
    }
}

