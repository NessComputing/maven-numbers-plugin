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
package com.likeness.mojo.numbers;

import java.io.File;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.junit.Assert;
import org.junit.Test;

/**
 * Tests basic functionality of the Number get mojo. This uses the pom.xml file in src/test/resources/basic-get and a lot of the
 * configuration there is expected in this test.
 *
 */
public class TestNumbersBasic
{
    @Test
    public void testNumber1() throws Exception
    {
        final File jarLocation = TestUtils.runPackageGoal("basic", "number1");

        final JarFile jarFile = new JarFile(jarLocation);
        final Manifest manifest = jarFile.getManifest();
        final Attributes attributes = manifest.getMainAttributes();

        final String version = attributes.getValue("my-version");
        final String versionMaj = attributes.getValue("my-version-major");
        final String versionMin = attributes.getValue("my-version-minor");
        final String versionRev = attributes.getValue("my-version-rev");

        Assert.assertNotNull(version);
        Assert.assertNotNull(versionMaj);
        Assert.assertNotNull(versionMin);
        Assert.assertNotNull(versionRev);

        Assert.assertEquals("1.2.3", version);
        Assert.assertEquals("1.2.3", versionMaj);
        Assert.assertEquals("1.2.3", versionMin);
        Assert.assertEquals("1.2.3", versionRev);
    }

    @Test
    public void testNumber2() throws Exception
    {
        final File jarLocation = TestUtils.runPackageGoal("missing", "number2");

        final JarFile jarFile = new JarFile(jarLocation);
        final Manifest manifest = jarFile.getManifest();
        final Attributes attributes = manifest.getMainAttributes();

        final String version = attributes.getValue("my-version");
        final String missing = attributes.getValue("my-missing");

        Assert.assertNotNull(version);
        Assert.assertNotNull(missing);

        Assert.assertEquals("1.2.3", version);
        Assert.assertEquals("", missing);
    }

    @Test
    public void testNumber3() throws Exception
    {
        final File jarLocation = TestUtils.runPackageGoal("props", "number3");

        final JarFile jarFile = new JarFile(jarLocation);
        final Manifest manifest = jarFile.getManifest();
        final Attributes attributes = manifest.getMainAttributes();

        final String version = attributes.getValue("my-version");
        Assert.assertNotNull(version);
        Assert.assertEquals("2.8.0", version);
    }
}
