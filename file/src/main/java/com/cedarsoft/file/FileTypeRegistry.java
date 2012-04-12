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

import com.cedarsoft.exceptions.StillContainedException;
import com.cedarsoft.registry.DefaultRegistry;
import com.cedarsoft.registry.RegistryFactory;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.lang.String;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;

/**
 * <p>FileTypeRegistry class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class FileTypeRegistry extends DefaultRegistry<FileType> {
  /**
   * Constant <code>LIGHT_ZONE</code>
   */
  @Nonnull
  public static final FileType LIGHT_ZONE = new FileType( "LightZone", "application/lightzone", true, new Extension( "_", "lzn.jpg" ) );
  /**
   * Constant <code>JPEG</code>
   */
  @Nonnull
  public static final FileType JPEG = new FileType( "JPEG", "image/jpeg",false, new Extension( ".", "jpg" ), new Extension( ".", "jpeg" ) );
  /**
   * Constant <code>TIFF</code>
   */
  @Nonnull
  public static final FileType TIFF = new FileType( "TIFF", "image/tiff",false, new Extension( ".", "tif" ), new Extension( ".", "tiff" ) );
  /**
   * Constant <code>GIMP</code>
   */
  @Nonnull
  public static final FileType GIMP = new FileType( "Gimp", "image/xcf",false, new Extension( ".", "xcf" ) );
  /**
   * Constant <code>PHOTO_SHOP</code>
   */
  @Nonnull
  public static final FileType PHOTO_SHOP = new FileType( "Photoshop", "image/psd", false, new Extension( ".", "psd" ) );
  /**
   * Constant <code>RAW_CANON</code>
   */
  @Nonnull
  public static final FileType RAW_CANON = new FileType( "CanonRaw", "image/cr2", false, new Extension( ".", "cr2" ) );

  @Nonnull
  private static final List<FileType> DEFAULT = Arrays.asList( LIGHT_ZONE, JPEG, TIFF, GIMP, RAW_CANON, PHOTO_SHOP );

  /**
   * <p>Constructor for FileTypeRegistry.</p>
   */
  @Deprecated
  public FileTypeRegistry() {
    this( true );
  }

  /**
   * <p>Constructor for FileTypeRegistry.</p>
   *
   * @param registerDefaultTypes a boolean.
   */
  @Deprecated
  public FileTypeRegistry( boolean registerDefaultTypes ) {
    if ( registerDefaultTypes ) {
      ensureDefaultTypesRegistered();
    }
  }

  /**
   * <p>Constructor for FileTypeRegistry.</p>
   *
   * @param storedObjects      a {@link Collection} object.
   * @param fileTypeComparator a {@link Comparator} object.
   * @throws StillContainedException
   *          if any.
   */
  public FileTypeRegistry( @Nonnull Collection<? extends FileType> storedObjects, @Nullable Comparator<FileType> fileTypeComparator ) throws StillContainedException {
    super( storedObjects, fileTypeComparator );
  }

  /**
   * Returns the file types
   *
   * @return the file types
   */
  @Nonnull
  public List<? extends FileType> getFileTypes() {
    return getStoredObjects();
  }

  /**
   * Ensures that the default types are registered
   */
  public final void ensureDefaultTypesRegistered() {
    lock.writeLock().lock();
    try {
      if ( !getStoredObjects().isEmpty() ) {
        return;
      }

      //Register the default types
      for ( FileType fileType : DEFAULT ) {
        try {
          store( fileType );
        } catch ( StillContainedException ignore ) {
        }
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>valueOf</p>
   *
   * @param id a {@link String} object.
   * @return a {@link FileType} object.
   */
  @Nonnull
  public FileType valueOf( @Nonnull final String id ) {
    return findStoredObject( new Matcher<FileType>() {
      @Override
      public boolean matches( @Nonnull FileType object ) {
        return object.getId().equals( id );
      }
    }, "No FileType found for <" + id + '>' );
  }

  /**
   * <p>get</p>
   *
   * @param fileName a {@link FileName} object.
   * @return a {@link FileType} object.
   */
  @Nonnull
  public FileType get( @Nonnull final FileName fileName ) {
    return findStoredObject( new Matcher<FileType>() {
      @Override
      public boolean matches( @Nonnull FileType object ) {
        return object.matches( fileName );
      }
    }, "No FileType found for file <" + fileName + '>' );
  }

  /**
   * <p>get</p>
   *
   * @param fileName a {@link String} object.
   * @return a {@link FileType} object.
   */
  @Nonnull
  public FileType get( @Nonnull final String fileName ) {
    return findStoredObject( new Matcher<FileType>() {
      @Override
      public boolean matches( @Nonnull FileType object ) {
        return object.matches( fileName );
      }
    }, "No FileType found for file <" + fileName + '>' );
  }

  /**
   * Parses a file name
   *
   * @param fileName the file name to parse
   * @return the file name
   */
  @Nonnull
  public FileName parseFileName( @Nonnull String fileName ) {
    FileType type = get( fileName );
    return type.getFileName( fileName );
  }

  public static class Factory implements RegistryFactory<FileType, FileTypeRegistry> {
    @Nonnull
    @Override
    public FileTypeRegistry createRegistry( @Nonnull List<? extends FileType> objects, @Nonnull Comparator<FileType> comparator ) {
      return new FileTypeRegistry( objects, comparator );
    }
  }
}
