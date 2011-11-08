package com.likeness.maven.plugins.numbers.beans;

import static java.lang.String.format;

import java.util.Locale;

import com.google.common.base.Preconditions;
import com.likeness.maven.plugins.numbers.util.Log;

public enum IWFCEnum
{
    IGNORE, WARN, FAIL, CREATE;

    private static final Log LOG = Log.findLog();

    public static IWFCEnum forString(final String value)
    {
        Preconditions.checkArgument(value != null, "the value can not be null");
        return valueOf(IWFCEnum.class, value.toUpperCase(Locale.ENGLISH));
    }

    /**
     * Reacts on a given thing existing or not existing.
     *
     * IGNORE: Do nothing.
     * WARN: Display a warning message if the thing does not exist, otherwise do nothing.
     * FAIL: Throws an exception if the thing does not exist.
     * CREATE: Suggest creation of the thing.
     *
     * Returns true if the thing should be create, false otherwise.
     */
    public static boolean checkState(final IWFCEnum iwfc, final boolean exists, final String thing)
    {
        if (exists || iwfc == IGNORE) {
            return false;
        }
        else if (iwfc == WARN) {
            LOG.warn("'%s' does not exist!", thing);
            return false;
        }
        else if (iwfc == FAIL) {
            throw new IllegalStateException(format("'%s' does not exist!", thing));
        }
        else if (iwfc == CREATE) {
            LOG.debug("'%s' does not exist, suggesting creation.", thing);
            return true;
        }
        else {
            return false;
        }
    }
}
