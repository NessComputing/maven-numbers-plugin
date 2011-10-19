package com.likeness.maven.plugins.numbers;

import java.io.File;
import java.util.List;

import org.apache.maven.it.Verifier;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class TestGetMojo
{
    @Test
    public void testBasicMojo() throws Exception
    {
        final Verifier verifier = new Verifier(new File("target/test-classes/basic-get").getAbsolutePath());
        final List<String> goals = ImmutableList.of("clean", "package");
        verifier.executeGoals(goals);

    }
}
