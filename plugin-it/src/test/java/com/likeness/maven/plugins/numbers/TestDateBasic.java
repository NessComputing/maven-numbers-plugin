package com.likeness.maven.plugins.numbers;

import java.io.File;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests basic functionality of the Strings elements.
 */
public class TestDateBasic
{
    @Test
    public void testNumber1() throws Exception
    {
        final DateTimeFormatter format = DateTimeFormat.forPattern("yyyyMMdd_HHmmss");

        final File jarLocation = TestUtils.runPackageGoal("date", "number1");

        final JarFile jarFile = new JarFile(jarLocation);
        final Manifest manifest = jarFile.getManifest();
        final Attributes attributes = manifest.getMainAttributes();

        final String regular = attributes.getValue("regular");
        Assert.assertNotNull(regular);
        final DateTime regularDate = format.parseDateTime(regular);
        Assert.assertNotNull(regularDate);

        final String regularUtc = attributes.getValue("regular-utc");
        Assert.assertNotNull(regularUtc);
        final DateTime regularUtcDate = format.parseDateTime(regularUtc);
        Assert.assertNotNull(regularUtcDate);

        final String epoch = attributes.getValue("epoch");
        Assert.assertNotNull(epoch);
        final DateTime epochDate = format.parseDateTime(epoch);
        Assert.assertNotNull(epochDate);

        final String epochUtc = attributes.getValue("epoch-utc");
        Assert.assertNotNull(epochUtc);
        final DateTime epochUtcDate = format.parseDateTime(epochUtc);
        Assert.assertNotNull(epochUtcDate);
    }
}
