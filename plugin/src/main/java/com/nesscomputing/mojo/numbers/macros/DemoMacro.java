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
package com.nesscomputing.mojo.numbers.macros;

import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;

import com.nesscomputing.mojo.numbers.AbstractNumbersMojo;
import com.nesscomputing.mojo.numbers.ValueProvider;
import com.nesscomputing.mojo.numbers.beans.MacroDefinition;

/**
 * @plexus.component role="com.nesscomputing.mojo.numbers.macros.MacroType" role-hint="demo"
 */
public class DemoMacro implements MacroType
{
    @Override
    public String getValue(@Nonnull final MacroDefinition macroDefinition,
                           @Nonnull final ValueProvider valueProvider,
                           @Nonnull final AbstractNumbersMojo mojo)
    {
        Preconditions.checkState(mojo != null, "inserted mojo is null!");

        final String type = macroDefinition.getProperties().getProperty("type", "static");
        if ("static".equals(type)) {
            return "static-value";
        }
        else if ("property".equals(type)) {
            return valueProvider.getValue();
        }
        else {
            return macroDefinition.getProperties().getProperty("value");
        }
    }
}
