package com.likeness.maven.plugins.numbers;

import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.google.common.base.Objects;
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
    private final Map<String, String> props = Maps.newHashMap();
    private final List<PropertyElement> propertyElements = Lists.newArrayList();

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        MavenLogAppender.startPluginLog(this);

        try {
            if (skip) {
                LOG.debug("Skipping execution!");
            }
            else {
                loadPropertyElements();
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

    protected void loadPropertyElements()
        throws Exception
    {
        propertyElements.addAll(NumberField.createNumbers(propertyCache, numbers));
        propertyElements.addAll(StringField.createStrings(propertyCache, strings));
        propertyElements.addAll(DateField.createDates(propertyCache, dates));
        propertyElements.addAll(MacroField.createMacros(propertyCache, macros));

        for (final PropertyElement pe : propertyElements) {
            final String value = pe.getPropertyValue();
            props.put(pe.getPropertyName(), value);

            if (pe.isExport()) {
                project.getProperties().setProperty(pe.getPropertyName(), Objects.firstNonNull(value, ""));
                LOG.debug("Exporting Property name: %s, value: %s", pe.getPropertyName(), value);
            }
            else {
                LOG.debug("Property name: %s, value: %s", pe.getPropertyName(), Objects.firstNonNull(value, "<null>"));
            }
        }

        final List<PropertyField> propertyFields = Lists.newArrayList();
    }
}
