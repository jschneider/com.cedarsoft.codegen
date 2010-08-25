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

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JType;
import com.sun.mirror.type.TypeMirror;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.HashSet;

/**
 *
 */
public class NewInstanceFactory {
  @NonNls
  public static final String METHOD_NAME_VALUE_OF = "valueOf";
  @NonNls
  public static final String METHOD_NAME_AS_LIST = "asList";
  @NonNls
  public static final String CONSTANT_NAME_TRUE = "TRUE";
  public static final int DEFAULT_VALUE_INTEGER = 42;
  public static final float DEFAULT_VALUE_FLOAT = 44.0F;
  public static final long DEFAULT_VALUE_LONG = 43L;
  public static final double DEFAULT_VALUE_DOUBLE = 12.5;
  public static final char DEFAULT_VALUE_CHAR = 'c';

  @NotNull
  private final JCodeModel codeModel;
  @NotNull
  private final ClassRefSupport classRefSupport;

  public NewInstanceFactory( @NotNull JCodeModel codeModel, @NotNull ClassRefSupport classRefSupport ) {
    this.codeModel = codeModel;
    this.classRefSupport = classRefSupport;
  }

  @NotNull
  public JExpression create( @NotNull JType type, @NotNull @NonNls String simpleName ) {
    JType erased = type.erasure();
    if ( TypeUtils.isType( erased, String.class ) ) {
      return JExpr.lit( simpleName );
    }

    //Primitive types
    if ( TypeUtils.isType( erased, Integer.TYPE ) ) {
      return JExpr.lit( DEFAULT_VALUE_INTEGER );
    }
    if ( TypeUtils.isType( erased, Long.TYPE ) ) {
      return JExpr.lit( DEFAULT_VALUE_LONG );
    }
    if ( TypeUtils.isType( erased, Float.TYPE ) ) {
      return JExpr.lit( DEFAULT_VALUE_FLOAT );
    }
    if ( TypeUtils.isType( erased, Double.TYPE ) ) {
      return JExpr.lit( DEFAULT_VALUE_DOUBLE );
    }
    if ( TypeUtils.isType( erased, Boolean.TYPE ) ) {
      return JExpr.lit( true );
    }
    if ( TypeUtils.isType( erased, Character.TYPE ) ) {
      return JExpr.lit( DEFAULT_VALUE_CHAR );
    }


    //Default types
    if ( TypeUtils.isType( erased, Integer.class ) ) {
      return codeModel.ref( Integer.class ).staticInvoke( METHOD_NAME_VALUE_OF ).arg( JExpr.lit( DEFAULT_VALUE_INTEGER ) );
    }
    if ( TypeUtils.isType( erased, Double.class ) ) {
      return codeModel.ref( Double.class ).staticInvoke( METHOD_NAME_VALUE_OF ).arg( JExpr.lit( DEFAULT_VALUE_DOUBLE ) );
    }
    if ( TypeUtils.isType( erased, Long.class ) ) {
      return codeModel.ref( Long.class ).staticInvoke( METHOD_NAME_VALUE_OF ).arg( JExpr.lit( DEFAULT_VALUE_LONG ) );
    }
    if ( TypeUtils.isType( erased, Float.class ) ) {
      return codeModel.ref( Float.class ).staticInvoke( METHOD_NAME_VALUE_OF ).arg( JExpr.lit( DEFAULT_VALUE_FLOAT ) );
    }
    if ( TypeUtils.isType( erased, Boolean.class ) ) {
      return codeModel.ref( Boolean.class ).staticRef( CONSTANT_NAME_TRUE );
    }

    if ( TypeUtils.isCollectionType( type ) ) {
      return createCollectionInvocation( TypeUtils.getCollectionParam( ( JClass ) type ), simpleName, TypeUtils.isSetType( type ) );
    } else {
      return JExpr._new( classRefSupport.ref( TypeUtils.removeWildcard( type, true ) ) );
    }
  }

  @NotNull
  public JExpression createCollectionInvocation( @NotNull JType collectionParamType, @NotNull @NonNls String simpleName, boolean isSet ) {
    JExpression expression = create( collectionParamType.erasure(), simpleName );

    JInvocation listInvocation = classRefSupport.ref( Arrays.class ).staticInvoke( METHOD_NAME_AS_LIST ).arg( expression );
    if ( isSet ) {
      return JExpr._new( classRefSupport.ref( HashSet.class ) ).arg( listInvocation );
    } else {
      return listInvocation;
    }
  }

  @NotNull
  public JExpression create( @NotNull TypeMirror type, @NotNull @NonNls String simpleName ) {
    if ( TypeUtils.isType( type, String.class ) ) {
      return JExpr.lit( simpleName );
    }

    //Primitive types
    if ( TypeUtils.isType( type, Integer.TYPE ) ) {
      return JExpr.lit( DEFAULT_VALUE_INTEGER );
    }
    if ( TypeUtils.isType( type, Long.TYPE ) ) {
      return JExpr.lit( DEFAULT_VALUE_LONG );
    }
    if ( TypeUtils.isType( type, Float.TYPE ) ) {
      return JExpr.lit( DEFAULT_VALUE_FLOAT );
    }
    if ( TypeUtils.isType( type, Double.TYPE ) ) {
      return JExpr.lit( DEFAULT_VALUE_DOUBLE );
    }
    if ( TypeUtils.isType( type, Boolean.TYPE ) ) {
      return JExpr.lit( true );
    }
    if ( TypeUtils.isType( type, Character.TYPE ) ) {
      return JExpr.lit( DEFAULT_VALUE_CHAR );
    }

    //Default types
    if ( TypeUtils.isType( type, Integer.class ) ) {
      return codeModel.ref( Integer.class ).staticInvoke( METHOD_NAME_VALUE_OF ).arg( JExpr.lit( DEFAULT_VALUE_INTEGER ) );
    }
    if ( TypeUtils.isType( type, Double.class ) ) {
      return codeModel.ref( Double.class ).staticInvoke( METHOD_NAME_VALUE_OF ).arg( JExpr.lit( DEFAULT_VALUE_DOUBLE ) );
    }
    if ( TypeUtils.isType( type, Long.class ) ) {
      return codeModel.ref( Long.class ).staticInvoke( METHOD_NAME_VALUE_OF ).arg( JExpr.lit( DEFAULT_VALUE_LONG ) );
    }
    if ( TypeUtils.isType( type, Float.class ) ) {
      return codeModel.ref( Float.class ).staticInvoke( METHOD_NAME_VALUE_OF ).arg( JExpr.lit( DEFAULT_VALUE_FLOAT ) );
    }
    if ( TypeUtils.isType( type, Boolean.class ) ) {
      return codeModel.ref( Boolean.class ).staticRef( CONSTANT_NAME_TRUE );
    }

    if ( TypeUtils.isCollectionType( type ) ) {
      return createCollectionInvocation( TypeUtils.getCollectionParam( type ), simpleName, TypeUtils.isSetType( type ) );
    } else {
      return JExpr._new( classRefSupport.ref( type.toString() ) );
    }
  }

  @NotNull
  public JExpression createCollectionInvocation( @NotNull TypeMirror collectionParamType, @NotNull @NonNls String simpleName, boolean isSet ) {
    JExpression expression = create( TypeUtils.getErasure( collectionParamType ), simpleName );

    JInvocation listInvocation = classRefSupport.ref( Arrays.class ).staticInvoke( METHOD_NAME_AS_LIST ).arg( expression );
    if ( isSet ) {
      return JExpr._new( classRefSupport.ref( HashSet.class ) ).arg( listInvocation );
    } else {
      return listInvocation;
    }
  }
}
