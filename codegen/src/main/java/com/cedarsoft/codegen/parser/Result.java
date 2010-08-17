/**
 * Copyright (C) cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce
 *         (GPL 3 with Classpath Exception)
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 3 only, as
 * published by the Free Software Foundation. cedarsoft GmbH designates this
 * particular file as subject to the "Classpath" exception as provided
 * by cedarsoft GmbH in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 3 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 3 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact cedarsoft GmbH, 72810 Gomaringen, Germany,
 * or visit www.cedarsoft.com if you need additional information or
 * have any questions.
 */

package com.cedarsoft.codegen.parser;

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.declaration.ClassDeclaration;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class Result {
  @NotNull
  private final List<ClassDeclaration> classDeclarations = new ArrayList<ClassDeclaration>();

  @NotNull
  private final AnnotationProcessorEnvironment environment;

  public Result( @NotNull AnnotationProcessorEnvironment environment ) {
    this.environment = environment;
  }

  public void addClassDeclaration( @NotNull ClassDeclaration classDeclaration ) {
    this.classDeclarations.add( classDeclaration );
  }

  @NotNull
  public List<? extends ClassDeclaration> getClassDeclarations() {
    return Collections.unmodifiableList( classDeclarations );
  }

  @NotNull
  public ClassDeclaration getClassDeclaration( @NotNull @NonNls String fqName ) {
    for ( ClassDeclaration classDeclaration : classDeclarations ) {
      if ( classDeclaration.getQualifiedName().equals( fqName ) ) {
        return classDeclaration;
      }
    }

    throw new IllegalArgumentException( "No class declaration found for <" + fqName + ">" );
  }

  /**
   * Use {@link #getClassDeclaration(String)} instead
   *
   * @return the class declaration with the shoretest fq name
   */
  @Deprecated
  @NotNull
  public ClassDeclaration getClassDeclaration() {
    if ( classDeclarations.isEmpty() ) {
      throw new IllegalStateException( "No class declaration found" );
    }

    if ( classDeclarations.size() == 1 ) {
      return classDeclarations.get( 0 );
    }

    //Find the shortest
    ClassDeclaration shortest = findClassDeclarationWithShortestFQName();

    //Verify the other are just inner classes!
    for ( ClassDeclaration classDeclaration : classDeclarations ) {
      if ( !classDeclaration.getQualifiedName().startsWith( shortest.getQualifiedName() ) ) {
        throw new IllegalStateException( "Invalid class declarations count found: " + classDeclarations.size() + " (" + classDeclarations + ")" );
      }
    }

    return shortest;
  }

  @Deprecated
  @NotNull
  public ClassDeclaration findClassDeclarationWithShortestFQName() {
    ClassDeclaration shortest = null;
    for ( ClassDeclaration classDeclaration : classDeclarations ) {
      if ( shortest == null || shortest.getQualifiedName().length() > classDeclaration.getQualifiedName().length() ) {
        shortest = classDeclaration;
      }
    }

    if ( shortest == null ) {
      throw new IllegalStateException( "No class declaration found" );
    }

    return shortest;
  }

  @NotNull
  public AnnotationProcessorEnvironment getEnvironment() {
    return environment;
  }
}
