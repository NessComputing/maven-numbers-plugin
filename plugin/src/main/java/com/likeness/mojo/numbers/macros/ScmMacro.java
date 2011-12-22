/**
 * Copyright (C) 2011 Ness Computing, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.likeness.maven.plugins.numbers.macros;

import java.util.List;
import java.util.Properties;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.maven.model.Scm;
import org.apache.maven.project.MavenProject;
import org.apache.maven.scm.ChangeSet;
import org.apache.maven.scm.ScmBranch;
import org.apache.maven.scm.ScmException;
import org.apache.maven.scm.ScmFileSet;
import org.apache.maven.scm.ScmRevision;
import org.apache.maven.scm.ScmTag;
import org.apache.maven.scm.ScmVersion;
import org.apache.maven.scm.command.changelog.ChangeLogScmResult;
import org.apache.maven.scm.manager.ScmManager;
import org.apache.maven.scm.provider.ScmProviderRepositoryWithHost;
import org.apache.maven.scm.repository.ScmRepository;
import org.apache.maven.scm.repository.ScmRepositoryException;
import org.apache.maven.settings.Server;
import org.apache.maven.settings.Settings;

import com.google.common.base.Preconditions;
import com.likeness.maven.plugins.numbers.AbstractNumbersMojo;
import com.likeness.maven.plugins.numbers.ValueProvider;
import com.likeness.maven.plugins.numbers.beans.IWFCEnum;
import com.likeness.maven.plugins.numbers.beans.MacroDefinition;
import com.likeness.maven.plugins.numbers.util.Log;

/**
 * Retrieves revisions from the underlying SCM. The SCM API of Maven is not great and has tons of bugs, so this probably only works
 * somewhat reliably with git and semi-reliably (once SCM-651 is applied) with Mercurial. Any additional testing or patches are welcome.
 *
 * Properties:
 *   requireDeveloperConnection  (true/false) - whether to use the developerConnection or the normal connection from the project pom.
 *   revision - fetch a revision
 *   branch - fetch or limit to a branch
 *   tag - fetch a tag
 *
 * @plexus.component role="com.likeness.maven.plugins.numbers.macros.MacroType" role-hint="scm"
 */
public class ScmMacro implements MacroType
{
    private static final Log LOG = Log.findLog();

    /**
     * SCM manager.
     *
     * @plexus.requirement
     */
    public ScmManager scmManager = null;   // yes, jason, this *is* your fault.

    @Override
    public String getValue(@Nonnull final MacroDefinition macroDefinition,
                           @Nonnull final ValueProvider valueProvider,
                           @Nonnull final AbstractNumbersMojo mojo) throws Exception
    {
        Preconditions.checkState(mojo != null, "inserted mojo is null!");
        final Properties props = macroDefinition.getProperties();
        final ScmRepository scmRepo = getScmRepository(mojo.getProject(), mojo.getSettings(), props);


        final String revision = props.getProperty("revision");
        final String branch = props.getProperty("branch");
        final String tag = props.getProperty("tag");

        final ScmRevision scmRevision = (revision == null) ? null : new ScmRevision(revision);
        final ScmBranch scmBranch = (branch == null) ? null : new ScmBranch(branch);
        final ScmTag scmTag = (tag == null) ? null : new ScmTag(tag);

        final ScmFileSet fileSet = new ScmFileSet(mojo.getBasedir());
        final ChangeLogScmResult result;

        final String rev;
        if (branch != null) {
            rev = scmBranch.getName();
            result = scmManager.changeLog(scmRepo,  fileSet, null, null, 0, scmBranch);
        }
        else {
            final ScmVersion scmVersion = ObjectUtils.defaultIfNull(scmTag, scmRevision);
            rev = (scmVersion != null) ? scmVersion.getName(): "<unset>";
            result = scmManager.changeLog(scmRepo, fileSet, null, scmVersion);
        }

        IWFCEnum.checkState(macroDefinition.getOnMissingProperty(), result.isSuccess(), "scm revision '" + rev + "'");

        if (!result.isSuccess()) {
            return "";
        }

        final List<ChangeSet> changeSets = result.getChangeLog().getChangeSets();
        Preconditions.checkState(changeSets != null && changeSets.size() > 0, "no change sets found!");
        return changeSets.get(0).getRevision();
    }

    public ScmRepository getScmRepository(final MavenProject project, final Settings settings, final Properties props) throws ScmException
    {
        final Scm scm = project.getScm();

        Preconditions.checkState(scm != null, "No scm element in the project found!");

        ScmRepository repository;

        try {
            repository = scmManager.makeScmRepository(getConnectionUrl(scm, props));

            if (repository.getProviderRepository() instanceof ScmProviderRepositoryWithHost) {
                ScmProviderRepositoryWithHost repo = (ScmProviderRepositoryWithHost) repository.getProviderRepository();
                final Server server = settings.getServer(repo.getHost());

                if ( server != null )
                {
                    repo.setUser(server.getUsername());
                    repo.setPassword(server.getPassword());
                    repo.setPrivateKey(server.getPrivateKey());
                    repo.setPassphrase(server.getPassphrase());
                }
            }
        }
        catch (ScmRepositoryException e) {
            if (!e.getValidationMessages().isEmpty()) {
                for (String msg : e.getValidationMessages()) {
                    LOG.error(msg);
                }
            }

            throw new ScmException("Can't load the scm provider.", e);
        }
        catch (Exception e) {
            throw new ScmException("Can't load the scm provider.", e);
        }

        return repository;
    }


    public String getConnectionUrl(final Scm scm, final Properties props)
    {
        final boolean requireDeveloperConnection = BooleanUtils.toBoolean(props.getProperty("requireDeveloperConnection"));

        if (StringUtils.isNotBlank(scm.getConnection()) && !requireDeveloperConnection) {
            final String url = ObjectUtils.toString(scm.getConnection(), scm.getDeveloperConnection());
            Preconditions.checkState(url != null, "no scm url found!");
            return url;
        }

        final String url = scm.getDeveloperConnection();
        Preconditions.checkState(url != null, "no scm developer url found!");
        return url;
    }
}
