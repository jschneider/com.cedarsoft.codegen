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

package com.cedarsoft.io;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * <p>LinkUtils class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class LinkUtils {
  /**
   * Returns whether the given file is a link
   *
   * @param file a {@link File} object.
   * @return whether the given file is a sym link
   *
   * @throws IOException if any.
   */
  public static boolean isLink( @Nonnull File file ) throws IOException {
    if ( !file.exists() ) {
      throw new FileNotFoundException( file.getAbsolutePath() );
    }

    @Nonnull
    String canonicalPath = file.getCanonicalPath();
    @Nonnull
    String absolutePath = file.getAbsolutePath();
    return !absolutePath.equals( canonicalPath );
  }

  /**
   * Creates a link
   *
   * @param linkTarget the link source
   * @param linkFile   the link file
   * @param linkType   the type of link
   * @return whether the link has been created
   *
   * @throws IOException if any.
   */
  public static boolean createLink( @Nonnull File linkTarget, @Nonnull File linkFile, @Nonnull LinkType linkType ) throws IOException {
    return createLink( linkTarget, linkFile, linkType == LinkType.SYMBOLIC );
  }

  /**
   * Creates a symbolik link
   *
   * @param linkTarget the link source
   * @param linkFile   the link file
   * @return whether the link has been created
   *
   * @throws IOException if any.
   */
  public static boolean createSymbolicLink( @Nonnull File linkTarget, @Nonnull File linkFile ) throws IOException {
    return createLink( linkTarget, linkFile, true );
  }

  /**
   * Creates a hard link
   *
   * @param linkTarget the link source
   * @param linkFile   the link file
   * @return whether the link has been created
   *
   * @throws IOException if any.
   */
  public static boolean createHardLink( @Nonnull File linkTarget, @Nonnull File linkFile ) throws IOException {
    return createLink( linkTarget, linkFile, false );
  }

  /**
   * Creates a link.
   * Returns true if the link has been created, false if the link (with the same link source) still exists.
   *
   * @param linkTarget the link source
   * @param linkFile   the link file
   * @param symbolic   whether to create a symbolic link
   * @return whether the link has been created (returns false if the link still existed)
   *
   * @throws IOException if something went wrong
   */
  public static boolean createLink( @Nonnull File linkTarget, @Nonnull File linkFile, boolean symbolic ) throws IOException {
    if ( linkFile.exists() ) {
      //Maybe the hard link still exists - we just don't know, so throw an exception
      if ( !symbolic ) {
        throw new IOException( "link still exists " + linkFile.getAbsolutePath() );
      }

      if ( linkFile.getCanonicalFile().equals( linkTarget.getCanonicalFile() ) ) {
        //still exists - that is ok, since it points to the same directory
        return false;
      } else {
        //Other target
        throw new IOException( "A link still exists at <" + linkFile.getAbsolutePath() + "> but with different target: <" + linkTarget.getCanonicalPath() + "> exected <" + linkFile.getCanonicalPath() + ">" );
      }
    }

    List<String> args = new ArrayList<String>();
    args.add( "ln" );
    if ( symbolic ) {
      args.add( "-s" );
    }
    args.add( linkTarget.getPath() );
    args.add( linkFile.getAbsolutePath() );

    ProcessBuilder builder = new ProcessBuilder( args );
    Process process = builder.start();
    try {
      int result = process.waitFor();
      if ( result != 0 ) {
        throw new IOException( "Creation of link failed: " + IOUtils.toString( process.getErrorStream() ) );
      }
    } catch ( InterruptedException e ) {
      throw new RuntimeException( e );
    }

    return true;
  }

  /**
   * Deletes the symbolic link
   *
   * @param linkFile the link file
   * @throws IOException if any.
   */
  public static void deleteSymbolicLink( @Nonnull File linkFile ) throws IOException {
    if ( !linkFile.exists() ) {
      throw new FileNotFoundException( "No such symlink: " + linkFile );
    }
    // find the resource of the existing link:
    File canonicalFile = linkFile.getCanonicalFile();

    // rename the resource, thus breaking the link:
    File temp = createTempFile( "symlink", ".tmp", canonicalFile.getParentFile() );
    try {
      try {
        FileUtils.moveFile( canonicalFile, temp );
      } catch ( IOException e ) {
        throw new IOException(
          "Couldn't rename resource when attempting to delete "
            + linkFile );
      }
      // delete the (now) broken link:
      if ( !linkFile.delete() ) {
        throw new IOException( "Couldn't delete symlink: " + linkFile
          + " (was it a real file? is this not a UNIX system?)" );
      }
    } finally {
      // return the resource to its original name:
      try {
        FileUtils.moveFile( temp, canonicalFile );
      } catch ( IOException e ) {
        throw new IOException( "Couldn't return resource " + temp
          + " to its original name: " + canonicalFile.getAbsolutePath()
          + "\n THE RESOURCE'S NAME ON DISK HAS "
          + "BEEN CHANGED BY THIS ERROR!\n" );
      }
    }
  }

  /**
   * Creates a temporary file
   *
   * @param prefix    the prefix
   * @param suffix    the suffix
   * @param parentDir the parent dir
   * @return the created file
   */
  @Nonnull
  public static File createTempFile( @Nonnull String prefix, @Nonnull String suffix, @Nullable File parentDir ) {
    Random rand = new Random();

    String parent = parentDir == null ? System.getProperty( "java.io.tmpdir" ) : parentDir.getPath();
    DecimalFormat fmt = new DecimalFormat( "#####" );

    File result;
    do {
      result = new File( parent, prefix + fmt.format( Math.abs( rand.nextInt() ) ) + suffix );
    } while ( result.exists() );
    return result;
  }


  /**
   * Checks whether a given file is a symbolic link.
   *
   * @param file the file
   * @return whether the given file is a symbolic link
   *
   * @throws IOException if any.
   */
  public boolean isSymbolicLink( @Nonnull File file ) throws IOException {
    return !file.getAbsoluteFile().equals( file.getCanonicalFile() );
  }
}
