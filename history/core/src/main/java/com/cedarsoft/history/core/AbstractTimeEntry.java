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
 * <p>Abstract AbstractTimeEntry class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public abstract class AbstractTimeEntry implements TimeEntry {
  private Long id;
  @Nonnull
  protected LocalDate begin;
  @Nullable
  protected LocalDate end;

  /**
   * Hibernate
   */
  @Deprecated
  protected AbstractTimeEntry() {
  }

  /**
   * <p>Constructor for AbstractTimeEntry.</p>
   *
   * @param begin a {@link LocalDate} object.
   */
  protected AbstractTimeEntry( @Nonnull LocalDate begin ) {
    this.begin = begin;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public LocalDate getBegin() {
    return begin;
  }

  /**
   * <p>Setter for the field <code>begin</code>.</p>
   *
   * @param begin a {@link LocalDate} object.
   */
  public void setBegin( @Nonnull LocalDate begin ) {
    this.begin = begin;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setEnd( @Nullable LocalDate end ) {
    this.end = end;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nullable
  public LocalDate getEnd() {
    return end;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasEnd() {
    return end != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isActiveAt( @Nonnull LocalDate date ) {
    if ( !getBegin().isBefore( date ) ) {
      return false;
    }

    LocalDate theEnd = end;
    if ( theEnd == null ) {
      return true;
    } else {
      return theEnd.isAfter( date );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo( TimeEntry o ) {
    return getBegin().compareTo( o.getBegin() );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof AbstractTimeEntry ) ) return false;

    AbstractTimeEntry that = ( AbstractTimeEntry ) o;

    if ( !begin.equals( that.begin ) ) return false;
    if ( end != null ? !end.equals( that.end ) : that.end != null ) return false;

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result;
    result = begin.hashCode();
    result = 31 * result + ( end != null ? end.hashCode() : 0 );
    return result;
  }
}
