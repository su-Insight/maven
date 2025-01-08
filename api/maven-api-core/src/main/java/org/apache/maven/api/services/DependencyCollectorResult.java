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

import java.util.List;

import org.apache.maven.api.Node;
import org.apache.maven.api.annotations.Experimental;
import org.apache.maven.api.annotations.Nonnull;
import org.apache.maven.api.annotations.Nullable;

/**
 * The result of a dependency collection request.
 *
 * @since 4.0.0
 * @see DependencyCollector#collect(DependencyCollectorRequest)
 */
@Experimental
public interface DependencyCollectorResult {
    /**
     * Gets the exceptions that occurred while building the dependency graph.
     *
     * @return the exceptions that occurred, never {@code null}
     */
    @Nonnull
    List<Exception> getExceptions();

    /**
     * Gets the root node of the dependency graph.
     *
     * @return the root node of the dependency graph or {@code null} if none
     */
    @Nullable
    Node getRoot();
}
