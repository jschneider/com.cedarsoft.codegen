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

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * An extended type of Boolean that has a third option: {@link #UNKNOWN}.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public enum ExtendedBoolean {
  UNKNOWN(),
  TRUE(),
  FALSE();

  /**
   * <p>isTrue</p>
   *
   * @return a boolean.
   */
  public boolean isTrue() {
    return this == TRUE;
  }

  /**
   * <p>isFalse</p>
   *
   * @return a boolean.
   */
  public boolean isFalse() {
    return this == FALSE;
  }

  /**
   * <p>isUnknown</p>
   *
   * @return a boolean.
   */
  public boolean isUnknown() {
    return this == UNKNOWN;
  }

  /**
   * Returns the description for the default locale
   *
   * @return the description for the default locale
   */
  @Nonnull
  public String getDescription() {
    return getDescription( Locale.getDefault() );
  }

  /**
   * Returns the description for the given locale
   *
   * @param locale the locale
   * @return the description for the locale
   */
  @Nonnull
  public String getDescription( @Nonnull Locale locale ) {
    ResourceBundle bundle = ResourceBundle.getBundle( getClass().getName(), locale );
    return bundle.getString( name() );
  }

  /**
   * Returns the ExtendedBoolean for a boolean
   *
   * @param value the boolean value
   * @return {@link #TRUE} or {@link #FALSE} - depending on the given value
   */
  @Nonnull
  public static ExtendedBoolean valueOf( boolean value ) {
    if ( value ) {
      return TRUE;
    } else {
      return FALSE;
    }
  }
}
