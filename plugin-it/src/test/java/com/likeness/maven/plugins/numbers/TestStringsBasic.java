package com.likeness.maven.plugins.numbers;

import java.io.File;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests basic functionality of the Strings elements.
 */
public class TestStringsBasic
{
    @Test
    public void testNumber1() throws Exception
    {
        final File jarLocation = TestUtils.runPackageGoal("strings", "number1");

        final JarFile jarFile = new JarFile(jarLocation);
        final Manifest manifest = jarFile.getManifest();
        final Attributes attributes = manifest.getMainAttributes();

        final String regular = attributes.getValue("regular");
        Assert.assertEquals("regular-value", regular);

        final String skipBlank = attributes.getValue("skip-blank");
        Assert.assertEquals("skip-blank-value", skipBlank);

        final String acceptBlank = attributes.getValue("accept-blank");
        Assert.assertEquals("", acceptBlank);

        final String nullValue = attributes.getValue("null-value");
        Assert.assertEquals("", nullValue);

        final String propValue = attributes.getValue("prop-value");
        Assert.assertEquals("from a properties file", propValue);

        final String propDefaultValue = attributes.getValue("prop-default");
        Assert.assertEquals("prop-default-value", propDefaultValue);
    }
}

