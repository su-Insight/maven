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
package org.apache.maven.repository.internal;

import org.apache.maven.repository.internal.scopes.MavenDependencyContextRefiner;
import org.apache.maven.repository.internal.scopes.MavenScopeDeriver;
import org.apache.maven.repository.internal.scopes.MavenScopeSelector;
import org.eclipse.aether.DefaultRepositorySystemSession;
import org.eclipse.aether.RepositorySystemSession.SessionBuilder;
import org.eclipse.aether.artifact.ArtifactTypeRegistry;
import org.eclipse.aether.artifact.DefaultArtifactType;
import org.eclipse.aether.collection.DependencyGraphTransformer;
import org.eclipse.aether.collection.DependencyManager;
import org.eclipse.aether.collection.DependencySelector;
import org.eclipse.aether.collection.DependencyTraverser;
import org.eclipse.aether.util.artifact.DefaultArtifactTypeRegistry;
import org.eclipse.aether.util.graph.manager.ClassicDependencyManager;
import org.eclipse.aether.util.graph.selector.AndDependencySelector;
import org.eclipse.aether.util.graph.selector.ExclusionDependencySelector;
import org.eclipse.aether.util.graph.selector.OptionalDependencySelector;
import org.eclipse.aether.util.graph.selector.ScopeDependencySelector;
import org.eclipse.aether.util.graph.transformer.ChainedDependencyGraphTransformer;
import org.eclipse.aether.util.graph.transformer.ConflictResolver;
import org.eclipse.aether.util.graph.transformer.NearestVersionSelector;
import org.eclipse.aether.util.graph.transformer.SimpleOptionalitySelector;
import org.eclipse.aether.util.repository.SimpleArtifactDescriptorPolicy;

import static java.util.Objects.requireNonNull;

/**
 * A utility class to assist in setting up a Maven-like repository system. <em>Note:</em> This component is meant to
 * assist those clients that employ the repository system outside of an IoC container, Maven plugins should instead
 * always use regular dependency injection to acquire the repository system.
 *
 * @deprecated See {@link MavenSessionBuilderSupplier}
 */
@Deprecated
public final class MavenRepositorySystemUtils {

    private MavenRepositorySystemUtils() {
        // hide constructor
    }

    /**
     * This method is deprecated, nobody should use it.
     *
     * @deprecated This method is here only for legacy uses (like UTs), nothing else should use it.
     */
    @Deprecated
    public static DefaultRepositorySystemSession newSession() {
        DefaultRepositorySystemSession session = new DefaultRepositorySystemSession(h -> false); // no close handle

        DependencyTraverser depTraverser = new FatArtifactTraverser();
        session.setDependencyTraverser(depTraverser);

        DependencyManager depManager = new ClassicDependencyManager();
        session.setDependencyManager(depManager);

        DependencySelector depFilter = new AndDependencySelector(
                new ScopeDependencySelector("test", "provided"),
                new OptionalDependencySelector(),
                new ExclusionDependencySelector());
        session.setDependencySelector(depFilter);

        DependencyGraphTransformer transformer = new ConflictResolver(
                new NearestVersionSelector(), new MavenScopeSelector(),
                new SimpleOptionalitySelector(), new MavenScopeDeriver());
        transformer = new ChainedDependencyGraphTransformer(transformer, new MavenDependencyContextRefiner());
        session.setDependencyGraphTransformer(transformer);

        session.setArtifactTypeRegistry(newArtifactTypeRegistry());

        session.setArtifactDescriptorPolicy(new SimpleArtifactDescriptorPolicy(true, true));

        return session;
    }

    /**
     * Creates new Maven-like {@link ArtifactTypeRegistry}. This method should not be used from Maven.
     *
     * @since 4.0.0
     */
    public static ArtifactTypeRegistry newArtifactTypeRegistry() {
        DefaultArtifactTypeRegistry stereotypes = new DefaultArtifactTypeRegistry();
        stereotypes.add(new DefaultArtifactType("pom"));
        stereotypes.add(new DefaultArtifactType("maven-plugin", "jar", "", "java"));
        stereotypes.add(new DefaultArtifactType("jar", "jar", "", "java"));
        stereotypes.add(new DefaultArtifactType("ejb", "jar", "", "java"));
        stereotypes.add(new DefaultArtifactType("ejb-client", "jar", "client", "java"));
        stereotypes.add(new DefaultArtifactType("test-jar", "jar", "tests", "java"));
        stereotypes.add(new DefaultArtifactType("javadoc", "jar", "javadoc", "java"));
        stereotypes.add(new DefaultArtifactType("java-source", "jar", "sources", "java", false, false));
        stereotypes.add(new DefaultArtifactType("war", "war", "", "java", false, true));
        stereotypes.add(new DefaultArtifactType("ear", "ear", "", "java", false, true));
        stereotypes.add(new DefaultArtifactType("rar", "rar", "", "java", false, true));
        stereotypes.add(new DefaultArtifactType("par", "par", "", "java", false, true));
        return stereotypes;
    }

    /**
     * Creates a new Maven-like repository system session by initializing the session with values typical for
     * Maven-based resolution. In more detail, this method configures settings relevant for the processing of dependency
     * graphs, most other settings remain at their generic default value. Use the various setters to further configure
     * the session with authentication, mirror, proxy and other information required for your environment.
     *
     * @return The new repository system session, never {@code null}.
     * @since 4.0.0
     */
    public static SessionBuilder newSession(SessionBuilder session, ArtifactTypeRegistry artifactTypeRegistry) {
        requireNonNull(session, "null sessionBuilder");
        requireNonNull(artifactTypeRegistry, "null artifactTypeRegistry");

        DependencyTraverser depTraverser = new FatArtifactTraverser();
        session.setDependencyTraverser(depTraverser);

        DependencyManager depManager = new ClassicDependencyManager();
        session.setDependencyManager(depManager);

        DependencySelector depFilter = new AndDependencySelector(
                new ScopeDependencySelector("test", "provided"),
                new OptionalDependencySelector(),
                new ExclusionDependencySelector());
        session.setDependencySelector(depFilter);

        DependencyGraphTransformer transformer = new ConflictResolver(
                new NearestVersionSelector(), new MavenScopeSelector(),
                new SimpleOptionalitySelector(), new MavenScopeDeriver());
        transformer = new ChainedDependencyGraphTransformer(transformer, new MavenDependencyContextRefiner());
        session.setDependencyGraphTransformer(transformer);
        session.setArtifactTypeRegistry(artifactTypeRegistry);

        session.setArtifactDescriptorPolicy(new SimpleArtifactDescriptorPolicy(true, true));

        return session;
    }
}
