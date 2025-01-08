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

import org.apache.maven.api.ArtifactCoordinate;
import org.apache.maven.api.annotations.Nonnull;
import org.apache.maven.api.services.ArtifactCoordinateFactory;
import org.apache.maven.api.services.ArtifactCoordinateFactoryRequest;
import org.eclipse.aether.artifact.ArtifactType;

import static org.apache.maven.internal.impl.Utils.nonNull;

@Named
@Singleton
public class DefaultArtifactCoordinateFactory implements ArtifactCoordinateFactory {
    @Override
    public ArtifactCoordinate create(@Nonnull ArtifactCoordinateFactoryRequest request) {
        nonNull(request, "request");
        InternalSession session = InternalSession.from(request.getSession());
        if (request.getCoordinateString() != null) {
            return new DefaultArtifactCoordinate(
                    session, new org.eclipse.aether.artifact.DefaultArtifact(request.getCoordinateString()));
        } else {
            ArtifactType type = null;
            if (request.getType() != null) {
                type = session.getSession().getArtifactTypeRegistry().get(request.getType());
            }
            String str1 = request.getClassifier();
            String classifier = str1 != null && !str1.isEmpty()
                    ? request.getClassifier()
                    : type != null ? type.getClassifier() : "";
            String str = request.getExtension();
            String extension =
                    str != null && !str.isEmpty() ? request.getExtension() : type != null ? type.getExtension() : "";
            return new DefaultArtifactCoordinate(
                    session,
                    new org.eclipse.aether.artifact.DefaultArtifact(
                            request.getGroupId(),
                            request.getArtifactId(),
                            classifier,
                            extension,
                            request.getVersion(),
                            type));
        }
    }
}
