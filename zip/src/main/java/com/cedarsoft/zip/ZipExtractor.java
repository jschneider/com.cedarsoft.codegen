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

package com.cedarsoft.zip;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.compress.archivers.zip.ZipArchiveInputStream;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Extracts ZIP files
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ZipExtractor {
  private static final int BUFFER_LENGTH = 1024;

  @Nullable
  private final Condition condition;

  /**
   * <p>Constructor for ZipExtractor.</p>
   */
  public ZipExtractor() {
    this( null );
  }

  /**
   * <p>Constructor for ZipExtractor.</p>
   *
   * @param condition a {@link ZipExtractor.Condition} object.
   */
  public ZipExtractor( @Nullable Condition condition ) {
    this.condition = condition;
  }

  /**
   * Extract the zip file to the given destination
   *
   * @param destination the destination the file will be extracted to
   * @param inputStream the input stream providing the zipped content
   * @throws IOException if any.
   */
  public void extract( @Nonnull File destination, @Nonnull final InputStream inputStream ) throws IOException {
    if ( !destination.exists() || !destination.isDirectory() ) {
      throw new IllegalArgumentException( "Invalid destination: " + destination.getCanonicalPath() );
    }

    ZipArchiveInputStream zipInputStream = new ZipArchiveInputStream( inputStream );
    try {

      byte[] buf = new byte[BUFFER_LENGTH];
      for ( ArchiveEntry zipEntry = zipInputStream.getNextEntry(); zipEntry != null; zipEntry = zipInputStream.getNextEntry() ) {
        if ( condition != null && !condition.shallExtract( zipEntry ) ) {
          continue;
        }

        String entryName = zipEntry.getName();
        File newFile = new File( destination, entryName );

        //Is a directory
        if ( zipEntry.isDirectory() ) {
          newFile.mkdirs();
          continue;
        }

        //Make the directory structure
        newFile.getParentFile().mkdirs();

        FileOutputStream fileoutputstream = new FileOutputStream( newFile );
        try {
          int n;
          while ( ( n = zipInputStream.read( buf, 0, BUFFER_LENGTH ) ) > -1 ) {
            fileoutputstream.write( buf, 0, n );
          }
        } finally {
          fileoutputstream.close();
        }
      }
    } finally {
      zipInputStream.close();
    }
  }

  /**
   * Returns the currently set condition
   *
   * @return the condition
   */
  @Nullable
  public Condition getCondition() {
    return condition;
  }

  /**
   * A condition can decide whether a given zip entry shall be extracted or not
   */
  public interface Condition {
    /**
     * Returns whether the given entry shall be extracted
     *
     * @param zipEntry the zip entry
     * @return whether the entry shall be extracted
     */
    boolean shallExtract( @Nonnull ArchiveEntry zipEntry );
  }

  /**
   * Inverts a condition
   */
  public static class InvertedCondition implements Condition {
    private final Condition condition;

    /**
     * Creates a new inverted condition
     *
     * @param condition the delegate that is inverted
     */
    public InvertedCondition( @Nonnull Condition condition ) {
      this.condition = condition;
    }

    @Override
    public boolean shallExtract( @Nonnull ArchiveEntry zipEntry ) {
      return !condition.shallExtract( zipEntry );
    }
  }
}
