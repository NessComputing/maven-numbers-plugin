package com.likeness.maven.plugins.numbers;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.likeness.maven.plugins.numbers.beans.NumberDefinition;
import org.apache.commons.lang3.StringUtils;

import static java.lang.String.format;

public class NumberField implements PropertyElement
{
    private static final Pattern MATCH_GROUPS = Pattern.compile("\\d+|[^\\d]+");

    private final NumberDefinition numberDefinition;

    private final List<String> elements = Lists.newArrayList();
    private final List<Integer> numberElements = Lists.newArrayList();

    public NumberField(final NumberDefinition numberDefinition, final String value)
    {
        this.numberDefinition = numberDefinition;

        if (value != null) {
            final Matcher m = MATCH_GROUPS.matcher(value);

            while (m.find()) {
                final String matchValue = m.group();
                elements.add(matchValue);
                if (isNumber(matchValue)) {
                    numberElements.add(elements.size() - 1);
                }
            }

            Preconditions.checkState(numberElements.size() > numberDefinition.getFieldNumber(), format("Only %d fields in %s, field %d requested.", numberElements.size(), value, numberDefinition.getFieldNumber()));
        }
    }

    private boolean isNumber(final CharSequence c) {
        for (int i = 0 ; i < c.length(); i++) {
            if (!Character.isDigit(c.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public Long getNumberValue()
    {
        String fieldValue = getPropertyValue();
        return fieldValue == null ? null : new Long(fieldValue);
    }

    @Override
    public String getPropertyName()
    {
        return numberDefinition.getPropertyName();
    }

    @Override
    public String getPropertyValue()
    {
        return numberElements.isEmpty() ? null : elements.get(numberElements.get(numberDefinition.getFieldNumber()));
    }

    @Override
    public String toString()
    {
        return StringUtils.join(elements, null);
    }
}
