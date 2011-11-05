package com.likeness.maven.plugins.numbers;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.google.common.collect.Maps;
import com.likeness.maven.plugins.numbers.beans.DateDefinition;
import com.likeness.maven.plugins.numbers.beans.MacroDefinition;
import com.likeness.maven.plugins.numbers.beans.NumberDefinition;
import com.likeness.maven.plugins.numbers.beans.PropertyGroup;
import com.likeness.maven.plugins.numbers.beans.StringDefinition;
import com.likeness.maven.plugins.numbers.util.Log;
import com.pyx4j.log4j.MavenLogAppender;

import java.io.File;
import java.util.Map;
import java.util.Properties;

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
}
