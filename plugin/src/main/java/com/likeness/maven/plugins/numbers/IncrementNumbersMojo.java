package com.likeness.maven.plugins.numbers;

/**
 * Fetches the defined numbers, adds properties and increments all numbers.
 *
 * @goal inc
 */
public class IncrementNumbersMojo extends AbstractNumbersMojo
{
    /**
     * Persist the properties.
     *
     * @parameter default-value="true"
     */
    protected boolean persist = true;

    protected void doExecute() throws Exception
    {
        LOG.debug("Running GetNumbers");

        loadPropertyElements();

        if (numberFields != null) {
            for (NumberField nf : numberFields) {
                nf.increment();
            }
        }

        if (persist) {
            propertyCache.persist();
        }
    }
}
