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

package com.cedarsoft.codegen.mock;

import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.TypeMirror;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

/**
 *
 */
public class CollectionTypeMirrorMock extends ReferenceTypeMock implements DeclaredType {
  @Nullable
  private final Class<?> paramType;

  public CollectionTypeMirrorMock( @Nonnull Class<?> collectionType ) {
    this( collectionType, null );
  }

  public CollectionTypeMirrorMock( @Nonnull Class<?> collectionType, @Nullable Class<?> type ) {
    super( collectionType );
    this.paramType = type;
  }

  @Override
  public TypeDeclaration getDeclaration() {
    return new TypeDeclarationMock( type );
  }

  @Override
  public DeclaredType getContainingType() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Collection<TypeMirror> getActualTypeArguments() {
    if ( paramType == null ) {
      return Collections.emptyList();
    }
    return Arrays.<TypeMirror>asList( new ClassTypeMock( paramType ) );
  }

  @Override
  public Collection<InterfaceType> getSuperinterfaces() {
    throw new UnsupportedOperationException();
  }

  @Nonnull
  public Class<?> getCollectionType() {
    return type;
  }

  @Override
  public String toString() {
    return "CollectionTypeMirrorMock{" +
      "collectionType=" + type +
      "paramType=" + paramType +
      '}';
  }
}
