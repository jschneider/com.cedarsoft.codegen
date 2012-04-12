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


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.lang.String;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * <p>PasswordUtils class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class PasswordUtils {
  private PasswordUtils() {
  }

  /**
   * <p>calculateMD5Hash</p>
   *
   * @param password a {@link String} object.
   * @return an array of byte.
   */
  @Nonnull
  public static byte[] calculateMD5Hash( @Nonnull String password ) {
    byte[] bytes = password.getBytes();
    try {
      MessageDigest messageDigest = MessageDigest.getInstance( "MD5" );
      return messageDigest.digest( bytes );
    } catch ( NoSuchAlgorithmException e ) {
      throw new RuntimeException( e );
    }
  }

  /**
   * <p>hasExpectedHash</p>
   *
   * @param password     a {@link String} object.
   * @param expectedHash an array of byte.
   * @return a boolean.
   */
  public static boolean hasExpectedHash( @Nonnull String password, @Nullable byte[] expectedHash ) {
    if ( expectedHash == null ) {
      return false;
    }

    byte[] actual = calculateMD5Hash( password );
    try {
      validatePasswordHash( expectedHash, actual );
      return true;
    } catch ( InvalidPasswordException ignore ) {
      return false;
    }
  }

  /**
   * <p>validatePasswordHash</p>
   *
   * @param expected an array of byte.
   * @param actual   an array of byte.
   * @throws InvalidPasswordException
   *          if any.
   */
  public static void validatePasswordHash( @Nullable byte[] expected, @Nullable byte[] actual ) throws InvalidPasswordException {
    if ( expected == null || actual == null ) {
      throw new InvalidPasswordException();
    }
    if ( actual.length != expected.length ) {
      throw new InvalidPasswordException();
    }

    for ( int i = 0; i < actual.length; i++ ) {
      byte actualByte = actual[i];
      byte expectedByte = expected[i];

      if ( actualByte != expectedByte ) {
        throw new InvalidPasswordException();
      }
    }
  }
}
