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
package org.apache.maven.api.services;

import org.apache.maven.api.ArtifactCoordinate;
import org.apache.maven.api.Session;
import org.apache.maven.api.annotations.Experimental;
import org.apache.maven.api.annotations.Immutable;
import org.apache.maven.api.annotations.Nonnull;
import org.apache.maven.api.annotations.NotThreadSafe;

import static org.apache.maven.api.services.BaseRequest.nonNull;

/**
 * A request for creating a {@link ArtifactCoordinate} object.
 *
 * @since 4.0.0
 */
@Experimental
@Immutable
public interface ArtifactCoordinateFactoryRequest {

    @Nonnull
    Session getSession();

    String getGroupId();

    String getArtifactId();

    String getVersion();

    String getClassifier();

    String getExtension();

    String getType();

    String getCoordinateString();

    @Nonnull
    static ArtifactCoordinateFactoryRequest build(
            @Nonnull Session session, String groupId, String artifactId, String version, String extension) {
        return ArtifactCoordinateFactoryRequest.builder()
                .session(nonNull(session, "session"))
                .groupId(groupId)
                .artifactId(artifactId)
                .version(version)
                .extension(extension)
                .build();
    }

    @Nonnull
    static ArtifactCoordinateFactoryRequest build(
            @Nonnull Session session,
            String groupId,
            String artifactId,
            String version,
            String classifier,
            String extension,
            String type) {
        return ArtifactCoordinateFactoryRequest.builder()
                .session(nonNull(session, "session"))
                .groupId(groupId)
                .artifactId(artifactId)
                .version(version)
                .classifier(classifier)
                .extension(extension)
                .type(type)
                .build();
    }

    @Nonnull
    static ArtifactCoordinateFactoryRequest build(@Nonnull Session session, @Nonnull String coordinateString) {
        return ArtifactCoordinateFactoryRequest.builder()
                .session(nonNull(session, "session"))
                .coordinateString(nonNull(coordinateString, "coordinateString"))
                .build();
    }

    @Nonnull
    static ArtifactCoordinateFactoryRequest build(@Nonnull Session session, @Nonnull ArtifactCoordinate coordinate) {
        return ArtifactCoordinateFactoryRequest.builder()
                .session(nonNull(session, "session"))
                .groupId(nonNull(coordinate, "coordinate").getGroupId())
                .artifactId(coordinate.getArtifactId())
                .classifier(coordinate.getClassifier())
                .version(coordinate.getVersion().asString())
                .extension(coordinate.getExtension())
                .build();
    }

    static ArtifactFactoryRequestBuilder builder() {
        return new ArtifactFactoryRequestBuilder();
    }

    @NotThreadSafe
    class ArtifactFactoryRequestBuilder {
        private Session session;
        private String groupId;
        private String artifactId;
        private String version;
        private String classifier;
        private String extension;
        private String type;
        private String coordinateString;

        ArtifactFactoryRequestBuilder() {}

        public ArtifactFactoryRequestBuilder session(Session session) {
            this.session = session;
            return this;
        }

        public ArtifactFactoryRequestBuilder groupId(String groupId) {
            this.groupId = groupId;
            return this;
        }

        public ArtifactFactoryRequestBuilder artifactId(String artifactId) {
            this.artifactId = artifactId;
            return this;
        }

        public ArtifactFactoryRequestBuilder version(String version) {
            this.version = version;
            return this;
        }

        public ArtifactFactoryRequestBuilder classifier(String classifier) {
            this.classifier = classifier;
            return this;
        }

        public ArtifactFactoryRequestBuilder extension(String extension) {
            this.extension = extension;
            return this;
        }

        public ArtifactFactoryRequestBuilder type(String type) {
            this.type = type;
            return this;
        }

        public ArtifactFactoryRequestBuilder coordinateString(String coordinateString) {
            this.coordinateString = coordinateString;
            return this;
        }

        public ArtifactCoordinateFactoryRequest build() {
            return new DefaultArtifactFactoryRequestArtifact(
                    session, groupId, artifactId, version, classifier, extension, type, coordinateString);
        }

        private static class DefaultArtifactFactoryRequestArtifact extends BaseRequest
                implements ArtifactCoordinateFactoryRequest {
            private final String groupId;
            private final String artifactId;
            private final String version;
            private final String classifier;
            private final String extension;
            private final String type;
            private final String coordinateString;

            DefaultArtifactFactoryRequestArtifact(
                    @Nonnull Session session,
                    String groupId,
                    String artifactId,
                    String version,
                    String classifier,
                    String extension,
                    String type,
                    String coordinateString) {
                super(session);
                this.groupId = groupId;
                this.artifactId = artifactId;
                this.version = version;
                this.classifier = classifier;
                this.extension = extension;
                this.type = type;
                this.coordinateString = coordinateString;
            }

            @Override
            public String getGroupId() {
                return groupId;
            }

            @Override
            public String getArtifactId() {
                return artifactId;
            }

            @Override
            public String getVersion() {
                return version;
            }

            @Override
            public String getClassifier() {
                return classifier;
            }

            @Override
            public String getExtension() {
                return extension;
            }

            @Override
            public String getType() {
                return type;
            }

            public String getCoordinateString() {
                return coordinateString;
            }
        }
    }
}
