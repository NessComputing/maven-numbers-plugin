package com.likeness.maven.plugins.numbers;

import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

public class NumberGroup
{
    private List<NumberDefinition> numberDefinitions;

    private String name;



    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public List<NumberDefinition> getNumbers()
    {
        return numberDefinitions;
    }

    public void setNumbers(List<NumberDefinition> number)
    {
        this.numberDefinitions = number;
    }

    @Override
    public boolean equals(final Object other)
    {
        if (!(other instanceof NumberGroup))
            return false;
        NumberGroup castOther = (NumberGroup) other;
        return new EqualsBuilder().append(numberDefinitions, castOther.numberDefinitions).append(name, castOther.name).isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder().append(numberDefinitions).append(name).toHashCode();
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("numberDefines", numberDefinitions).append("name", name).toString();
    }
}
