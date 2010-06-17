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

import com.cedarsoft.codegen.NamingSupport;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import com.sun.mirror.declaration.TypeDeclaration;
import com.sun.mirror.type.DeclaredType;
import com.sun.mirror.type.InterfaceType;
import com.sun.mirror.type.TypeMirror;
import com.sun.mirror.util.Types;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;

/**
 * Offers utility methods related to types
 */
public class TypeUtils {
  private TypeUtils() {
  }

  @NotNull
  private static final ThreadLocal<Types> TYPES = new ThreadLocal<Types>();

  @NotNull
  public static Types getTypes() {
    Types resolved = TYPES.get();
    if ( resolved == null ) {
      throw new IllegalStateException( "No types object found!" );
    }
    return resolved;
  }

  public static void setTypes( @NotNull Types types ) {
    TYPES.set( types );
  }

  public static TypeMirror getErasure( @NotNull TypeMirror type ) {
    return getTypes().getErasure( type );
  }

  public static TypeMirror getCollectionParam( @NotNull TypeMirror type ) {
    if ( !( type instanceof DeclaredType ) ) {
      throw new IllegalStateException( "Invalid type: " + type );
    }

    TypeDeclaration declaredType = ( ( DeclaredType ) type ).getDeclaration();
    if ( declaredType == null ) {
      throw new IllegalStateException( "No declaration found for <" + type + ">" );
    }

    if ( declaredType.getQualifiedName().equals( Collection.class.getName() ) ) {
      return getFirstTypeParam( ( DeclaredType ) type );
    }

    for ( InterfaceType interfaceType : declaredType.getSuperinterfaces() ) {
      if ( interfaceType.getDeclaration().getQualifiedName().equals( Collection.class.getName() ) ) {
        return getFirstTypeParam( ( DeclaredType ) type );
      }
    }

    throw new IllegalStateException( "Invalid type: " + type );
  }

  @NotNull
  private static TypeMirror getFirstTypeParam( @NotNull DeclaredType type ) {
    Collection<TypeMirror> typeArguments = type.getActualTypeArguments();
    if ( typeArguments.size() != 1 ) {
      throw new IllegalStateException( "Invalid type arguments: " + typeArguments );
    }

    return typeArguments.iterator().next();
  }

  public static boolean isCollectionType( @NotNull TypeMirror type ) {
    try {
      getCollectionParam( type );
      return true;
    } catch ( IllegalStateException ignore ) {
      return false;
    }
  }

  public static boolean isAssignable( TypeMirror t1, TypeMirror t2 ) {
    return getTypes().isAssignable( t1, t2 );
  }

  public static boolean mightBeConstructorCallFor( @NotNull TypeMirror parameterType, @NotNull TypeMirror fieldType ) {
    return isAssignable( parameterType, fieldType ) || isAssignable( fieldType, parameterType );
  }

  @NotNull
  public static MethodDeclaration findSetter( @NotNull ClassDeclaration classDeclaration, @NotNull FieldDeclaration fieldDeclaration ) {
    return findSetter( classDeclaration, fieldDeclaration.getSimpleName(), fieldDeclaration.getType() );
  }

  @NotNull
  public static MethodDeclaration findSetter( @NotNull ClassDeclaration classDeclaration, @NotNull @NonNls String fieldName, @NotNull TypeMirror type ) throws IllegalArgumentException {
    String expectedName = NamingSupport.createSetter( fieldName );

    for ( MethodDeclaration methodDeclaration : classDeclaration.getMethods() ) {
      if ( !methodDeclaration.getSimpleName().equals( expectedName ) ) {
        continue;
      }

      if ( methodDeclaration.getParameters().size() != 1 ) {
        throw new IllegalArgumentException( "Expected one parameter. But was <" + methodDeclaration.getParameters() + ">" );
      }

      ParameterDeclaration parameterDeclaration = methodDeclaration.getParameters().iterator().next();
      if ( !isAssignable( type, parameterDeclaration.getType() ) ) {
        throw new IllegalArgumentException( "Invalid parameter type for <" + expectedName + ">. Was <" + parameterDeclaration.getType() + "> but expected <" + type + ">" );
      }

      return methodDeclaration;
    }

    throw new IllegalArgumentException( "No method declaration found for <" + expectedName + ">" );
  }

  public static MethodDeclaration findGetterForField( @NotNull ClassDeclaration classDeclaration, @NotNull FieldDeclaration fieldDeclaration ) {
    return findGetterForField( classDeclaration, fieldDeclaration.getSimpleName(), fieldDeclaration.getType() );
  }

  /**
   * @param classDeclaration the class declaration
   * @param simpleName       the simple name
   * @param type             the type
   * @return the getter declaration
   *
   * @noinspection TypeMayBeWeakened
   */
  public static MethodDeclaration findGetterForField( @NotNull ClassDeclaration classDeclaration, @NotNull @NonNls String simpleName, @NotNull TypeMirror type ) {
    String expectedName = "get" + simpleName.substring( 0, 1 ).toUpperCase() + simpleName.substring( 1 );

    for ( MethodDeclaration methodDeclaration : classDeclaration.getMethods() ) {
      if ( methodDeclaration.getSimpleName().equals( expectedName ) ) {
        TypeMirror returnType = methodDeclaration.getReturnType();
        if ( isAssignable( type, returnType ) ) {
          return methodDeclaration;
        } else {
          throw new IllegalArgumentException( "Invalid return types for <" + expectedName + ">. Was <" + returnType + "> but expected <" + type + ">" );
        }
      }
    }

    throw new IllegalArgumentException( "No method declaration found for <" + expectedName + ">" );
  }


  /**
   * @param classDeclaration the class declaration
   * @param fieldName        the field name
   * @return the field declaration
   *
   * @noinspection TypeMayBeWeakened
   */
  @NotNull
  public static FieldDeclaration findFieldDeclaration( @NotNull ClassDeclaration classDeclaration, @NotNull @NonNls String fieldName ) {
    for ( FieldDeclaration fieldDeclaration : classDeclaration.getFields() ) {
      if ( fieldDeclaration.getSimpleName().equals( fieldName ) ) {
        return fieldDeclaration;
      }
    }

    throw new IllegalArgumentException( "No field declaration found for <" + fieldName + ">" );
  }

  public static boolean isType( @NotNull TypeMirror typeMirror, @NotNull Class<?> type ) {
    @NonNls
    String typeAsName = type.getName();
    return typeMirror.toString().equals( typeAsName );
  }
}
