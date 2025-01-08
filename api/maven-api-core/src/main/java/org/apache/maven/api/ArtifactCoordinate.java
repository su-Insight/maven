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

import org.apache.maven.api.annotations.Experimental;
import org.apache.maven.api.annotations.Immutable;
import org.apache.maven.api.annotations.Nonnull;

/**
 * The {@code Coordinate} object is used to point to an {@link Artifact}
 * but the version may be specified as a range instead of an exact version.
 *
 * @since 4.0.0
 */
@Experimental
@Immutable
public interface ArtifactCoordinate {

    /**
     * The groupId of the artifact.
     *
     * @return the groupId
     */
    @Nonnull
    String getGroupId();

    /**
     * The artifactId of the artifact.
     *
     * @return the artifactId
     */
    @Nonnull
    String getArtifactId();

    /**
     * The classifier of the artifact.
     *
     * @return the classifier or an empty string if none, never {@code null}
     */
    @Nonnull
    String getClassifier();

    /**
     * The version of the artifact.
     *
     * @return the version
     */
    @Nonnull
    VersionConstraint getVersion();

    /**
     * The extension of the artifact.
     *
     * @return the extension or an empty string if none, never {@code null}
     */
    @Nonnull
    String getExtension();

    /**
     * Unique id identifying this artifact
     */
    @Nonnull
    default String getId() {
        return getGroupId()
                + ":" + getArtifactId()
                + ":" + getExtension()
                + (getClassifier().isEmpty() ? "" : ":" + getClassifier())
                + ":" + getVersion();
    }
}
