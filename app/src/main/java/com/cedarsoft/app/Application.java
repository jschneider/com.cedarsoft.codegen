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

package com.cedarsoft.app;

import com.cedarsoft.Version;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * Informations about an
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class Application {
  @NotNull
  @NonNls
  private final String name;
  @NotNull
  private final Version version;

  /**
   * <p>Constructor for Application.</p>
   *
   * @param name a {@link java.lang.String} object.
   * @param version a {@link com.cedarsoft.Version} object.
   */
  public Application( @NotNull String name, @NotNull Version version ) {
    this.name = name;
    this.version = version;
  }

  /**
   * <p>Getter for the field <code>name</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  @NotNull
  public String getName() {
    return name;
  }

  /**
   * <p>Getter for the field <code>version</code>.</p>
   *
   * @return a {@link com.cedarsoft.Version} object.
   */
  @NotNull
  public Version getVersion() {
    return version;
  }

  /** {@inheritDoc} */
  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof Application ) ) return false;

    Application that = ( Application ) o;

    if ( !name.equals( that.name ) ) return false;
    if ( !version.equals( that.version ) ) return false;

    return true;
  }

  /** {@inheritDoc} */
  @Override
  public int hashCode() {
    int result = name.hashCode();
    result = 31 * result + version.hashCode();
    return result;
  }

  /** {@inheritDoc} */
  @NonNls
  @Override
  public String toString() {
    return name + " (" + version + ')';
  }
}
