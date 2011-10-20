package com.likeness.maven.plugins.numbers;

import static java.lang.String.format;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
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
     * Count groups.
     * @parameter expression="${project.countGroups}"
     * @readonly
     */
    protected List<CountDefine> countDefines;

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
    protected final Map<String, String> counters = Maps.newHashMap();

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

    protected void loadDefines()
        throws Exception
    {
        // Load all defined properties files and make sure that the configuration
        // is valid.
        for (CountDefine countDefine : countDefines) {
            final String name = countDefine.getName();
            Preconditions.checkArgument(StringUtils.isNotBlank(name), "Empty or missing name for count definition found!");
            Preconditions.checkArgument(!counters.containsKey(name), "Counter '%s' was defined multiple times!", name);
            counters.put(name, "");
            locateProperties(countDefine);
        }
    }

    protected void createCounters(final List<CountDefine> countDefines)
        throws Exception
    {
        for (CountDefine countDefine : countDefines) {
            final String name = countDefine.getName();
            Preconditions.checkState("".equals(counters.get(name)), "Unknown Counter '%s' found!", name);

            final File propertiesFile = countDefine.getPropertiesFile();
            if (propertiesFile == null) {
                // Transient counters are set to 0.
                counters.put(name, Objects.firstNonNull(countDefine.getInitialValue(), "0"));
            }
            else {
                final Properties props = propertiesFiles.get(propertiesFile);
                counters.put(name, props.getProperty(name, Objects.firstNonNull(countDefine.getInitialValue(), "0")));
            }
        }
    }

    private void locateProperties(final CountDefine countDefine)
        throws Exception
    {
        final String name = countDefine.getName();

        final File propertiesFile = countDefine.getPropertiesFile();
        if (propertiesFile == null) {
            return; // no property file define. This is a transient counter.
        }

        if (!propertiesFiles.containsKey(propertiesFile)) {

            final boolean exists = propertiesFile.exists();
            if (!exists && !countDefine.isCreateFile()) {
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

            if (!propertyExists && !countDefine.isCreateProperty()) {
                throw new IllegalArgumentException(format("Property '%s' does not exist in properties file '%s' and creation is disabled!", name, propertiesFile));
            }
        }
    }

}
