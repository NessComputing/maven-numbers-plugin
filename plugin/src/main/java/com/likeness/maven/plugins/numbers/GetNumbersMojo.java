package com.likeness.maven.plugins.numbers;

import com.likeness.maven.plugins.numbers.util.Log;


/**
 * Fetches the defined numbers and add properties.
 *
 * @goal get
 */
public class GetNumbersMojo extends AbstractNumbersMojo
{
    private final Log LOG = Log.findLog();

    protected void doExecute() throws Exception
    {
        LOG.debug("Running GetNumbers");
    }
}
