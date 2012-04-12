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

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

import javax.annotation.Nonnull;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Represents a hash value
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class Hash implements Serializable {
  private static final long serialVersionUID = 5728176239480983210L;

  @Nonnull
  private final Algorithm algorithm;
  @Nonnull
  private final byte[] value;

  /**
   * <p>Constructor for Hash.</p>
   *
   * @param algorithm a {@link Algorithm} object.
   * @param value     an array of byte.
   */
  public Hash( @Nonnull Algorithm algorithm, @Nonnull byte[] value ) {
    this.algorithm = algorithm;
    this.value = value.clone();
  }

  /**
   * <p>Getter for the field <code>algorithm</code>.</p>
   *
   * @return a {@link Algorithm} object.
   */
  @Nonnull
  public Algorithm getAlgorithm() {
    return algorithm;
  }

  /**
   * <p>getValueAsHex</p>
   *
   * @return a {@link String} object.
   */
  @Nonnull
  public String getValueAsHex() {
    return new String( Hex.encodeHex( value ) );
  }

  /**
   * <p>Getter for the field <code>value</code>.</p>
   *
   * @return an array of byte.
   */
  @Nonnull
  public byte[] getValue() {
    return value.clone();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public String toString() {
    return "[" + algorithm + ": " + getValueAsHex() + "]";
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;

    Hash hash = ( Hash ) o;

    if ( algorithm != hash.algorithm ) return false;
    if ( !Arrays.equals( value, hash.value ) ) return false;

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result;
    result = algorithm.hashCode();
    result = 31 * result + ( value != null ? Arrays.hashCode( value ) : 0 );
    return result;
  }

  /**
   * Creates a hash from the given hex value
   *
   * @param algorithm  the algorithm
   * @param valueAsHex the hex value
   * @return the hash
   */
  @Nonnull
  public static Hash fromHex( @Nonnull Algorithm algorithm, @Nonnull String valueAsHex ) {
    try {
      return new Hash( algorithm, Hex.decodeHex( valueAsHex.toCharArray() ) );
    } catch ( DecoderException e ) {
      throw new IllegalArgumentException( "Invalid hex string <" + valueAsHex + ">", e );
    }
  }
}
