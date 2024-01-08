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
package org.apache.maven.internal.impl.types;

import javax.inject.Named;
import javax.inject.Provider;
import javax.inject.Singleton;

import org.apache.maven.api.JavaPathType;
import org.apache.maven.api.Type;
import org.apache.maven.internal.impl.DefaultType;

/**
 * Type provider for a JAR file containing test classes. Dependencies of this type are class-path constituents
 * or {@code --patch-module} option values. For any dependency, the choice depends on whether the main JAR file
 * was placed on the class-path or module-path respectively.
 *
 * @see Type#TEST_JAR
 */
@Named(TestJarTypeProvider.NAME)
@Singleton
public class TestJarTypeProvider implements Provider<Type> {
    public static final String NAME = Type.TEST_JAR;

    private final Type type;

    public TestJarTypeProvider() {
        this.type = new DefaultType(
                NAME, Type.LANGUAGE_JAVA, "jar", "tests", JavaPathType.CLASSES, JavaPathType.PATCH_MODULE);
    }

    @Override
    public Type get() {
        return type;
    }
}
