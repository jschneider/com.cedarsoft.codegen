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

package com.cedarsoft.file;


import javax.annotation.Nonnull;

import java.lang.String;
import java.util.Locale;

/**
 * An extension of a file.
 * The case does *not* matter(!)
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class Extension {
  /**
   * Constant <code>DEFAULT_DELIMITER="."</code>
   */
  @Nonnull
  public static final String DEFAULT_DELIMITER = ".";
  /**
   * Constant <code>NONE</code>
   */
  @Nonnull
  public static final Extension NONE = new Extension( "", "" );

  @Nonnull
  private final String delimiter;
  @Nonnull
  private final String extension;

  /**
   * <p>Constructor for Extension.</p>
   *
   * @param extension a {@link String} object.
   */
  public Extension( @Nonnull String extension ) {
    this( DEFAULT_DELIMITER, extension );
  }

  /**
   * <p>Constructor for Extension.</p>
   *
   * @param delimiter a {@link String} object.
   * @param extension a {@link String} object.
   */
  public Extension( @Nonnull String delimiter, @Nonnull String extension ) {
    this.delimiter = delimiter;
    this.extension = extension; //We only accept lower case extensions
  }

  /**
   * <p>Getter for the field <code>delimiter</code>.</p>
   *
   * @return a {@link String} object.
   */
  @Nonnull
  public String getDelimiter() {
    return delimiter;
  }

  /**
   * <p>Getter for the field <code>extension</code>.</p>
   *
   * @return a {@link String} object.
   */
  @Nonnull
  public String getExtension() {
    return extension;
  }

  /**
   * <p>getCombined</p>
   *
   * @return a {@link String} object.
   */
  @Nonnull
  public String getCombined() {
    return delimiter + extension;
  }

  @Nonnull
  public Extension createCaseSensitiveExtension( @Nonnull String fileName ) {
    int extensionIndex = fileName.toLowerCase( Locale.US ).lastIndexOf( getExtension().toLowerCase( Locale.US ) );
    return new Extension( getDelimiter(), fileName.substring( extensionIndex ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return getCombined();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof Extension ) ) return false;

    Extension extension1 = ( Extension ) o;

    if ( !delimiter.equals( extension1.delimiter ) ) return false;
    if ( !extension.equalsIgnoreCase( extension1.extension ) ) return false;

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = delimiter.hashCode();
    result = 31 * result + extension.toLowerCase( Locale.US ).hashCode();
    return result;
  }
}
