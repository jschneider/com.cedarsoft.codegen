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


import javax.annotation.Nonnull;

import java.io.File;
import java.lang.String;

/**
 * Resolves the relative path
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class RelativePathFinder {
  private static final char BACKSLASH = '\\';
  private static final char SLASH = '/';

  /**
   * Calculates the relative path
   *
   * @param target    the target path
   * @param base      the base (a directory)
   * @param separator the separator
   * @return the relative path pointing to the target (from the base)
   */
  public static String getRelativePath( @Nonnull final String target, @Nonnull final String base, @Nonnull final String separator ) {
    //
    // remove trailing file separator
    //
    @Nonnull
    String canonicalBase = base;
    if ( base.charAt( base.length() - 1 ) == SLASH || base.charAt( base.length() - 1 ) == BACKSLASH ) {
      canonicalBase = base.substring( 0, base.length() - 1 );
    }

    //
    // get canonical name of target and remove trailing separator
    //
    @Nonnull
    String canonicalTarget = target;

    if ( canonicalTarget.charAt( canonicalTarget.length() - 1 ) == SLASH || canonicalTarget.charAt( canonicalTarget.length() - 1 ) == BACKSLASH ) {
      canonicalTarget = canonicalTarget.substring( 0, canonicalTarget.length() - 1 );
    }

    if ( canonicalTarget.equals( canonicalBase ) ) {
      return ".";
    }

    //
    // see if the prefixes are the same
    //
    if ( canonicalBase.substring( 0, 2 ).equals( "\\\\" ) ) {
      //
      // UNC file name, if target file doesn't also start with same
      // server name, don't go there
      int endPrefix = canonicalBase.indexOf( BACKSLASH, 2 );
      @Nonnull
      String prefix1 = canonicalBase.substring( 0, endPrefix );
      @Nonnull
      String prefix2 = canonicalTarget.substring( 0, endPrefix );
      if ( !prefix1.equals( prefix2 ) ) {
        return canonicalTarget;
      }
    } else {
      if ( canonicalBase.substring( 1, 3 ).equals( ":\\" ) ) {
        int endPrefix = 2;
        @Nonnull
        String prefix1 = canonicalBase.substring( 0, endPrefix );
        @Nonnull
        String prefix2 = canonicalTarget.substring( 0, endPrefix );
        if ( !prefix1.equals( prefix2 ) ) {
          return canonicalTarget;
        }
      } else {
        if ( canonicalBase.charAt( 0 ) == SLASH ) {
          if ( canonicalTarget.charAt( 0 ) != SLASH ) {
            return canonicalTarget;
          }
        }
      }
    }

    // char separator = File.separatorChar;
    int minLength = canonicalBase.length();

    if ( canonicalTarget.length() < minLength ) {
      minLength = canonicalTarget.length();
    }

    int firstDifference = minLength + 1;

    //
    // walk to the shorter of the two paths
    // finding the last separator they have in common
    int lastSeparator = -1;
    for ( int i = 0; i < minLength; i++ ) {
      if ( canonicalTarget.charAt( i ) == canonicalBase.charAt( i ) ) {
        if ( canonicalTarget.charAt( i ) == SLASH
          || canonicalTarget.charAt( i ) == BACKSLASH ) {
          lastSeparator = i;
        }
      } else {
        firstDifference = lastSeparator + 1;
        break;
      }
    }

    StringBuilder relativePath = new StringBuilder( 50 );

    //
    // walk from the first difference to the end of the base
    // adding "../" for each separator encountered
    //
    if ( canonicalBase.length() > firstDifference ) {
      relativePath.append( ".." );
      for ( int i = firstDifference; i < canonicalBase.length(); i++ ) {
        if ( canonicalBase.charAt( i ) == SLASH
          || canonicalBase.charAt( i ) == BACKSLASH ) {
          relativePath.append( separator );
          relativePath.append( ".." );
        }
      }
    }

    if ( canonicalTarget.length() > firstDifference ) {
      //
      // append the rest of the target
      //

      if ( relativePath.length() > 0 ) {
        relativePath.append( separator );
      }
      relativePath.append( canonicalTarget.substring( firstDifference ) );
    }

    return relativePath.toString();
  }

  /**
   * <p>getRelativePath</p>
   *
   * @param target        a {@link File} object.
   * @param base          a {@link File} object.
   * @param pathSeparator a {@link String} object.
   * @return a {@link File} object.
   */
  @Nonnull
  public static File getRelativePath( @Nonnull File target, @Nonnull File base, @Nonnull String pathSeparator ) {
    return new File( getRelativePath( target.getPath(), base.getPath(), pathSeparator ) );
  }

  /**
   * <p>getRelativePath</p>
   *
   * @param target a {@link File} object.
   * @param base   a {@link File} object.
   * @return a {@link File} object.
   */
  public static File getRelativePath( @Nonnull File target, @Nonnull File base ) {
    return getRelativePath( target, base, File.separator );
  }

  /**
   * <p>getRelativePath</p>
   *
   * @param targetPath a {@link String} object.
   * @param basePath   a {@link String} object.
   * @return a {@link String} object.
   */
  @Nonnull
  public static String getRelativePath( @Nonnull String targetPath, @Nonnull String basePath ) {
    return getRelativePath( targetPath, basePath, File.separator );
  }
}
