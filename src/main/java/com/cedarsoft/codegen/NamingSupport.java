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


import javax.annotation.Nonnull;

/**
 *
 */
public class NamingSupport {
  private NamingSupport() {

  }

  @Nonnull
  public static String createXmlElementName( @Nonnull String simpleClassName ) {
    return simpleClassName.toLowerCase();
  }

  @Nonnull
  public static String createVarName( @Nonnull String simpleClassName ) {
    if ( simpleClassName.length() == 0 ) {
      throw new IllegalArgumentException( "Invalid class name: Is empty" );
    }

    return simpleClassName.substring( 0, 1 ).toLowerCase() + simpleClassName.substring( 1 );
  }

  @Nonnull
  public static String createSetter( @Nonnull String fieldName ) {
    return "set" + fieldName.substring( 0, 1 ).toUpperCase() + fieldName.substring( 1 );
  }

  @Nonnull
  public static String createGetterName( @Nonnull String simpleName ) {
    return "get" + simpleName.substring( 0, 1 ).toUpperCase() + simpleName.substring( 1 );
  }

  @Nonnull
  public static String plural( @Nonnull String singular ) {
    if ( singular.endsWith( "s" ) || singular.endsWith( "ch" ) || singular.endsWith( "x" ) ) {
      return singular + "es";
    }

    return singular + "s";
  }

  @Nonnull
  public static String createSingular( @Nonnull String plural ) {
    if ( !plural.endsWith( "s" ) ) {
      return plural;
    }

    if ( plural.endsWith( "ches" ) || plural.endsWith( "xes" ) ) {
      return plural.substring( 0, plural.length() - 2 );
    }

    return plural.substring( 0, plural.length() - 1 );
  }
}
