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

package com.cedarsoft;

import javax.annotation.Nonnull;

/**
 * <p>VersionMismatchException class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class VersionMismatchException extends VersionException {
  @Nonnull
  private final VersionRange expected;
  @Nonnull
  private final Version actual;

  /**
   * <p>Constructor for VersionMismatchException.</p>
   *
   * @param expected a {@link Version} object.
   * @param actual   a {@link Version} object.
   */
  public VersionMismatchException( @Nonnull Version expected, @Nonnull Version actual ) {
    this( expected, actual, "Version mismatch. " );
  }

  public VersionMismatchException( @Nonnull VersionRange expected, @Nonnull Version actual ) {
    this( expected, actual, "Version mismatch. " );
  }

  /**
   * Creates a new version mismatch exception
   *
   * @param expected      the expected version
   * @param actual        the actual version
   * @param messagePrefix the message prefix. This constructor automatically appends the version info (expected/actual) to the message
   */
  public VersionMismatchException( @Nonnull Version expected, @Nonnull Version actual, @Nonnull String messagePrefix ) {
    this( expected, actual, messagePrefix, true );
  }

  public VersionMismatchException( @Nonnull VersionRange expected, @Nonnull Version actual, @Nonnull String messagePrefix ) {
    this( expected, actual, messagePrefix, true );
  }

  /**
   * Creates a new exception
   *
   * @param expected          the expected version
   * @param actual            the actual version
   * @param messagePrefix     the message prefix
   * @param appendVersionInfo whether to append the expected/actual version info to the message
   */
  public VersionMismatchException( @Nonnull Version expected, @Nonnull Version actual, @Nonnull String messagePrefix, boolean appendVersionInfo ) {
    this( VersionRange.single( expected ), actual, messagePrefix, appendVersionInfo );
  }

  public VersionMismatchException( @Nonnull VersionRange expected, @Nonnull Version actual, @Nonnull String messagePrefix, boolean appendVersionInfo ) {
    super( messagePrefix, "Expected " + expected.format() + " but was [" + actual + "]", appendVersionInfo );
    this.expected = expected;
    this.actual = actual;
  }

  /**
   * <p>Getter for the field <code>expected</code>.</p>
   *
   * @return a {@link Version} object.
   */
  @Nonnull
  public VersionRange getExpected() {
    return expected;
  }

  /**
   * <p>Getter for the field <code>actual</code>.</p>
   *
   * @return a {@link Version} object.
   */
  @Nonnull
  public Version getActual() {
    return actual;
  }
}
