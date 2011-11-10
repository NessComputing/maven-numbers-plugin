package com.likeness.maven.plugins.numbers;

import java.util.List;


/**
 * Fetches the defined numbers and add properties.
 *
 * @goal get
 */
public class GetNumbersMojo extends AbstractNumbersMojo
{
    private final PropertyCache propertyCache = new PropertyCache();

    protected void doExecute() throws Exception
    {
        LOG.debug("Running GetNumbers");
        final List<PropertyElement> propertyElements = loadPropertyElements();
    }
}
