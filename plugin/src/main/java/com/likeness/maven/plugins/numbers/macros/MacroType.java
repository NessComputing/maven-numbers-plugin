package com.likeness.maven.plugins.numbers.macros;

import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.likeness.maven.plugins.numbers.ValueProvider;
import com.likeness.maven.plugins.numbers.beans.MacroDefinition;

public interface MacroType
{
    Map<String, ? extends MacroType> EXISTING_MACROS = ImmutableMap.of("demo", new DemoMacro());

    String getValue(MacroDefinition macroDefinition, ValueProvider valueProvider);
}
