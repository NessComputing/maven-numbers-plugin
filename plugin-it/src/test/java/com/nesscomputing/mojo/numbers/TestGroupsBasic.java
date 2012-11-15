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
package com.nesscomputing.mojo.numbers;

import java.io.File;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.junit.Assert;
import org.junit.Test;

import com.nesscomputing.testing.lessio.AllowExternalProcess;
import com.nesscomputing.testing.lessio.AllowLocalFileAccess;

@AllowLocalFileAccess(paths= {"*"})
@AllowExternalProcess
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
        Assert.assertEquals("4.8.15.16-numbers-maven-plugin-it-19700101_000000", groupDemo);
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
        Assert.assertEquals("numbers-maven-plugin-it", str);
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

