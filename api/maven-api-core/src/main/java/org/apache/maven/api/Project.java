/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.api;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.maven.api.annotations.Experimental;
import org.apache.maven.api.annotations.Nonnull;
import org.apache.maven.api.model.Build;
import org.apache.maven.api.model.Model;

/**
 * Interface representing a Maven project.
 * Projects can be built using the {@link org.apache.maven.api.services.ProjectBuilder} service.
 *
 * @since 4.0.0
 */
@Experimental
public interface Project {

    @Nonnull
    String getGroupId();

    @Nonnull
    String getArtifactId();

    @Nonnull
    String getVersion();

    @Nonnull
    String getPackaging();

    @Nonnull
    Artifact getArtifact();

    @Nonnull
    Model getModel();

    @Nonnull
    default Build getBuild() {
        Build build = getModel().getBuild();
        return build != null ? build : Build.newInstance();
    }

    /**
     * Returns the path to the pom file for this project.
     * A project is usually read from the file system and this will point to
     * the file.  In some cases, a transient project can be created which
     * will not point to an actual pom file.
     * @return the path of the pom
     */
    @Nonnull
    Optional<Path> getPomPath();

    @Nonnull
    default Optional<Path> getBasedir() {
        return getPomPath().map(Path::getParent);
    }

    @Nonnull
    List<DependencyCoordinate> getDependencies();

    @Nonnull
    List<DependencyCoordinate> getManagedDependencies();

    @Nonnull
    default String getId() {
        return getModel().getId();
    }

    /**
     * @deprecated use {@link #isTopProject()} instead
     */
    @Deprecated
    boolean isExecutionRoot();

    /**
     * Returns a boolean indicating if the project is the top level project for
     * this reactor build.  The top level project may be different from the
     * {@code rootDirectory}, especially if a subtree of the project is being
     * built, either because Maven has been launched in a subdirectory or using
     * a {@code -f} option.
     *
     * @return {@code true} if the project is the top level project for this build
     */
    boolean isTopProject();

    /**
     * Returns a boolean indicating if the project is a root project,
     * meaning that the {@link #getRootDirectory()} and {@link #getBasedir()}
     * points to the same directory, and that either {@link Model#isRoot()}
     * is {@code true} or that {@code basedir} contains a {@code .mvn} child
     * directory.
     *
     * @return {@code true} if the project is the root project
     * @see Model#isRoot()
     */
    boolean isRootProject();

    /**
     * Gets the root directory of the project, which is the parent directory
     * containing the {@code .mvn} directory or flagged with {@code root="true"}.
     *
     * @throws IllegalStateException if the root directory could not be found
     * @see Session#getRootDirectory()
     */
    @Nonnull
    Path getRootDirectory();

    @Nonnull
    Optional<Project> getParent();

    @Nonnull
    List<RemoteRepository> getRemoteProjectRepositories();

    @Nonnull
    List<RemoteRepository> getRemotePluginRepositories();

    /**
     * Returns the project properties as immutable map.
     *
     * @see org.apache.maven.api.services.ProjectManager#setProperty(Project, String, String)
     */
    @Nonnull
    Map<String, String> getProperties();
}
