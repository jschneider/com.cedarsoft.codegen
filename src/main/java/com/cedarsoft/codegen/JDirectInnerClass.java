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

import com.google.common.base.Splitter;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JTypeVar;

import javax.annotation.Nonnull;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 *
 */
public class JDirectInnerClass extends JClass {
  @Nonnull
  private final JClass outer;

  private final String name;

  public JDirectInnerClass( @Nonnull JCodeModel owner, @Nonnull JClass outer, @Nonnull String name ) {
    super( owner );
    this.outer = outer;
    this.name = name;
  }

  @Override
  public JClass outer() {
    return outer;
  }

  @Override
  public void generate( JFormatter f ) {
    super.generate( f );
  }

  @Override
  public String name() {
    return name;
  }

  @Override
  public String fullName() {
    return outer.fullName() + "." + name;
  }

  @Override
  public String binaryName() {
    return outer.fullName() + "$" + name;
  }

  @Override
  public JPackage _package() {
    return outer._package();
  }

  @Override
  public JClass _extends() {
    return owner().ref( Object.class );
  }

  @Override
  public Iterator<JClass> _implements() {
    return Collections.<JClass>emptyList().iterator();
  }

  @Override
  public boolean isInterface() {
    return false;
  }

  @Override
  public boolean isAbstract() {
    return false;
  }

  @Override
  protected JClass substituteParams( JTypeVar[] variables, List<JClass> bindings ) {
    return this;
  }
}
