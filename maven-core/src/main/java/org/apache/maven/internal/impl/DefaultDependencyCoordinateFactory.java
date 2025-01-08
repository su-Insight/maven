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
package org.apache.maven.internal.impl;

import javax.inject.Named;
import javax.inject.Singleton;

import org.apache.maven.api.DependencyCoordinate;
import org.apache.maven.api.Exclusion;
import org.apache.maven.api.annotations.Nonnull;
import org.apache.maven.api.services.DependencyCoordinateFactory;
import org.apache.maven.api.services.DependencyCoordinateFactoryRequest;
import org.eclipse.aether.artifact.ArtifactType;

import static org.apache.maven.internal.impl.Utils.map;
import static org.apache.maven.internal.impl.Utils.nonNull;

@Named
@Singleton
public class DefaultDependencyCoordinateFactory implements DependencyCoordinateFactory {

    @Nonnull
    @Override
    public DependencyCoordinate create(@Nonnull DependencyCoordinateFactoryRequest request) {
        nonNull(request, "request");
        InternalSession session = InternalSession.from(request.getSession());

        ArtifactType type = null;
        if (request.getType() != null) {
            type = session.getSession().getArtifactTypeRegistry().get(request.getType());
        }
        if (request.getCoordinateString() != null) {
            return new DefaultDependencyCoordinate(
                    session,
                    new org.eclipse.aether.graph.Dependency(
                            new org.eclipse.aether.artifact.DefaultArtifact(request.getCoordinateString()),
                            request.getScope(),
                            request.isOptional(),
                            map(request.getExclusions(), this::toExclusion)));
        } else {
            return new DefaultDependencyCoordinate(
                    session,
                    new org.eclipse.aether.graph.Dependency(
                            new org.eclipse.aether.artifact.DefaultArtifact(
                                    request.getGroupId(),
                                    request.getArtifactId(),
                                    request.getClassifier(),
                                    request.getExtension(),
                                    request.getVersion(),
                                    type),
                            request.getScope(),
                            request.isOptional(),
                            map(request.getExclusions(), this::toExclusion)));
        }
    }

    private org.eclipse.aether.graph.Exclusion toExclusion(Exclusion exclusion) {
        return new org.eclipse.aether.graph.Exclusion(exclusion.getGroupId(), exclusion.getArtifactId(), "*", "*");
    }
}
