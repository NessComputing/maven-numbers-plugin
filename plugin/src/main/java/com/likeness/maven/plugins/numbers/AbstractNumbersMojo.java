package com.likeness.maven.plugins.numbers;

import java.util.List;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;

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
