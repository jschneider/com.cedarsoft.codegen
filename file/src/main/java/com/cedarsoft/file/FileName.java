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

import java.io.File;
import java.lang.String;

/**
 * Represents a file name
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class FileName {
  @Nonnull
  private final BaseName baseName;
  @Nonnull
  private final Extension extension;

  /**
   * Creates a new file name
   *
   * @param baseName  the base name
   * @param extension the file extension
   */
  public FileName( @Nonnull String baseName, @Nonnull String extension ) {
    this( new BaseName( baseName ), new Extension( extension ) );
  }

  /**
   * Creates a new file name
   *
   * @param baseName  the base name
   * @param extension the extension
   */
  public FileName( @Nonnull String baseName, @Nonnull Extension extension ) {
    this( new BaseName( baseName ), extension );
  }

  /**
   * <p>Constructor for FileName.</p>
   *
   * @param baseName  a {@link String} object.
   * @param delimiter a {@link String} object.
   * @param extension a {@link String} object.
   */
  public FileName( @Nonnull String baseName, @Nonnull String delimiter, @Nonnull String extension ) {
    this( new BaseName( baseName ), new Extension( delimiter, extension ) );
  }

  /**
   * Creates a file name
   *
   * @param baseName  the base name
   * @param delimiter the delimiter
   * @param extension the extension
   */
  public FileName( @Nonnull BaseName baseName, @Nonnull String delimiter, @Nonnull String extension ) {
    this( baseName, new Extension( delimiter, extension ) );
  }

  /**
   * Creates a file name
   *
   * @param baseName  the base name
   * @param extension the extension
   */
  public FileName( @Nonnull BaseName baseName, @Nonnull Extension extension ) {
    this.baseName = baseName;
    this.extension = extension;
  }

  /**
   * <p>Getter for the field <code>baseName</code>.</p>
   *
   * @return a {@link BaseName} object.
   */
  @Nonnull
  public BaseName getBaseName() {
    return baseName;
  }

  /**
   * <p>Getter for the field <code>extension</code>.</p>
   *
   * @return a {@link Extension} object.
   */
  @Nonnull
  public Extension getExtension() {
    return extension;
  }

  /**
   * <p>getName</p>
   *
   * @return a {@link String} object.
   */
  @Nonnull
  public String getName() {
    return baseName + extension.getCombined();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof FileName ) ) return false;

    FileName fileName = ( FileName ) o;

    if ( !baseName.equals( fileName.baseName ) ) return false;
    if ( !extension.equals( fileName.extension ) ) return false;

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = baseName.hashCode();
    result = 31 * result + extension.hashCode();
    return result;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return baseName.toString() + extension.toString();
  }

  /**
   * Returns the corresponding file
   *
   * @param baseDir the base dir
   * @return the file represented
   */
  @Nonnull
  public File getFile( @Nonnull File baseDir ) {
    assert baseDir.isDirectory();
    return new File( baseDir, getName() );
  }
}
