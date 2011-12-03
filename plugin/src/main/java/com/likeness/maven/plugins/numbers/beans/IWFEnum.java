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
package com.likeness.maven.plugins.numbers.beans;

import static java.lang.String.format;

import java.util.Locale;

import com.google.common.base.Preconditions;
import com.likeness.maven.plugins.numbers.util.Log;

public enum IWFEnum
{
    IGNORE, WARN, FAIL;

    private static final Log LOG  = Log.findLog();

    public static IWFEnum forString(final String value)
    {
        Preconditions.checkNotNull(value, "the value can not be null");
        return valueOf(IWFEnum.class, value.toUpperCase(Locale.ENGLISH));
    }

    /**
     * Reacts on a given thing existing or not existing.
     *
     * IGNORE: Do nothing.
     * WARN: Display a warning message if the thing does not exist, otherwise do nothing.
     * FAIL: Throws an exception if the thing does not exist.
     *
     * Returns true if the thing should be create, false otherwise.
     */
    public static void checkState(final IWFEnum iwf, final boolean exists, final String thing)
    {
        if (exists || iwf == IGNORE) {
            return;
        }
        else if (iwf == WARN) {
            LOG.warn("'%s' does not exist!", thing);
        }
        else if (iwf == FAIL) {
            throw new IllegalStateException(format("'%s' does not exist!", thing));
        }
    }
}
