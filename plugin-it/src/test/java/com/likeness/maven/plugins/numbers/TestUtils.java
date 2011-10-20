package com.likeness.maven.plugins.numbers;

import static java.lang.String.format;

import java.io.File;

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

    public static File getTargetArtifact(final String testName, final String type)
    {
        final String location = getLocation(testName);

        return new File(format("%s/target/%s-%s-%s.%s", location, ARTIFACT, testName, VERSION, type));
    }
}
