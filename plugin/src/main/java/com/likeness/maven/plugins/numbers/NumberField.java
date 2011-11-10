package com.likeness.maven.plugins.numbers;

import static java.lang.String.format;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.likeness.maven.plugins.numbers.beans.NumberDefinition;

public class NumberField implements PropertyElement
{
    private static final Pattern MATCH_GROUPS = Pattern.compile("\\d+|[^\\d]+");

    private final NumberDefinition numberDefinition;
    private final ValueProvider valueProvider;

    private final List<String> elements = Lists.newArrayList();
    private final List<Integer> numberElements = Lists.newArrayList();

    public NumberField(final NumberDefinition numberDefinition, final ValueProvider valueProvider)
    {
        this.numberDefinition = numberDefinition;
        this.valueProvider = valueProvider;
    }

    @Override
    public String getPropertyName()
    {
        // This is not the property name (because many definitions can map onto one prop) 
        // but the actual id.
        return numberDefinition.getId();
    }

    @Override
    public String getPropertyValue()
    {
        parse();
        final String value = StringUtils.join(elements, null);
        final String format = numberDefinition.getFormat();
        return format == null ? value : format(format, value);
    }

    @Override
    public boolean isExport()
    {
        return numberDefinition.isExport();
    }

    private void parse()
    {
        final String value = valueProvider.getValue();

        final Matcher m = MATCH_GROUPS.matcher(value);
        elements.clear();
        numberElements.clear();

        while (m.find()) {
            final String matchValue = m.group();
            elements.add(matchValue);
            if (isNumber(matchValue)) {
                numberElements.add(elements.size() - 1);
            }
        }

        Preconditions.checkState(numberElements.size() > numberDefinition.getFieldNumber(), format("Only %d fields in %s, field %d requested.", numberElements.size(), value, numberDefinition.getFieldNumber()));
    }

    private boolean isNumber(final CharSequence c) {
        for (int i = 0 ; i < c.length(); i++) {
            if (!Character.isDigit(c.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public void increment()
    {
        final Long value = getNumberValue();
        if (value != null) {
            setNumberValue(value + numberDefinition.getIncrement());
        }
    }

    public void setNumberValue(final Long value)
    {
        parse();
        if (!numberElements.isEmpty()) {
            elements.set(numberElements.get(numberDefinition.getFieldNumber()), value.toString());
            valueProvider.setValue(StringUtils.join(elements, null));
        }
    }

    public Long getNumberValue()
    {
        parse();
        return numberElements.isEmpty() ? null : new Long(elements.get(numberElements.get(numberDefinition.getFieldNumber())));
    }

    @Override
    public String toString()
    {
        return getPropertyValue();
    }
}
