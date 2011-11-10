package com.likeness.maven.plugins.numbers;

public interface PropertyElement
{
    String getPropertyName();

    String getPropertyValue() throws Exception;

    boolean isExport();
}
