package com.likeness.maven.plugins.numbers;

import java.io.File;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.junit.Assert;
import org.junit.Test;

public class TestGroupsBasic
{
    @Test
    public void testNumber1() throws Exception
    {
        final File jarLocation = TestUtils.runPackageGoal("groups", "number1");

        final JarFile jarFile = new JarFile(jarLocation);
        final Manifest manifest = jarFile.getManifest();
        final Attributes attributes = manifest.getMainAttributes();

        final String groupDemo = attributes.getValue("my-groupdemo");
        Assert.assertEquals("4.8.15.16-maven-numbers-plugin-it-19700101_000000", groupDemo);
    }

    @Test
    public void testNumber2() throws Exception
    {
        final File jarLocation = TestUtils.runPackageGoal("groups", "number2");

        final JarFile jarFile = new JarFile(jarLocation);
        final Manifest manifest = jarFile.getManifest();
        final Attributes attributes = manifest.getMainAttributes();

        final String number = attributes.getValue("my-number");
        Assert.assertEquals("4.8.15.16", number);
    }

    @Test
    public void testNumber3() throws Exception
    {
        final File jarLocation = TestUtils.runPackageGoal("groups", "number3");

        final JarFile jarFile = new JarFile(jarLocation);
        final Manifest manifest = jarFile.getManifest();
        final Attributes attributes = manifest.getMainAttributes();

        final String str = attributes.getValue("my-string");
        Assert.assertEquals("maven-numbers-plugin-it", str);
    }

    @Test
    public void testNumber4() throws Exception
    {
        final File jarLocation = TestUtils.runPackageGoal("groups", "number4");

        final JarFile jarFile = new JarFile(jarLocation);
        final Manifest manifest = jarFile.getManifest();
        final Attributes attributes = manifest.getMainAttributes();

        final String date = attributes.getValue("my-date");
        Assert.assertEquals("19700101_000000", date);

    }
}

