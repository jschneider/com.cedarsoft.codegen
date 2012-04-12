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

import com.cedarsoft.codegen.ConstructorCallInfo;
import com.cedarsoft.codegen.TypeUtils;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.ConstructorDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.type.TypeMirror;

import javax.annotation.Nonnull;

import java.util.Collection;

/**
 *
 */
public class DomainObjectDescriptorFactory {
  @Nonnull
  public static final String JAVA_LANG_OBJECT = "java.lang.Object";
  @Nonnull
  private final ClassDeclaration classDeclaration;

  public DomainObjectDescriptorFactory( @Nonnull ClassDeclaration classDeclaration ) {
    this.classDeclaration = classDeclaration;
  }

  @Nonnull
  public DomainObjectDescriptor create() {
    DomainObjectDescriptor domainObjectDescriptor = new DomainObjectDescriptor( classDeclaration );

    Collection<FieldDeclaration> fields = TypeUtils.findFieldsIncludingSuperClasses( classDeclaration);
    for ( FieldDeclaration fieldDeclaration : fields ) {
      if ( TypeUtils.isStatic( fieldDeclaration ) ) {
        continue;
      }
      FieldWithInitializationInfo info = getFieldWithInitializationInfo( fieldDeclaration );
      domainObjectDescriptor.addField( info );
    }

    return domainObjectDescriptor;
  }

  @Nonnull
  public FieldWithInitializationInfo getFieldWithInitializationInfo( @Nonnull FieldDeclaration fieldDeclaration ) {
    MethodDeclaration getterDeclaration = TypeUtils.findGetterForField( classDeclaration, fieldDeclaration );

    //At first look for fields that are initialized within the constructor
    try {
      ConstructorCallInfo constructorCallInfo = findConstructorCallInfoForField( fieldDeclaration );
      return new FieldInitializedInConstructorInfo( fieldDeclaration, getterDeclaration, constructorCallInfo );
    } catch ( IllegalArgumentException ignore ) {
    }

    //Now look for fields that have a setter
    try {
      MethodDeclaration setter = TypeUtils.findSetter( classDeclaration, fieldDeclaration );
      return new FieldInitializedInSetterInfo( fieldDeclaration, getterDeclaration, setter );
    } catch ( IllegalArgumentException ignore ) {
    }

    //Ok, return a read only field descriptor
    return new FieldNotInitializationInfo( fieldDeclaration, getterDeclaration );
  }

  @Nonnull
  public ConstructorCallInfo findConstructorCallInfoForField( @Nonnull FieldDeclaration fieldDeclaration ) throws IllegalArgumentException {
    return findConstructorCallInfoForField( fieldDeclaration.getSimpleName(), fieldDeclaration.getType() );
  }

  @Nonnull
  public ConstructorCallInfo findConstructorCallInfoForField( @Nonnull String simpleName, @Nonnull TypeMirror type ) throws IllegalArgumentException {
    ConstructorDeclaration constructorDeclaration = TypeUtils.findBestConstructor( classDeclaration );

    int index = 0;

    for ( ParameterDeclaration parameterDeclaration : constructorDeclaration.getParameters() ) {
      if ( parameterDeclaration.getSimpleName().equals( simpleName ) ) {
        //Found a fitting type
        if ( TypeUtils.mightBeConstructorCallFor( parameterDeclaration.getType(), type ) ) {
          return new ConstructorCallInfo( constructorDeclaration, index, parameterDeclaration );
        } else {
          throw new IllegalArgumentException( "Type mismatch for <" + simpleName + ">. Was <" + parameterDeclaration.getType() + "> but expected <" + type + ">" );
        }
      }
      index++;
    }

    throw new IllegalArgumentException( "No parameter found that fits <" + simpleName + ">" );
  }

  @Nonnull
  public FieldInitializedInConstructorInfo findFieldInitializedInConstructor( @Nonnull String simpleName ) {
    FieldDeclaration fieldDeclaration = TypeUtils.findFieldDeclaration( classDeclaration, simpleName );
    return getFieldInitializeInConstructorInfo( fieldDeclaration );
  }

  @Nonnull
  public FieldInitializedInConstructorInfo getFieldInitializeInConstructorInfo( @Nonnull FieldDeclaration fieldDeclaration ) {
    ConstructorCallInfo constructorCallInfo = findConstructorCallInfoForField( fieldDeclaration );
    MethodDeclaration getterDeclaration = TypeUtils.findGetterForField( classDeclaration, fieldDeclaration );
    return new FieldInitializedInConstructorInfo( fieldDeclaration, getterDeclaration, constructorCallInfo );
  }

  @Nonnull
  public ClassDeclaration getClassDeclaration() {
    return classDeclaration;
  }
}
