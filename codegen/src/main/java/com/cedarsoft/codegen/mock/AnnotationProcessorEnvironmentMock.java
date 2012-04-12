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

import com.sun.mirror.apt.AnnotationProcessorEnvironment;
import com.sun.mirror.apt.AnnotationProcessorListener;
import com.sun.mirror.apt.Filer;
import com.sun.mirror.apt.Messager;
import com.sun.mirror.declaration.AnnotationTypeDeclaration;
import com.sun.mirror.declaration.Declaration;
import com.sun.mirror.declaration.PackageDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.util.Declarations;
import com.sun.mirror.util.Types;

import java.util.Collection;
import java.util.Map;

/**
 *
 */
public class AnnotationProcessorEnvironmentMock implements AnnotationProcessorEnvironment {
  @Override
  public Map<String, String> getOptions() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Messager getMessager() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Filer getFiler() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Collection<TypeDeclaration> getSpecifiedTypeDeclarations() {
    throw new UnsupportedOperationException();
  }

  @Override
  public PackageDeclaration getPackage( String name ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public TypeDeclaration getTypeDeclaration( String name ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Collection<TypeDeclaration> getTypeDeclarations() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Collection<Declaration> getDeclarationsAnnotatedWith( AnnotationTypeDeclaration a ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public Declarations getDeclarationUtils() {
    throw new UnsupportedOperationException();
  }

  @Override
  public Types getTypeUtils() {
    throw new UnsupportedOperationException();
  }

  @Override
  public void addListener( AnnotationProcessorListener listener ) {
    throw new UnsupportedOperationException();
  }

  @Override
  public void removeListener( AnnotationProcessorListener listener ) {
    throw new UnsupportedOperationException();
  }
}
