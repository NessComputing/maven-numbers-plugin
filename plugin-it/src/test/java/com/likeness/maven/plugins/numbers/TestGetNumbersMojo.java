package com.likeness.maven.plugins.numbers;

import java.io.File;
import java.util.List;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.apache.maven.it.Verifier;
import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

/**
 * Tests basic functionality of the Number get mojo. This uses the pom.xml file in src/test/resources/basic-get and a lot of the
 * configuration there is expected in this test.
 *
 */
public class TestGetNumbersMojo
{
    public static final String TEST_NAME = "basic-get";

    @Test
    public void testBasicMojo() throws Exception
    {
        final String location = TestUtils.getLocation(TEST_NAME);
        final Verifier verifier = new Verifier(location);
        final List<String> goals = ImmutableList.of("package");
        verifier.addCliOption("-X");
        verifier.executeGoals(goals);

        final File jarLocation = TestUtils.getTargetArtifact(TEST_NAME, "jar");
        Assert.assertTrue(jarLocation.exists());
        Assert.assertTrue(jarLocation.isFile());
        final JarFile jarFile = new JarFile(jarLocation);

        final Manifest manifest = jarFile.getManifest();
        final Attributes attributes = manifest.getMainAttributes();
        final String testGetNumberStr = attributes.getValue("Test-Get-Number");
        Assert.assertNotNull(testGetNumberStr);
        Assert.assertEquals(0L, Long.parseLong(testGetNumberStr));

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();
    }
}
