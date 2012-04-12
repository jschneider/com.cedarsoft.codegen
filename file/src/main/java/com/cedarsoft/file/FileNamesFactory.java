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

import javax.inject.Inject;
import org.apache.commons.io.filefilter.FileFileFilter;
import javax.annotation.Nonnull;

import java.io.File;
import java.io.FileFilter;
import java.lang.Iterable;
import java.util.Arrays;

/**
 * <p>FileNamesFactory class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class FileNamesFactory {
  @Nonnull
  private final FileTypeRegistry fileTypeRegistry;

  /**
   * <p>Constructor for FileNamesFactory.</p>
   *
   * @param fileTypeRegistry a {@link FileTypeRegistry} object.
   */
  @Inject
  public FileNamesFactory( @Nonnull FileTypeRegistry fileTypeRegistry ) {
    this.fileTypeRegistry = fileTypeRegistry;
  }

  /**
   * <p>create</p>
   *
   * @param baseDir a {@link File} object.
   * @return a {@link FileNames} object.
   */
  @Nonnull
  public FileNames create( @Nonnull File baseDir ) {
    File[] files = listFiles( baseDir );
    return create( files );
  }

  /**
   * <p>create</p>
   *
   * @param sourceFiles an array of {@link File} objects.
   * @return a {@link FileNames} object.
   */
  @Nonnull
  public FileNames create( @Nonnull File[] sourceFiles ) {
    return create( Arrays.asList( sourceFiles ) );
  }

  /**
   * <p>create</p>
   *
   * @param sourceFiles a {@link Iterable} object.
   * @return a {@link FileNames} object.
   */
  public FileNames create( @Nonnull Iterable<? extends File> sourceFiles ) {
    FileNames fileNames = new FileNames();

    for ( File file : sourceFiles ) {
      FileName fileName = fileTypeRegistry.parseFileName( file.getName() );
      fileNames.add( fileName );
    }

    return fileNames;
  }

  /**
   * <p>createBaseNameAware</p>
   *
   * @param baseDir a {@link File} object.
   * @return a {@link BaseNameAwareFileNames} object.
   */
  @Nonnull
  public BaseNameAwareFileNames createBaseNameAware( @Nonnull File baseDir ) {
    return createBaseNameAware( listFiles( baseDir ) );
  }

  /**
   * <p>createBaseNameAware</p>
   *
   * @param sourceFiles an array of {@link File} objects.
   * @return a {@link BaseNameAwareFileNames} object.
   */
  @Nonnull
  public BaseNameAwareFileNames createBaseNameAware( @Nonnull File[] sourceFiles ) {
    return createBaseNameAware( Arrays.asList( sourceFiles ) );
  }

  /**
   * <p>createBaseNameAware</p>
   *
   * @param sourceFiles a {@link Iterable} object.
   * @return a {@link BaseNameAwareFileNames} object.
   */
  @Nonnull
  public BaseNameAwareFileNames createBaseNameAware( @Nonnull Iterable<? extends File> sourceFiles ) {
    BaseNameAwareFileNames report = new BaseNameAwareFileNames();

    for ( File sourceFile : sourceFiles ) {
      FileName fileName = fileTypeRegistry.parseFileName( sourceFile.getName() );
      report.add( fileName );
    }

    return report;
  }

  @Nonnull
  private File[] listFiles( @Nonnull File baseDir ) {
    if ( !baseDir.isDirectory() ) {
      throw new IllegalArgumentException( "Invalid base dir <" + baseDir.getAbsolutePath() + '>' );
    }

    return baseDir.listFiles( ( FileFilter ) FileFileFilter.FILE );
  }

}
