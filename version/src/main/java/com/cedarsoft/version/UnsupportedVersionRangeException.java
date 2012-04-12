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

package com.cedarsoft.version;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.lang.String;

/**
 * <p>UnsupportedVersionRangeException class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class UnsupportedVersionRangeException extends VersionException {
  @Nonnull
  private final VersionRange actual;

  @Nullable
  private final VersionRange supportedRange;

  /**
   * <p>Constructor for UnsupportedVersionRangeException.</p>
   *
   * @param actual a {@link VersionRange} object.
   */
  public UnsupportedVersionRangeException( @Nonnull VersionRange actual ) {
    this( actual, null );
  }

  /**
   * <p>Constructor for UnsupportedVersionRangeException.</p>
   *
   * @param actual         a {@link VersionRange} object.
   * @param supportedRange a {@link VersionRange} object.
   */
  public UnsupportedVersionRangeException( @Nonnull VersionRange actual, @Nullable VersionRange supportedRange ) {
    this( actual, supportedRange, "Unsupported version range. ", true );
  }

  /**
   * <p>Constructor for UnsupportedVersionRangeException.</p>
   *
   * @param actual         a {@link VersionRange} object.
   * @param supportedRange a {@link VersionRange} object.
   * @param messagePrefix  a {@link String} object.
   */
  public UnsupportedVersionRangeException( @Nonnull VersionRange actual, @Nullable VersionRange supportedRange, @Nonnull String messagePrefix ) {
    this( actual, supportedRange, messagePrefix, true );
  }

  /**
   * <p>Constructor for UnsupportedVersionRangeException.</p>
   *
   * @param actual         a {@link VersionRange} object.
   * @param supportedRange a {@link VersionRange} object.
   * @param message        a {@link String} object.
   * @param appendSuffix   a boolean.
   */
  public UnsupportedVersionRangeException( @Nonnull VersionRange actual, @Nullable VersionRange supportedRange, @Nonnull String message, boolean appendSuffix ) {
    super( message, createMessageSuffix( actual, supportedRange ), appendSuffix );
    this.actual = actual;
    this.supportedRange = supportedRange;
  }

  /**
   * <p>Getter for the field <code>actual</code>.</p>
   *
   * @return a {@link VersionRange} object.
   */
  @Nonnull
  public VersionRange getActual() {
    return actual;
  }

  /**
   * <p>Getter for the field <code>supportedRange</code>.</p>
   *
   * @return a {@link VersionRange} object.
   */
  @Nullable
  public VersionRange getSupportedRange() {
    return supportedRange;
  }

  private static String createMessageSuffix( @Nonnull VersionRange actual, @Nullable VersionRange supportedRange ) {
    if ( supportedRange == null ) {
      return "Was <" + actual + ">";
    }

    return "Was <" + actual + "> but expected <" + supportedRange.toString() + ">";
  }
}
