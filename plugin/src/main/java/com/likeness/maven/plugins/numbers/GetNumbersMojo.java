package com.likeness.maven.plugins.numbers;


/**
 * Fetches the defined numbers and add properties.
 *
 * @goal get
 */
public class GetNumbersMojo extends AbstractNumbersMojo
{
    protected void doExecute() throws Exception
    {
        loadDefines();
        createCounters(countDefines);
    }
}
