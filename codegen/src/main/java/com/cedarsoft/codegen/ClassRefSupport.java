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

import com.cedarsoft.NotFoundException;
import com.google.common.base.Splitter;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 */
public class ClassRefSupport {
  @NonNls
  public static final String JDIRECT_CLASS_NAME = "com.sun.codemodel.JDirectClass";
  @NotNull
  protected final JCodeModel model;
  @NotNull
  private final Map<String, JClass> refs = new HashMap<String, JClass>();

  public ClassRefSupport( @NotNull JCodeModel model ) {
    this.model = model;
  }

  @NotNull
  public JClass ref( @NotNull @NonNls String qualifiedName ) {
    verifyValidRefName( qualifiedName );

    try {
      return resolve( qualifiedName );
    } catch ( NotFoundException ignore ) {
    }

    return createRef( qualifiedName );
  }

  protected void verifyValidRefName( @NotNull @NonNls String qualifiedName ) {
    if ( qualifiedName.contains( "?" ) || qualifiedName.contains( "<" ) || qualifiedName.contains( ">" ) ) {
      throw new IllegalArgumentException( "Cannot create ref for <" + qualifiedName + ">" );
    }

    //Check for inner class with wrong notation
    int index = qualifiedName.lastIndexOf( '.' );
    if ( index > 0 ) {
      @NotNull @NonNls
      String packagePart = qualifiedName.substring( 0, index );
      if ( !packagePart.toLowerCase().equals( packagePart ) ) {
        throw new IllegalArgumentException( "Invalid inner class <" + qualifiedName + ">. Use \"$\" sign instead." );
      }
    }
  }

  @NotNull
  private JClass createRef( @NotNull @NonNls String qualifiedName ) {
    JClass newRef = model.ref( qualifiedName );

    //fix it if it is a inner class
    if ( isJDirectClass( newRef ) && isInner( qualifiedName ) ) {
      newRef = createDirectInner( qualifiedName );
    }

    storeRef( qualifiedName, newRef );
    return newRef;
  }

  @NotNull
  private JClass createDirectInner( @NotNull @NonNls String qualifiedName ) {
    Iterator<String> split = Splitter.on( "$" ).split( qualifiedName ).iterator();

    String outerFqName = split.next();
    String innerName = split.next();

    JClass outer = model.ref( outerFqName );

    return new JDirectInnerClass( model, outer, innerName );
  }

  private void storeRef( @NotNull @NonNls String qualifiedName, @NotNull JClass newRef ) {
    refs.put( qualifiedName, newRef );
  }

  @NotNull
  private JClass resolve( @NotNull @NonNls String qualifiedName ) throws NotFoundException {
    JClass ref = refs.get( qualifiedName );
    if ( ref != null ) {
      return ref;
    }

    JDefinedClass resolved = model._getClass( qualifiedName );
    if ( resolved != null ) {
      return resolved;
    }

    throw new NotFoundException();
  }

  @NotNull
  public JClass ref( @NotNull Class<?> type ) {
    try {
      return resolve( type.getName() );
    } catch ( NotFoundException ignore ) {
    }

    JClass newRef = model.ref( type );
    storeRef( type.getName(), newRef );
    return newRef;
  }

  private static boolean isJDirectClass( @NotNull JClass newRef ) {
    return newRef.getClass().getName().equals( JDIRECT_CLASS_NAME );
  }

  private static boolean isInner( @NotNull @NonNls String qualifiedName ) {
    return qualifiedName.contains( "$" );
  }
}
