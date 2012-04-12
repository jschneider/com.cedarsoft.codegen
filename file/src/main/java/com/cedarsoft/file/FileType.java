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
import javax.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * <p>FileType class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class FileType {

  @Nonnull
  private final List<Extension> extensions = new ArrayList<Extension>();

  @Nonnull
  private final String id;
  private final boolean dependentType;

  @Nonnull
  private final String contentType;

  /**
   * <p>Constructor for FileType.</p>
   *
   * @param id            a {@link String} object.
   * @param contentType   the content type (eg. image/jpeg)
   * @param dependentType a boolean.
   * @param extensions    a {@link Extension} object.
   */
  public FileType( @Nonnull String id, @Nonnull String contentType, boolean dependentType, @Nonnull Extension... extensions ) {
    this( id, contentType, dependentType, Arrays.<Extension>asList( extensions ) );
  }

  /**
   * <p>Constructor for FileType.</p>
   *
   * @param id            a {@link String} object.
   * @param contentType   the content type (eg. image/jpeg)
   * @param dependentType a boolean.
   * @param extensions    a {@link Collection} object.
   */
  public FileType( @Nonnull String id, @Nonnull String contentType, boolean dependentType, @Nonnull Collection<? extends Extension> extensions ) {
    this.contentType = contentType;
    this.dependentType = dependentType;
    if ( extensions.isEmpty() ) {
      throw new IllegalArgumentException( "Need at least one extension" );
    }
    this.id = id;
    this.extensions.addAll( extensions );
  }

  @Nonnull
  public String getContentType() {
    return contentType;
  }

  /**
   * <p>Getter for the field <code>extensions</code>.</p>
   *
   * @return a {@link List} object.
   */
  @Nonnull
  public List<? extends Extension> getExtensions() {
    return Collections.unmodifiableList( extensions );
  }

  /**
   * <p>matches</p>
   *
   * @param fileName a {@link String} object.
   * @return a boolean.
   */
  public boolean matches( @Nonnull String fileName ) {
    for ( @Nonnull Extension ex : extensions ) {
      if ( fileName.toLowerCase( Locale.US ).endsWith( ex.getCombined() ) ) {
        return true;
      }
    }
    return false;
  }

  /**
   * <p>matches</p>
   *
   * @param fileName a {@link FileName} object.
   * @return a boolean.
   */
  public boolean matches( @Nonnull FileName fileName ) {
    return matches( fileName.getName() );
  }

  /**
   * <p>getDefaultExtension</p>
   *
   * @return a {@link Extension} object.
   */
  @Nonnull
  public Extension getDefaultExtension() {
    return extensions.get( 0 );
  }

  /**
   * <p>Getter for the field <code>id</code>.</p>
   *
   * @return a {@link String} object.
   */
  @Nonnull
  public String getId() {
    return id;
  }

  /**
   * Returns whether this file is a dependent type.
   * A dependent file is a file that needs another file it depends on.
   *
   * @return whether this file is a dependent type
   */
  public boolean isDependentType() {
    return dependentType;
  }

  /**
   * <p>getFileName</p>
   *
   * @param file a {@link File} object.
   * @return a {@link FileName} object.
   */
  @Nonnull
  public FileName getFileName( @Nonnull File file ) {
    return getFileName( file.getName() );
  }

  /**
   * <p>getFileName</p>
   *
   * @param fileName a {@link String} object.
   * @return a {@link FileName} object.
   */
  @Nonnull
  public FileName getFileName( @Nonnull String fileName ) throws IllegalArgumentException {//$NON-NLS-1$
    @Nullable String bestBase = null;
    @Nullable Extension bestExtension = null;

    for ( @Nonnull Extension extension : extensions ) {
      int index = fileName.toLowerCase().indexOf( extension.getCombined() );
      if ( index < 0 ) {
        continue;
      }

      String base = fileName.substring( 0, index );
      if ( bestBase == null || base.length() < bestBase.length() ) {
        bestBase = base;
        bestExtension = extension.createCaseSensitiveExtension( fileName );
      }
    }

    if ( bestBase == null ) {
      throw new IllegalArgumentException( "Cannot get base for " + fileName );
    }

    return new FileName( bestBase, bestExtension );
  }

  /**
   * <p>getExtension</p>
   *
   * @param fileName a {@link String} object.
   * @return a {@link Extension} object.
   */
  @Nonnull
  public Extension getExtension( @Nonnull String fileName ) {
    return getFileName( fileName ).getExtension();
  }

  /**
   * <p>getBaseName</p>
   *
   * @param fileName a {@link String} object.
   * @return a {@link String} object.
   */
  @Nonnull
  public String getBaseName( @Nonnull String fileName ) {
    return getFileName( fileName ).getBaseName().getName();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "FileType{" +
      "id='" + id + '\'' +
      '}';
  }

  /**
   * <p>isDefaultExtension</p>
   *
   * @param extension a {@link Extension} object.
   * @return a boolean.
   */
  public boolean isDefaultExtension( @Nonnull Extension extension ) {
    return getDefaultExtension().equals( extension );
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof FileType ) ) return false;

    FileType fileType = ( FileType ) o;

    if ( !id.equals( fileType.id ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }
}
