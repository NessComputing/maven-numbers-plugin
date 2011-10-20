package com.likeness.maven.plugins.numbers;

import static java.lang.String.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.common.io.Closeables;
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
     * Number groups.
     * @parameter expression="${project.numberGroups}"
     * @readonly
     */
    protected List<NumberGroup> numberGroups;

    /**
     * Skip the plugin execution.
     *
     * @parameter default-value="false"
     */
    protected boolean skip = false;

    /**
     * Groups to activate.
     *
     * @parameter default-value=""
     */
    protected String activation = "";

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

    protected List<NumberDefinition> getNumberDefines()
    {
        final String [] activeGroups = StringUtils.stripAll(StringUtils.split(activation));

        LOG.debug("Active groups: %s", (Object) activeGroups);

        final List<NumberDefinition> numbers = Lists.newArrayList();
        final Set<String> groupNames = Sets.newHashSet();

        for (final NumberGroup numberGroup : numberGroups) {
            final String name = StringUtils.trimToEmpty(numberGroup.getName());
            Preconditions.checkArgument(StringUtils.isNotBlank(name), "Empty or missing id for number group found!");
            Preconditions.checkArgument(!groupNames.contains(name), "Number group '%s' was defined multiple times!", name);
            groupNames.add(name);
            for (String activeGroup : activeGroups) {
                if (StringUtils.equals(activeGroup, name)) {
                    numbers.addAll(numberGroup.getNumbers());
                }
            }
        }
        LOG.debug("Found %d numbers", numbers.size());
        return numbers;
    }

    protected void loadDefines(final List<NumberDefinition> numbers)
        throws Exception
    {
        // Load all defined properties files and make sure that the configuration
        // is valid.
        for (NumberDefinition number : numbers) {
            final String name = number.getName();
            Preconditions.checkArgument(StringUtils.isNotBlank(name), "Empty or missing name for number definition found!");
            Preconditions.checkArgument(!definedNumbers.containsKey(name), "Number '%s' was defined multiple times!", name);
            definedNumbers.put(name, "");
            locateProperties(number);
        }
    }

    protected void defineNumbers(final List<NumberDefinition> numberDefines)
        throws Exception
    {
        for (NumberDefinition numberDefine : numberDefines) {
            final String name = numberDefine.getName();
            Preconditions.checkState("".equals(definedNumbers.get(name)), "Unknown Number '%s' found!", name);

            final File propertiesFile = numberDefine.getPropertiesFile();
            if (propertiesFile == null) {
                // Transient numbers are set to 0.
                definedNumbers.put(name, Objects.firstNonNull(numberDefine.getInitialValue(), "0"));
            }
            else {
                final Properties props = propertiesFiles.get(propertiesFile);
                definedNumbers.put(name, props.getProperty(name, Objects.firstNonNull(numberDefine.getInitialValue(), "0")));
            }
        }
    }

    private void locateProperties(final NumberDefinition numberDefine)
        throws Exception
    {
        final String name = numberDefine.getName();

        final File propertiesFile = numberDefine.getPropertiesFile();
        if (propertiesFile == null) {
            return; // no property file define. This is a transient number.
        }

        if (!propertiesFiles.containsKey(propertiesFile)) {

            final boolean exists = propertiesFile.exists();
            if (!exists && !numberDefine.isCreateFile()) {
                throw new IllegalArgumentException(format("Properties file '%s' must exist for property '%s'!", propertiesFile, name));
            }

            if (exists) {
                if (!propertiesFile.canRead() || !propertiesFile.isFile()) {
                    throw new IllegalStateException(format("Properties file '%s' exists but is not readable or not a file!", propertiesFile));
                }

                final Properties props = new Properties();
                InputStream stream = null;
                try {
                    stream = new FileInputStream(propertiesFile);
                    props.load(stream);
                }
                finally {
                    Closeables.closeQuietly(stream);
                }

                propertiesFiles.put(propertiesFile, props);
                LOG.debug("Loaded property file '%s'", propertiesFile);
            }

            boolean propertyExists = propertiesFiles.containsKey(propertiesFile) && propertiesFiles.get(propertiesFile).containsKey(name);

            if (!propertyExists && !numberDefine.isCreateProperty()) {
                throw new IllegalArgumentException(format("Property '%s' does not exist in properties file '%s' and creation is disabled!", name, propertiesFile));
            }
        }
    }

}
