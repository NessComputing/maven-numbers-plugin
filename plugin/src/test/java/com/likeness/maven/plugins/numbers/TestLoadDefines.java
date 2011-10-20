package com.likeness.maven.plugins.numbers;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableList;

public class TestLoadDefines
{
    @Test
    public void testOneTransientCounter() throws Exception
    {
        final TestMojo testMojo = new TestMojo(ImmutableList.of(new CountDefine("hello", null, 1, 1, false, false, null)));
        testMojo.loadDefines();
        final Map<File, Properties> propertiesFiles = testMojo.getPropertiesFiles();
        Assert.assertNotNull(propertiesFiles);
        Assert.assertTrue(propertiesFiles.isEmpty());
    }

    @Test
    public void testTwoTransientCounters() throws Exception
    {
        final TestMojo testMojo = new TestMojo(ImmutableList.of(new CountDefine("hello", null, 1, 1, false, false, null), new CountDefine("world", null, 1, 1, false, false, null)));
        testMojo.loadDefines();
        final Map<File, Properties> propertiesFiles = testMojo.getPropertiesFiles();
        Assert.assertNotNull(propertiesFiles);
        Assert.assertTrue(propertiesFiles.isEmpty());
    }

    @Test(expected=IllegalArgumentException.class)
    public void testErrorNameTwice() throws Exception
    {
        final TestMojo testMojo = new TestMojo(ImmutableList.of(new CountDefine("hello", null, 1, 1, false, false, null), new CountDefine("hello", null, 1, 1, false, false, null)));
        testMojo.loadDefines();
    }

    @Test
    public void testCreateOnePersistentCounter() throws Exception
    {
        final File countFile = File.createTempFile("test", null);
        countFile.deleteOnExit();

        final TestMojo testMojo = new TestMojo(ImmutableList.of(new CountDefine("hello", countFile, 1, 1, true, true, null)));
        testMojo.loadDefines();
        final Map<File, Properties> propertiesFiles = testMojo.getPropertiesFiles();
        Assert.assertNotNull(propertiesFiles);
        Assert.assertEquals(1, propertiesFiles.size());
        Assert.assertTrue(propertiesFiles.containsKey(countFile));
    }

    @Test
    public void testCreateTwoPersistentCounters() throws Exception
    {
        final File countFile = File.createTempFile("test", null);
        final File countFile2 = File.createTempFile("test", null);
        countFile.deleteOnExit();
        countFile2.deleteOnExit();

        final TestMojo testMojo = new TestMojo(ImmutableList.of(new CountDefine("hello", countFile, 1, 1, true, true, null),
                                                                new CountDefine("world", countFile2, 1, 1, true, true, null)));
        testMojo.loadDefines();
        final Map<File, Properties> propertiesFiles = testMojo.getPropertiesFiles();
        Assert.assertNotNull(propertiesFiles);
        Assert.assertEquals(2, propertiesFiles.size());
        Assert.assertTrue(propertiesFiles.containsKey(countFile));
        Assert.assertTrue(propertiesFiles.containsKey(countFile2));
    }

    @Test
    public void testCreateTwoPersistentCountersSameFile() throws Exception
    {
        final File countFile = File.createTempFile("test", null);
        countFile.deleteOnExit();

        final TestMojo testMojo = new TestMojo(ImmutableList.of(new CountDefine("hello", countFile, 1, 1, true, true, null),
                                                                new CountDefine("world", countFile, 1, 1, true, true, null)));
        testMojo.loadDefines();
        final Map<File, Properties> propertiesFiles = testMojo.getPropertiesFiles();
        Assert.assertNotNull(propertiesFiles);
        Assert.assertEquals(1, propertiesFiles.size());
        Assert.assertTrue(propertiesFiles.containsKey(countFile));
    }

    @Test
    public void testOnePersistentCounterNoFile() throws Exception
    {
        final File countFile = File.createTempFile("test", null);
        countFile.delete();

        final TestMojo testMojo = new TestMojo(ImmutableList.of(new CountDefine("hello", countFile, 1, 1, true, true, null)));
        testMojo.loadDefines();
        final Map<File, Properties> propertiesFiles = testMojo.getPropertiesFiles();
        Assert.assertNotNull(propertiesFiles);
        Assert.assertEquals(0, propertiesFiles.size());
    }

    @Test
    public void testCreateOnePersistentCounterNoCreation() throws Exception
    {
        final File countFile = File.createTempFile("test", null);
        countFile.deleteOnExit();

        final TestMojo testMojo = new TestMojo(ImmutableList.of(new CountDefine("hello", countFile, 1, 1, true, false, null)));
        testMojo.loadDefines();
        final Map<File, Properties> propertiesFiles = testMojo.getPropertiesFiles();
        Assert.assertNotNull(propertiesFiles);
        Assert.assertEquals(1, propertiesFiles.size());
        Assert.assertTrue(propertiesFiles.containsKey(countFile));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateOnePersistentCounterNoPropertyCreation() throws Exception
    {
        final File countFile = File.createTempFile("test", null);
        countFile.deleteOnExit();

        final TestMojo testMojo = new TestMojo(ImmutableList.of(new CountDefine("hello", countFile, 1, 1, false, true, null)));
        testMojo.loadDefines();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnePersistentCounterNoFileNoCreation() throws Exception
    {
        final File countFile = File.createTempFile("test", null);
        countFile.delete();

        final TestMojo testMojo = new TestMojo(ImmutableList.of(new CountDefine("hello", countFile, 1, 1, true, false, null)));
        testMojo.loadDefines();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOnePersistentCounterNoFileNoPropertyCreation() throws Exception
    {
        final File countFile = File.createTempFile("test", null);
        countFile.delete();

        final TestMojo testMojo = new TestMojo(ImmutableList.of(new CountDefine("hello", countFile, 1, 1, false, true, null)));
        testMojo.loadDefines();
    }



    public static class TestMojo extends AbstractNumbersMojo
    {
        TestMojo(final List<CountDefine> countDefines)
        {
            super.countDefines = countDefines;
        }

        @Override
        protected void doExecute() throws Exception
        {
        }

        private Map<File, Properties> getPropertiesFiles()
        {
            return super.propertiesFiles;
        }

        @Override
        public void loadDefines()
            throws Exception
        {
            super.loadDefines();
        }

    }
}