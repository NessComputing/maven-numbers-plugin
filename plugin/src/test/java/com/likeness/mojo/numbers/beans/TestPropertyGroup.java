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
package com.likeness.mojo.numbers.beans;

import java.util.Collections;
import java.util.List;
import java.util.Properties;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.likeness.mojo.numbers.beans.IWFEnum;
import com.likeness.mojo.numbers.beans.PropertyGroup;

public class TestPropertyGroup
{
    @Test
    public void testConstant()
    {
        final Properties props = new Properties();
        props.setProperty("hello", "world");

        PropertyGroup pg = new PropertyGroup("hello", true, true, IWFEnum.FAIL, IWFEnum.FAIL, props);

        final List<String> propNames = Lists.newArrayList(pg.getPropertyNames());
        Assert.assertEquals(1, propNames.size());
        Assert.assertEquals("hello", propNames.get(0));

        final String propValue = pg.getPropertyValue("hello", Collections.<String, String>emptyMap());
        Assert.assertEquals("world", propValue);
    }

    @Test
    public void testRenderSingle()
    {
        final Properties props = new Properties();
        props.setProperty("hello", "#{world}");

        PropertyGroup pg = new PropertyGroup("hello", true, true, IWFEnum.FAIL, IWFEnum.FAIL, props);

        final List<String> propNames = Lists.newArrayList(pg.getPropertyNames());
        Assert.assertEquals(1, propNames.size());
        Assert.assertEquals("hello", propNames.get(0));

        final String propValue = pg.getPropertyValue("hello", ImmutableMap.of("world", "pizza"));
        Assert.assertEquals("pizza", propValue);
    }

    @Test(expected=IllegalStateException.class)
    public void testRenderEmptyFail()
    {
        final Properties props = new Properties();
        props.setProperty("hello", "#{world}");

        PropertyGroup pg = new PropertyGroup("hello", true, true, IWFEnum.FAIL, IWFEnum.FAIL, props);

        final List<String> propNames = Lists.newArrayList(pg.getPropertyNames());
        Assert.assertEquals(1, propNames.size());
        Assert.assertEquals("hello", propNames.get(0));

        final String propValue = pg.getPropertyValue("hello", Collections.<String, String>emptyMap());
        Assert.assertEquals("", propValue);
    }

    @Test
    public void testRenderEmptyOk()
    {
        final Properties props = new Properties();
        props.setProperty("hello", "nice-#{world}-hat");

        PropertyGroup pg = new PropertyGroup("hello", true, true, IWFEnum.FAIL, IWFEnum.IGNORE, props);

        final List<String> propNames = Lists.newArrayList(pg.getPropertyNames());
        Assert.assertEquals(1, propNames.size());
        Assert.assertEquals("hello", propNames.get(0));

        final String propValue = pg.getPropertyValue("hello", Collections.<String, String>emptyMap());
        Assert.assertEquals("nice--hat", propValue);
    }

    @Test
    public void testRenderDotsAreCool()
    {
        final Properties props = new Properties();
        props.setProperty("hello", "nice-#{foo.bar.world}-hat");

        PropertyGroup pg = new PropertyGroup("hello", true, true, IWFEnum.FAIL, IWFEnum.IGNORE, props);

        final List<String> propNames = Lists.newArrayList(pg.getPropertyNames());
        Assert.assertEquals(1, propNames.size());
        Assert.assertEquals("hello", propNames.get(0));

        final String propValue = pg.getPropertyValue("hello", ImmutableMap.of("foo.bar.world", "strange"));
        Assert.assertEquals("nice-strange-hat", propValue);
    }

}
