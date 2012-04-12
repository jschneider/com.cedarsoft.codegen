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

package com.cedarsoft.history.core;

import javax.annotation.Nonnull;
import org.joda.time.LocalDate;

/**
 * Default entry for a history with validity dates.
 * Each entry has exactly one validity date where the entry is valid at.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class DefaultDiscreteHistoryEntry extends DefaultHistoryEntry implements DiscreteHistoryEntry {
  @Nonnull
  private final LocalDate validityDate;

  /**
   * Creates a new entry with the current date as validity and verification date
   */
  public DefaultDiscreteHistoryEntry() {
    this( new LocalDate() );
  }

  /**
   * Creates a new entry
   *
   * @param validityDate the validity date
   */
  public DefaultDiscreteHistoryEntry( @Nonnull LocalDate validityDate ) {
    this( validityDate, new LocalDate() );
  }

  /**
   * Creates a new entry
   *
   * @param validityDate     the validity date
   * @param verificationDate the verification date
   */
  public DefaultDiscreteHistoryEntry( @Nonnull LocalDate validityDate, @Nonnull LocalDate verificationDate ) {
    super( verificationDate );
    this.validityDate = validityDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public LocalDate getValidityDate() {
    return validityDate;
  }
}
