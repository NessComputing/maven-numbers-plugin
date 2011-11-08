package com.likeness.maven.plugins.numbers;

import java.io.IOException;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;

import com.google.common.collect.Lists;
import com.likeness.maven.plugins.numbers.beans.NumberDefinition;
import com.likeness.maven.plugins.numbers.util.Log;


/**
 * Fetches the defined numbers and add properties.
 *
 * @goal get
 */
public class GetNumbersMojo extends AbstractNumbersMojo
{
    private final Log LOG = Log.findLog();

    private final PropertyCache propertyCache = new PropertyCache();

    protected void doExecute() throws Exception
    {
        LOG.debug("Running GetNumbers");
        final List<PropertyElement> propertyElements = Lists.newArrayList();
        final List<NumberField> numberFields = createNumbers(numbers);
        propertyElements.addAll(numberFields);

        for (PropertyElement pe : propertyElements) {
            if (pe.isExport()) {
                project.getProperties().setProperty(pe.getPropertyName(), pe.getPropertyValue());
                LOG.info("Exporting Property name: %s, value: %s", pe.getPropertyName(), pe.getPropertyValue());
            }
            else {
                LOG.info("Property name: %s, value: %s", pe.getPropertyName(), pe.getPropertyValue());
            }
        }
    }

    private List<NumberField> createNumbers(final NumberDefinition [] numberDefinitions)
        throws IOException
    {
        final List<NumberField> result = Lists.newArrayList();

        if (!ArrayUtils.isEmpty(numberDefinitions)) {
            for (NumberDefinition numberDefinition : numberDefinitions) {
                numberDefinition.check();
                final ValueProvider numberValue = propertyCache.getPropertyValue(numberDefinition);
                final NumberField numberField = new NumberField(numberDefinition, numberValue);
                result.add(numberField);
            }
        }
        return result;
    }
}
