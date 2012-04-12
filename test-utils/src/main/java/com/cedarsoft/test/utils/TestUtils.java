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

package com.cedarsoft.test.utils;

import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/**
 * <p>TestUtils class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class TestUtils {
  private TestUtils() {
  }

  /**
   * Cleans up all fields within a given test
   *
   * @param test the test that is cleaned up
   * @throws IllegalAccessException if any.
   */
  public static void cleanupFields( @Nullable Object test ) throws IllegalAccessException {
    if ( test == null ) {
      return;
    }

    for ( Field field : test.getClass().getDeclaredFields() ) {
      if ( Modifier.isFinal( field.getModifiers() ) ) {
        continue;
      }
      field.setAccessible( true );
      field.set( test, null );
    }
  }

  /**
   * <p>getTmpDir</p>
   *
   * @return a {@link File} object.
   */
  @Nonnull
  public static File getTmpDir() {
    return new File( System.getProperty( "java.io.tmpdir" ) );
  }

  /**
   * @param prefix a {@link String} object.
   * @param suffix a {@link String} object.
   * @param in     a {@link InputStream} object.
   * @return a {@link File} object.
   *
   * @throws IOException if any.
   * @deprecated Use org.junit.rules.TemporaryFolder instead
   */
  @Deprecated
  @Nonnull
  public static File createTmpFile( @Nonnull String prefix, @Nonnull String suffix, @Nonnull InputStream in ) throws IOException {
    File file = File.createTempFile( prefix, suffix );
    file.deleteOnExit();

    FileOutputStream out = new FileOutputStream( file );
    try {
      IOUtils.copy( in, out );
    } finally {
      out.close();
    }
    return file;
  }

  /**
   * @return a {@link File} object.
   *
   * @deprecated Use org.junit.rules.TemporaryFolder instead
   *             <p>createEmptyTmpDir</p>
   */
  @Deprecated
  @Nonnull
  public static File createEmptyTmpDir() {
    File tmp = getTmpDir();
    File dir = null;

    while ( dir == null || dir.exists() ) {
      dir = new File( tmp, String.valueOf( ( int ) ( Math.random() * 1000000 ) ) );
    }

    dir.mkdir();
    return dir;
  }
}
