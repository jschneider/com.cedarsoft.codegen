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
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents an algorithm
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public enum Algorithm {
  MD5( "MD5" ),
  SHA1( "SHA-1", "SHA1" ),
  SHA256( "SHA-256", "SHA256" ),
  SHA512( "SHA-512", "SHA512" ),;

  /**
   * <p>getAlgorithm</p>
   *
   * @param algorithmString a {@link String} object.
   * @return a {@link Algorithm} object.
   */
  @Nonnull
  public static Algorithm getAlgorithm( @Nonnull String algorithmString ) {
    //First search for the exact match
    try {
      return Algorithm.valueOf( algorithmString );
    } catch ( IllegalArgumentException ignore ) {
    }

    //Now search for the alternative names
    for ( Algorithm algorithm : Algorithm.values() ) {
      if ( algorithm.alternativeNames.contains( algorithmString ) ) {
        return algorithm;
      }
    }
    throw new IllegalArgumentException( "No Alogirthm found for " + algorithmString );
  }

  @Nonnull
  private final List<String> alternativeNames = new ArrayList<String>();

  /**
   * Creates a new algorithm.
   * The first alternative name will be used to get the MessageDigest
   *
   * @param alternativeNames the alternative names
   */
  Algorithm( @Nonnull String... alternativeNames ) {
    if ( alternativeNames.length == 0 ) {
      throw new IllegalArgumentException( "Need at least one algorithm name" );
    }
    this.alternativeNames.addAll( Arrays.asList( alternativeNames ) );
  }

  /**
   * Returns a list containing all alternative names for the given algorithm
   *
   * @return the alternative names for the algorithm
   */
  @Nonnull
  public List<? extends String> getAlternativeNames() {
    return Collections.unmodifiableList( alternativeNames );
  }

  /**
   * Returns the Message Digest  algorithm
   *
   * @return the message digest
   */
  @Nonnull
  public MessageDigest getMessageDigest() {
    try {
      return MessageDigest.getInstance( alternativeNames.get( 0 ) );
    } catch ( NoSuchAlgorithmException e ) {
      throw new RuntimeException( e );
    }
  }
}
