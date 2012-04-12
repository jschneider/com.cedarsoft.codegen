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

import com.cedarsoft.exceptions.NotFoundException;
import com.cedarsoft.codegen.model.FieldDeclarationInfo;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMod;
import com.sun.mirror.type.TypeMirror;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class CodeGenerator {
  @Nonnull
  protected final JCodeModel model;
  @Nonnull
  private final ParseExpressionFactory parseExpressionFactory;
  @Nonnull
  private final NewInstanceFactory newInstanceFactory;
  @Nonnull
  private final ClassRefSupport classRefSupport;
  @Nonnull
  private final DecisionCallback decisionCallback;

  @Nonnull
  private final List<Decorator> decorators = new ArrayList<Decorator>();

  public CodeGenerator( @Nonnull DecisionCallback decisionCallback ) {
    this( new JCodeModel(), decisionCallback );
  }

  protected CodeGenerator( @Nonnull JCodeModel model, @Nonnull DecisionCallback decisionCallback ) {
    this.model = model;
    this.classRefSupport = new ClassRefSupport( model );
    this.parseExpressionFactory = new ParseExpressionFactory( model, classRefSupport );
    this.newInstanceFactory = new NewInstanceFactory( model, classRefSupport );
    this.decisionCallback = decisionCallback;
  }

  @Nonnull
  public ParseExpressionFactory getParseExpressionFactory() {
    return parseExpressionFactory;
  }

  @Nonnull
  public NewInstanceFactory getNewInstanceFactory() {
    return newInstanceFactory;
  }

  @Nonnull
  public JCodeModel getModel() {
    return model;
  }

  @Nonnull
  public ClassRefSupport getClassRefSupport() {
    return classRefSupport;
  }

  @Nonnull
  public DecisionCallback getDecisionCallback() {
    return decisionCallback;
  }

  public void addDecorator( @Nonnull Decorator decorator ) {
    this.decorators.add( decorator );
  }

  @Nonnull
  public List<? extends Decorator> getDecorators() {
    return Collections.unmodifiableList( decorators );
  }

  @Nonnull
  public JFieldVar getOrCreateConstant( @Nonnull JDefinedClass serializerClass, @Nonnull Class<?> type, @Nonnull String constantName, @Nonnull JExpression initExpression ) {
    //Get the constant if it still exists
    JFieldVar fieldVar = serializerClass.fields().get( constantName );
    if ( fieldVar != null ) {
      return fieldVar;
    }

    //Create
    return createConstant( serializerClass, type, constantName, initExpression );
  }

  @Nonnull
  public JFieldVar createConstant( @Nonnull JDefinedClass serializerClass, @Nonnull Class<?> type, @Nonnull String constantName, @Nonnull JExpression initExpression ) {
    JFieldVar constant = serializerClass.field( JMod.FINAL | JMod.PUBLIC | JMod.STATIC, type, constantName, initExpression );

    for ( Decorator decorator : decorators ) {
      decorator.decorateConstant( this, constant );
    }

    return constant;
  }

  @Nonnull
  public JClass ref( @Nonnull String qualifiedName ) {
    return getClassRefSupport().ref( qualifiedName );
  }

  @Nonnull
  public JClass ref( @Nonnull Class<?> type ) {
    return getClassRefSupport().ref( type );
  }

  @Nonnull
  public JInvocation createGetterInvocation( @Nonnull JExpression object, @Nonnull FieldDeclarationInfo fieldInfo ) {
    return object.invoke( fieldInfo.getGetterDeclaration().getSimpleName() );
  }

  @Nonnull
  public JClass ref( @Nonnull TypeMirror type ) {
    if ( TypeUtils.isCollectionType( type ) ) {
      JClass referencedCollectionParam = refCollectionParam( type );

      JClass referencedCollection = ref( TypeUtils.getErasure( type ).toString() );
      if ( referencedCollectionParam == null ) {
        return referencedCollection;
      } else {
        return referencedCollection.narrow( referencedCollectionParam );
      }
    }

    return ref( TypeUtils.getErasure( type ).toString() );
  }

  @Nullable
  private JClass refCollectionParam( @Nonnull TypeMirror collectionType ) {
    try {
      TypeMirror collectionParamType = TypeUtils.getCollectionParam( collectionType );
      if ( TypeUtils.isWildcardType( collectionParamType ) ) {
        return ref( TypeUtils.getErasure( collectionParamType ).toString() ).wildcard();
      } else {
        return ref( collectionParamType.toString() );
      }
    } catch ( NotFoundException ignore ) {
      return null;
    }
  }

  public boolean isPrimitiveType( @Nonnull TypeMirror type ) {
    throw new UnsupportedOperationException();
  }
}
