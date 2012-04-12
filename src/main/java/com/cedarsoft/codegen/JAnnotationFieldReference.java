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

import com.sun.codemodel.JAnnotationUse;
import com.sun.codemodel.JAnnotationValue;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JFormatter;
import org.fest.reflect.core.Reflection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class JAnnotationFieldReference extends JAnnotationValue {
  @Nonnull
  public static final String FIELD_OWNER = "owner";
  @Nonnull
  public static final String METHOD_ADD_VALUE = "addValue";
  @Nonnull
  private final JFieldVar ns;
  @Nonnull
  private final JDefinedClass owner;

  public JAnnotationFieldReference( @Nonnull JFieldVar ns ) {
    this.ns = ns;
    this.owner = Reflection.field( FIELD_OWNER ).ofType( JDefinedClass.class ).in( ns ).get();
  }

  @Override
  public void generate( JFormatter f ) {
    f.p( owner.name() );
    f.p( "." );
    f.p( ns.name() );
  }

  @Nonnull
  public static JAnnotationUse param( @Nonnull JAnnotationUse annotation, @Nonnull String key, @Nonnull JFieldVar ref ) {
    Reflection.method( METHOD_ADD_VALUE ).withParameterTypes( String.class, JAnnotationValue.class )
      .in( annotation ).invoke( key, new JAnnotationFieldReference( ref ) );
    return annotation;
  }
}
