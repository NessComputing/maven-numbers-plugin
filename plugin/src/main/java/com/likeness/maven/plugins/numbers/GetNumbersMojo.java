package com.likeness.maven.plugins.numbers;

import java.util.List;
import java.util.Map;

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
        final List<NumberDefinition> numberDefines = getNumberDefines();
        loadDefines(numberDefines);
        defineNumbers(numberDefines);

        for (Map.Entry<String, String> number : definedNumbers.entrySet()) {
            project.getProperties().setProperty(number.getKey(), number.getValue());
            LOG.debug("Setting number '%s' to %s.", number.getKey(), number.getValue());
        }
    }
}
