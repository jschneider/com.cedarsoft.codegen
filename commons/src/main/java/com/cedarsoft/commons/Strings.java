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

package com.cedarsoft.commons;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.lang.String;

/**
 * Utility method for string operations
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class Strings {
  private Strings() {
  }

  /**
   * <p>stripQuotes</p>
   *
   * @param value a {@link String} object.
   * @return a {@link String} object.
   */
  @Nonnull
  public static String stripQuotes( @Nonnull String value ) {
    if ( value.indexOf( '\"' ) == 0 ) {
      value = value.substring( 1 );
    }

    if ( value.endsWith( "\"" ) ) {
      value = value.substring( 0, value.length() - 1 );
    }

    return value;
  }

  /**
   * Cuts the uncut to the given maxlength
   *
   * @param uncut     the uncut
   * @param maxLength the maxlength
   * @return the cut uncut
   */
  @Nonnull
  public static String cut( @Nonnull String uncut, int maxLength ) {
    if ( uncut.length() > maxLength ) {
      return uncut.substring( 0, maxLength );
    } else {
      return uncut;
    }
  }

  /**
   * Cuts the given uncut
   *
   * @param uncut     the uncut
   * @param maxLength the maxlength
   * @return the cut uncut or null if the given uncut has been null
   */
  @Nullable
  public static String cutNull( @Nullable String uncut, int maxLength ) {
    if ( uncut == null ) {
      return null;
    }
    return cut( uncut, maxLength );
  }
}
