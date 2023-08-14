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
package com.paywholesail.mojo.numbers;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.project.MavenProject;
import org.apache.maven.settings.Settings;
import org.codehaus.plexus.PlexusConstants;
import org.codehaus.plexus.PlexusContainer;
import org.codehaus.plexus.context.Context;
import org.codehaus.plexus.context.ContextException;
import org.codehaus.plexus.personality.plexus.lifecycle.phase.Contextualizable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.paywholesail.mojo.numbers.beans.DateDefinition;
import com.paywholesail.mojo.numbers.beans.IWFEnum;
import com.paywholesail.mojo.numbers.beans.MacroDefinition;
import com.paywholesail.mojo.numbers.beans.NumberDefinition;
import com.paywholesail.mojo.numbers.beans.PropertyGroup;
import com.paywholesail.mojo.numbers.beans.StringDefinition;

/**
 * Base code for all the mojos.
 */
public abstract class AbstractNumbersMojo extends AbstractMojo implements Contextualizable
{
    /**
     * The maven project (effective pom).
     * @parameter property="project"
     * @required
     * @readonly
    */
    protected MavenProject project;

    /**
     * @parameter property="settings"
     * @required
     * @readonly
     */
    protected Settings settings;

    /**
     * @parameter property="basedir"
     * @required
     * @readonly
     */
    protected File basedir;

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

    protected final Logger LOG = LoggerFactory.getLogger(getClass());

    protected final PropertyCache propertyCache = new PropertyCache();
    private final Map<String, String> props = Maps.newHashMap();

    protected List<NumberField> numberFields = null;

    private PlexusContainer container = null;

    private boolean isSnapshot;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        isSnapshot = project.getArtifact().isSnapshot();
        LOG.debug("Project is a {}.", isSnapshot ? "snapshot" : "release");
        LOG.trace("{} on duplicate, {} on missing", onDuplicateProperty, onMissingProperty);

        try {
            if (skip) {
                LOG.debug("Skipping execution!");
            }
            else {
                doExecute();
            }
        }
        catch (final MojoExecutionException me) {
            throw me;
        }
        catch (final MojoFailureException mfe) {
            throw mfe;
        }
        catch (final Exception e) {
            throw new MojoExecutionException("While running mojo: ", e);
        }
        finally {
            LOG.debug("Ended {} mojo run!", this.getClass().getSimpleName());
        }
    }

    public MavenProject getProject()
    {
        Preconditions.checkNotNull(project);
        return project;
    }

    public Settings getSettings()
    {
        Preconditions.checkNotNull(settings);
        return settings;
    }

    public File getBasedir()
    {
        Preconditions.checkNotNull(basedir);
        return basedir;
    }

    public PlexusContainer getContainer()
    {
        Preconditions.checkNotNull(container);
        return container;
    }

    @Override
    public void contextualize(final Context context) throws ContextException
    {
        this.container = (PlexusContainer) context.get(PlexusConstants.PLEXUS_KEY);
    }

    /**
     * Subclasses need to implement this method.
     */
    protected abstract void doExecute() throws Exception;

    protected void loadPropertyElements()
        throws Exception
    {
        final List<PropertyElement> propertyElements = Lists.newArrayList();

        numberFields = NumberField.createNumbers(propertyCache, numbers);
        propertyElements.addAll(numberFields);
        propertyElements.addAll(StringField.createStrings(propertyCache, strings));
        propertyElements.addAll(DateField.createDates(propertyCache, dates));
        propertyElements.addAll(MacroField.createMacros(propertyCache, macros, this));

        for (final PropertyElement pe : propertyElements) {
            final String value = pe.getPropertyValue();
            props.put(pe.getPropertyName(), value);

            if (pe.isExport()) {
                project.getProperties().setProperty(pe.getPropertyName(), MoreObjects.firstNonNull(value, ""));
                LOG.debug("Exporting Property name: {}, value: {}", pe.getPropertyName(), value);
            }
            else {
                LOG.debug("Property name: {}, value: {}", pe.getPropertyName(), MoreObjects.firstNonNull(value, "<null>"));
            }
        }

        // Now generate the property groups.
        final Map<String, Pair<PropertyGroup, List<PropertyElement>>> propertyPairs = Maps.newHashMap();

        final Set<String> propertyNames = Sets.newHashSet();


        if (propertyGroups != null) {
            for (final PropertyGroup propertyGroup : propertyGroups) {
                final List<PropertyElement> propertyFields = PropertyField.createProperties(props, propertyGroup);
                propertyPairs.put(propertyGroup.getId(), Pair.of(propertyGroup, propertyFields));
            }
        }

        if (activeGroups != null) {
            for (final String activeGroup : activeGroups) {
                final Pair<PropertyGroup, List<PropertyElement>> propertyElement = propertyPairs.get(activeGroup);
                Preconditions.checkState(propertyElement != null, "activated group '%s' does not exist", activeGroup);

                final PropertyGroup propertyGroup = propertyElement.getLeft();
                if ((propertyGroup.isActiveOnRelease() && !isSnapshot) || (propertyGroup.isActiveOnSnapshot() && isSnapshot)) {
                    for (final PropertyElement pe : propertyElement.getRight()) {
                        final String value = pe.getPropertyValue();
                        final String propertyName = pe.getPropertyName();
                        IWFEnum.checkState(propertyGroup.getOnDuplicateProperty(), !propertyNames.contains(propertyName), "property name '" + propertyName + "'");
                        propertyNames.add(propertyName);

                        project.getProperties().setProperty(propertyName, MoreObjects.firstNonNull(value, ""));
                    }
                }
                else {
                    LOG.debug("Skipping property group {}: Snapshot: {}, onSnapshot: {}, onRelease: {}", activeGroup, isSnapshot, propertyGroup.isActiveOnSnapshot(), propertyGroup.isActiveOnRelease());
                }
            }
        }
    }
}
