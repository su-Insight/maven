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
package org.apache.maven.api.services.model;

import java.util.function.Consumer;

import org.apache.maven.api.model.Model;
import org.apache.maven.api.services.ModelBuilderRequest;
import org.apache.maven.api.services.ModelProblemCollector;

/**
 * Holds data relevant for a model building event.
 *
 */
public interface ModelBuildingEvent {

    /**
     * Gets the model being built. The precise state of this model depends on the event being fired.
     *
     * @return The model being built, never {@code null}.
     */
    Model model();

    Consumer<Model> update();

    /**
     * Gets the model building request being processed.
     *
     * @return The model building request being processed, never {@code null}.
     */
    ModelBuilderRequest request();

    /**
     * Gets the container used to collect problems that were encountered while processing the event.
     *
     * @return The container used to collect problems that were encountered, never {@code null}.
     */
    ModelProblemCollector problems();
}
