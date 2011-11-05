package com.likeness.maven.plugins.numbers;

import static java.lang.String.format;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.likeness.maven.plugins.numbers.beans.NumberDefinition;

public class NumberField implements PropertyElement
{
    private static final Pattern MATCH_GROUPS = Pattern.compile("\\d+|[^\\d]+");

    private final NumberDefinition numberDefinition;

    private final List<String> elements = Lists.newArrayList();
    private final List<Pair<Integer, Long>> numberElements;

    public NumberField(final NumberDefinition numberDefinition, final String value)
    {
        this.numberDefinition = numberDefinition;

        if (value != null) {
            numberElements = Lists.newArrayList();
            final Matcher m = MATCH_GROUPS.matcher(value);

            while (m.find()) {
                final String matchValue = m.group();
                elements.add(matchValue);
                if (isNumber(matchValue)) {
                    final Pair<Integer,Long> numberValue = Pair.of(elements.size() - 1, Long.parseLong(value));
                    numberElements.add(numberValue);
                }
            }

            Preconditions.checkState(numberElements.size() > numberDefinition.getFieldNumber(), format("Only %d fields in %s, field %d requested.", numberElements.size(), value, numberDefinition.getFieldNumber()));
        }
        else {
            numberElements = null;
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
        return numberElements == null ? null : numberElements.get(numberDefinition.getFieldNumber()).getValue();
    }

    @Override
    public String getPropertyName()
    {
        return numberDefinition.getPropertyName();
    }

    @Override
    public String getPropertyValue()
    {
        return numberElements == null ? null : numberElements.get(numberDefinition.getFieldNumber()).getValue().toString();
    }
}
