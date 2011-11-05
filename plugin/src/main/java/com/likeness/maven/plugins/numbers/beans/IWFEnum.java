package com.likeness.maven.plugins.numbers.beans;

import com.google.common.base.Preconditions;

import java.util.Locale;

public enum IWFEnum
{
    IGNORE, WARN, FAIL;

    public static IWFEnum forString(final String value)
    {
        Preconditions.checkNotNull(value, "the value can not be null");
        return valueOf(IWFEnum.class, value.toUpperCase(Locale.ENGLISH));
    }
}
