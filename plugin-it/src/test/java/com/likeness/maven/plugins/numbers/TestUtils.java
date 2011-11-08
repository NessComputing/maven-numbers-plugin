package com.likeness.maven.plugins.numbers;

import static java.lang.String.format;

import java.io.File;
import java.util.List;

import com.google.common.collect.ImmutableList;
import org.apache.maven.it.Verifier;
import org.junit.Assert;

public final class TestUtils
{
    private TestUtils()
    {
    }

    public static final String GROUP = "test-group";
    public static final String ARTIFACT = "test-artifact";
    public static final String VERSION = "1.0";

    public static String getLocation(final String testName)
    {
        return new File(format("target/test-classes/%s", testName)).getAbsolutePath();
    }

    public static File getTargetArtifact(final String pomName, final String testName, final String type)
    {
        final String location = getLocation(pomName);

        return new File(format("%s/target/%s-%s-%s.%s", location, ARTIFACT, testName, VERSION, type));
    }

    public static File runPackageGoal(final String pomName, final String testName)
        throws Exception
    {
        final String location = TestUtils.getLocation(pomName);
        final Verifier verifier = new Verifier(location);
        verifier.getEnvironmentVariables().setProperty("testName", testName);
        final List<String> goals = ImmutableList.of("package");
        verifier.addCliOption("-X");
        verifier.executeGoals(goals);

        verifier.verifyErrorFreeLog();
        verifier.resetStreams();

        final File jarLocation = TestUtils.getTargetArtifact(pomName, testName, "jar");
        Assert.assertTrue(format("'%s' does not exist!", jarLocation.getAbsolutePath()), jarLocation.exists());
        Assert.assertTrue(format("'%s' is not a file!", jarLocation.getAbsolutePath()), jarLocation.isFile());
        return jarLocation;
    }
}
