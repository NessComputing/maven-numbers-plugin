package com.likeness.maven.plugins.numbers;



/**
 * Fetches the defined numbers and add properties.
 *
 * @goal get
 */
public class GetNumbersMojo extends AbstractNumbersMojo
{
    /**
     * Persist the properties.
     *
     * @parameter default-value="false"
     */
    protected boolean persist = false;


    protected void doExecute() throws Exception
    {
        LOG.debug("Running GetNumbers");

        loadPropertyElements();

        if (persist) {
            // Now dump the property cache back to the files if necessary.
            propertyCache.persist();
        }
    }
}
