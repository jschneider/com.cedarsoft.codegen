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

package com.cedarsoft.crypt;


import javax.annotation.Nonnull;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;

/**
 * <p>HashCalculator class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class HashCalculator {
  private HashCalculator() {
  }

  /**
   * <p>calculate</p>
   *
   * @param algorithm a {@link Algorithm} object.
   * @param value     an array of byte.
   * @return a {@link Hash} object.
   */
  @Nonnull
  public static Hash calculate( @Nonnull Algorithm algorithm, @Nonnull byte[] value ) {
    return calculate( algorithm.getMessageDigest(), value );
  }

  /**
   * <p>calculate</p>
   *
   * @param messageDigest a {@link MessageDigest} object.
   * @param value         an array of byte.
   * @return a {@link Hash} object.
   */
  @Nonnull
  public static Hash calculate( @Nonnull MessageDigest messageDigest, @Nonnull byte[] value ) {
    messageDigest.reset();
    messageDigest.update( value );

    byte[] digest = messageDigest.digest();
    return new Hash( Algorithm.getAlgorithm( messageDigest.getAlgorithm() ), digest );
  }


  /**
   * <p>calculate</p>
   *
   * @param algorithm a {@link Algorithm} object.
   * @param value     a {@link String} object.
   * @return a {@link Hash} object.
   */
  @Nonnull
  public static Hash calculate( @Nonnull Algorithm algorithm, @Nonnull String value ) {
    return calculate( algorithm.getMessageDigest(), value );
  }

  /**
   * <p>calculate</p>
   *
   * @param messageDigest a {@link MessageDigest} object.
   * @param value         a {@link String} object.
   * @return a {@link Hash} object.
   */
  @Nonnull
  public static Hash calculate( @Nonnull MessageDigest messageDigest, @Nonnull String value ) {
    return calculate( messageDigest, value.getBytes() );
  }

  /**
   * <p>calculate</p>
   *
   * @param algorithm a {@link Algorithm} object.
   * @param resource  a {@link URL} object.
   * @return a {@link Hash} object.
   *
   * @throws IOException if any.
   */
  @Nonnull
  public static Hash calculate( @Nonnull Algorithm algorithm, @Nonnull URL resource ) throws IOException {
    return calculate( algorithm.getMessageDigest(), resource );
  }

  /**
   * <p>calculate</p>
   *
   * @param messageDigest a {@link MessageDigest} object.
   * @param resource      a {@link URL} object.
   * @return a {@link Hash} object.
   *
   * @throws IOException if any.
   */
  @Nonnull
  public static Hash calculate( @Nonnull MessageDigest messageDigest, @Nonnull URL resource ) throws IOException {
    InputStream in = resource.openStream();
    try {
      return calculate( messageDigest, in );
    } finally {
      in.close();
    }
  }

  /**
   * <p>calculate</p>
   *
   * @param algorithm a {@link Algorithm} object.
   * @param file      a {@link File} object.
   * @return a {@link Hash} object.
   *
   * @throws IOException if any.
   */
  @Nonnull
  public static Hash calculate( @Nonnull Algorithm algorithm, @Nonnull File file ) throws IOException {
    return calculate( algorithm.getMessageDigest(), file );
  }

  /**
   * <p>calculate</p>
   *
   * @param messageDigest a {@link MessageDigest} object.
   * @param file          a {@link File} object.
   * @return a {@link Hash} object.
   *
   * @throws IOException if any.
   */
  @Nonnull
  public static Hash calculate( @Nonnull MessageDigest messageDigest, @Nonnull File file ) throws IOException {
    InputStream in = new BufferedInputStream( new FileInputStream( file ) );
    try {
      return calculate( messageDigest, in );
    } finally {
      in.close();
    }
  }

  /**
   * <p>calculate</p>
   *
   * @param algorithm  a {@link Algorithm} object.
   * @param resourceIn a {@link InputStream} object.
   * @return a {@link Hash} object.
   *
   * @throws IOException if any.
   */
  @Nonnull
  public static Hash calculate( @Nonnull Algorithm algorithm, @Nonnull InputStream resourceIn ) throws IOException {
    return calculate( algorithm.getMessageDigest(), resourceIn );
  }

  /**
   * <p>calculate</p>
   *
   * @param messageDigest a {@link MessageDigest} object.
   * @param resourceIn    a {@link InputStream} object.
   * @return a {@link Hash} object.
   *
   * @throws IOException if any.
   */
  @Nonnull
  public static Hash calculate( @Nonnull MessageDigest messageDigest, @Nonnull InputStream resourceIn ) throws IOException {
    messageDigest.reset();

    byte[] cache = new byte[255];
    int k;
    while ( ( k = resourceIn.read( cache ) ) > -1 ) {
      messageDigest.update( cache, 0, k );
    }

    byte[] digest = messageDigest.digest();
    return new Hash( Algorithm.getAlgorithm( messageDigest.getAlgorithm() ), digest );
  }
}
