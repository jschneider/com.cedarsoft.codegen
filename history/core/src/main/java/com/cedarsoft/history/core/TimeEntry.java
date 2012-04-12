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
 * An entry for the {@link TimeElementsCollection}
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public interface TimeEntry extends Comparable<TimeEntry> {
  /**
   * Constant <code>PROPERTY_BEGIN="begin"</code>
   */
  @Nonnull
  String PROPERTY_BEGIN = "begin";
  /**
   * Constant <code>PROPERTY_END="end"</code>
   */
  @Nonnull
  String PROPERTY_END = "end";
  /**
   * Constant <code>PROPERTY_HAS_END="hasEnd"</code>
   */
  @Nonnull
  String PROPERTY_HAS_END = "hasEnd";

  /**
   * Returns the begin of the entry
   *
   * @return the begin
   */
  @Nonnull
  LocalDate getBegin();

  /**
   * Returns the end of the entry
   *
   * @return the end (if there is one).
   */
  @Nullable
  LocalDate getEnd();

  /**
   * Sets the end date
   *
   * @param end the end
   */
  void setEnd( @Nullable LocalDate end );

  /**
   * Returns whether the entry has an end set
   *
   * @return whether the entry has an end set
   */
  boolean hasEnd();

  /**
   * Whether the entry is active at the given date
   *
   * @param date the date
   * @return whether the entry is active at the given date
   */
  boolean isActiveAt( @Nonnull LocalDate date );
}
