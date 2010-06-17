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

package com.cedarsoft.codegen;

import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.declaration.TypeParameterDeclaration;
import com.sun.mirror.type.ArrayType;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.PrimitiveType;
import com.sun.mirror.type.ReferenceType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.type.TypeVariable;
import com.sun.mirror.type.VoidType;
import com.sun.mirror.type.WildcardType;
import com.sun.mirror.util.Types;

import java.util.Collection;

/**
 *
 */
public class TypesMock implements Types {
  @Override
  public boolean isSubtype( TypeMirror t1, TypeMirror t2 ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean isAssignable( TypeMirror t1, TypeMirror t2 ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public TypeMirror getErasure( TypeMirror t ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public PrimitiveType getPrimitiveType( PrimitiveType.Kind kind ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public VoidType getVoidType() {
    throw new UnsupportedOperationException();
  }

  @Override
  public ArrayType getArrayType( TypeMirror componentType ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public TypeVariable getTypeVariable( TypeParameterDeclaration tparam ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public WildcardType getWildcardType( Collection<ReferenceType> upperBounds, Collection<ReferenceType> lowerBounds ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeclaredType getDeclaredType( TypeDeclaration decl, TypeMirror... typeArgs ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public DeclaredType getDeclaredType( DeclaredType containing, TypeDeclaration decl, TypeMirror... typeArgs ) {
    throw new UnsupportedOperationException();
  }
}
