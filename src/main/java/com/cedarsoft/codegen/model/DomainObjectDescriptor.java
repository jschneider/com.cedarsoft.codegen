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

package com.cedarsoft.codegen.model;

import com.cedarsoft.codegen.TypeUtils;
import com.google.common.collect.Lists;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.ConstructorDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.type.TypeMirror;

import javax.annotation.Nonnull;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 */
public class DomainObjectDescriptor {
  @Nonnull
  private final List<FieldWithInitializationInfo> fieldInfos = Lists.newArrayList();
  @Nonnull
  private final ClassDeclaration classDeclaration;

  public DomainObjectDescriptor( @Nonnull ClassDeclaration classDeclaration ) {
    this.classDeclaration = classDeclaration;
  }

  @Nonnull
  public String getQualifiedName() {
    return classDeclaration.getQualifiedName();
  }

  @Nonnull
  public ClassDeclaration getClassDeclaration() {
    return classDeclaration;
  }

  public void addField( @Nonnull FieldWithInitializationInfo fieldToSerialize ) {
    this.fieldInfos.add( fieldToSerialize );
  }

  @Nonnull
  public List<? extends FieldWithInitializationInfo> getFieldInfos() {
    return Collections.unmodifiableList( fieldInfos );
  }

  /**
   * Returns only the field infos that are initialized using the constructor
   *
   * @return the field infos initialized within the constructor
   */
  @Nonnull
  public List<? extends FieldInitializedInConstructorInfo> getFieldsInitializedInConstructor() {
    List<FieldInitializedInConstructorInfo> found = new ArrayList<FieldInitializedInConstructorInfo>();
    for ( FieldWithInitializationInfo info : fieldInfos ) {
      if ( info instanceof FieldInitializedInConstructorInfo ) {
        found.add( ( FieldInitializedInConstructorInfo ) info );
      }
    }

    Collections.sort( found, new FieldWithInitializationInfoComparator() );

    return found;
  }

  @Nonnull
  public List<? extends FieldInitializedInSetterInfo> getFieldsInitializedInSetter() {
    List<FieldInitializedInSetterInfo> found = new ArrayList<FieldInitializedInSetterInfo>();
    for ( FieldWithInitializationInfo info : fieldInfos ) {
      if ( info instanceof FieldInitializedInSetterInfo ) {
        found.add( ( FieldInitializedInSetterInfo ) info );
      }
    }

    return found;
  }

  @Nonnull
  public List<? extends FieldNotInitializationInfo> getFieldsNotInitialized() {
    List<FieldNotInitializationInfo> found = new ArrayList<FieldNotInitializationInfo>();
    for ( FieldWithInitializationInfo info : fieldInfos ) {
      if ( info instanceof FieldNotInitializationInfo ) {
        found.add( ( FieldNotInitializationInfo ) info );
      }
    }

    return found;
  }

  @Nonnull
  public ConstructorDeclaration findBestConstructor() {
    return TypeUtils.findBestConstructor( classDeclaration );
  }

  @Nonnull
  public MethodDeclaration findSetter( @Nonnull String fieldName, @Nonnull TypeMirror type ) {
    return TypeUtils.findSetter( classDeclaration, fieldName, type );
  }

  @Nonnull
  public MethodDeclaration findSetter( @Nonnull FieldDeclaration fieldDeclaration ) {
    return TypeUtils.findSetter( classDeclaration, fieldDeclaration );
  }

  @Nonnull
  public MethodDeclaration findGetterForField( @Nonnull FieldDeclaration fieldDeclaration ) {
    return TypeUtils.findGetterForField( classDeclaration, fieldDeclaration );
  }

  @Nonnull
  public MethodDeclaration findGetterForField( @Nonnull String simpleName, @Nonnull TypeMirror type ) {
    return TypeUtils.findGetterForField( classDeclaration, simpleName, type );
  }

  @Nonnull
  public FieldDeclaration findFieldDeclaration( @Nonnull String fieldName ) {
    return TypeUtils.findFieldDeclaration( classDeclaration, fieldName );
  }

  private static class FieldWithInitializationInfoComparator implements Comparator<FieldInitializedInConstructorInfo>, Serializable {
    @Override
    public int compare( FieldInitializedInConstructorInfo o1, FieldInitializedInConstructorInfo o2 ) {
      return Integer.valueOf( o1.getConstructorCallInfo().getIndex() ).compareTo( o2.getConstructorCallInfo().getIndex() );
    }
  }
}
