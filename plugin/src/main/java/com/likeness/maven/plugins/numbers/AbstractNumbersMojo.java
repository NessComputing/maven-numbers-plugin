package com.likeness.maven.plugins.numbers;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.likeness.maven.plugins.numbers.beans.DateDefinition;
import com.likeness.maven.plugins.numbers.beans.MacroDefinition;
import com.likeness.maven.plugins.numbers.beans.NumberDefinition;
import com.likeness.maven.plugins.numbers.beans.PropertyGroup;
import com.likeness.maven.plugins.numbers.beans.StringDefinition;
import com.likeness.maven.plugins.numbers.util.Log;
import com.pyx4j.log4j.MavenLogAppender;

/**
 * Base code for all the mojos.
 */
public abstract class AbstractNumbersMojo extends AbstractMojo
{
    /**
     * The maven project (effective pom).
     * @parameter expression="${project}"
     * @required
     * @readonly
    */
    protected MavenProject project;

    /**
     * Skip the plugin execution.
     *
     * @parameter default-value="false"
     */
    protected boolean skip = false;

    /**
     * Default action on duplicate properties.
     *
     * @parameter default-value="fail"
     */
    protected String onDuplicateProperty = "fail";

    /**
     * Default action on missing properties.
     *
     * @parameter default-value="fail"
     */
    protected String onMissingProperty = "fail";

    /**
     * Which groups to activate for this plugin run.
     *
     * @parameter
     */
    protected String[] activeGroups;

    /**
     * Property groups.
     *
     * @parameter
     */
    protected PropertyGroup[] propertyGroups;

    /**
     * Numbers.
     *
     * @parameter
     * @name="numbers"
     */
    protected NumberDefinition[] numbers;

    /**
     * Strings.
     *
     * @parameter
     * @name="strings"
     */
    protected StringDefinition[] strings;

    /**
     * Dates.
     *
     * @parameter
     * @name="dates"
     */
    protected DateDefinition[] dates;

    /**
     * Macros.
     *
     * @parameter
     * @name="macros"
     */
    protected MacroDefinition[] macros;

    protected final Log LOG = Log.findLog();

    private final PropertyCache propertyCache = new PropertyCache();


    protected final Map<File, Properties> propertiesFiles = Maps.newHashMap();
    protected final Map<String, String> definedNumbers = Maps.newHashMap();

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        MavenLogAppender.startPluginLog(this);

        try {
            if (skip) {
                LOG.debug("Skipping execution!");
            }
            else {
                doExecute();
            }
        }
        catch (MojoExecutionException me) {
            throw me;
        }
        catch (MojoFailureException mfe) {
            throw mfe;
        }
        catch (Exception e) {
            throw new MojoExecutionException("While running mojo: ", e);
        }
        finally {
            LOG.debug("Ended %s mojo run!", this.getClass().getSimpleName());
            MavenLogAppender.endPluginLog(this);
        }
    }

    /**
     * Subclasses need to implement this method.
     */
    protected abstract void doExecute() throws Exception;

    protected List<PropertyElement> loadPropertyElements()
        throws Exception
    {
        final List<PropertyElement> propertyElements = Lists.newArrayList();
        propertyElements.addAll(createNumbers(numbers));
        propertyElements.addAll(createStrings(strings));
        propertyElements.addAll(createDates(dates));

        for (PropertyElement pe : propertyElements) {
            if (pe.isExport()) {
                final String value = pe.getPropertyValue();
                if (value != null) {
                    project.getProperties().setProperty(pe.getPropertyName(), value);
                    LOG.info("Exporting Property name: %s, value: %s", pe.getPropertyName(), value);
                }
            }
            else {
                LOG.info("Property name: %s, value: %s", pe.getPropertyName(), pe.getPropertyValue());
            }
        }


        return propertyElements;
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

    private List<StringField> createStrings(final StringDefinition[] stringDefinitions)
        throws IOException
    {
        final List<StringField> result = Lists.newArrayList();

        if (!ArrayUtils.isEmpty(stringDefinitions)) {
            for (StringDefinition stringDefinition : stringDefinitions) {
                stringDefinition.check();
                final ValueProvider stringValue = propertyCache.getPropertyValue(stringDefinition);
                final StringField stringField = new StringField(stringDefinition, stringValue);
                result.add(stringField);
            }
        }
        return result;
    }

    private List<DateField> createDates(final DateDefinition[] dateDefinitions)
        throws IOException
    {
        final List<DateField> result = Lists.newArrayList();

        if (!ArrayUtils.isEmpty(dateDefinitions)) {
            for (DateDefinition dateDefinition : dateDefinitions) {
                dateDefinition.check();
                final ValueProvider dateValue = propertyCache.getPropertyValue(dateDefinition);
                final DateField dateField = new DateField(dateDefinition, dateValue);
                result.add(dateField);
            }
        }
        return result;
    }

    private List<MacroField> createMacros(final MacroDefinition[] macroDefinitions)
        throws IOException
    {
        final List<MacroField> result = Lists.newArrayList();

        if (!ArrayUtils.isEmpty(macroDefinitions)) {
            for (MacroDefinition macroDefinition : macroDefinitions) {
                macroDefinition.check();
                final ValueProvider macroValue = propertyCache.getPropertyValue(macroDefinition);
                final MacroField macroField = new MacroField(macroDefinition, macroValue);
                result.add(macroField);
            }
        }
        return result;
    }
}
