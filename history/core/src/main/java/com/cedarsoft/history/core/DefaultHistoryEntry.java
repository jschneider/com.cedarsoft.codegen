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
import javax.annotation.Nullable;
import org.joda.time.LocalDate;

/**
 * Contains information that are valid at a given point in time.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class DefaultHistoryEntry implements HistoryEntry {
  private Long id;

  @Nonnull
  private final LocalDate verificationDate;

  /**
   * Creates a new default entry with the current datetime as verification date
   */
  public DefaultHistoryEntry() {
    this( new LocalDate() );
  }

  /**
   * Creates a new default entry
   *
   * @param verificationDate the verification date
   */
  public DefaultHistoryEntry( @Nonnull LocalDate verificationDate ) {
    this.verificationDate = verificationDate;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public LocalDate getVerificationDate() {
    return verificationDate;
  }

  //todo really necessary????

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo( @Nonnull HistoryEntry o ) {
    return verificationDate.compareTo( o.getVerificationDate() );
  }

  /**
   * Use with care!
   *
   * @return the id
   */
  @Nullable
  public Long getId() {
    return id;
  }
}
