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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Holds a list of entries. Each of the entries has an beginning and an (optional) end.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class TimeElementsCollection<E extends TimeEntry> extends SortedClusteredElementsCollection<E> {
  /**
   * Adds an entry and optionally closes all old entries
   *
   * @param entry           the entry that is added
   * @param closeOldEntries whether the old entries shall be closed
   */
  public void addEntry( @Nonnull E entry, boolean closeOldEntries ) {
    if ( closeOldEntries ) {
      for ( E currentEntry : elements ) {
        if ( currentEntry.getEnd() == null ) {
          currentEntry.setEnd( entry.getBegin() );
        }
      }
    }
    addElement( entry );
  }

  /**
   * Returns the latest entry
   *
   * @return the latest entry
   */
  @Nonnull
  public E getYoungestEntry() {
    if ( elements.isEmpty() ) {
      throw new IllegalStateException( "No elements found" );
    }
    return elements.get( elements.size() - 1 );
  }

  /**
   * <p>getOldestEntry</p>
   *
   * @return a E object.
   */
  @Nonnull
  public E getOldestEntry() {
    if ( elements.isEmpty() ) {
      throw new IllegalStateException( "No elements found" );
    }
    return elements.get( 0 );
  }

  /**
   * Returns the entry for the given date
   *
   * @param date the date
   * @return the entry
   */
  @Nonnull
  public List<? extends E> getEntries( @Nonnull LocalDate date ) {
    List<E> found = new ArrayList<E>();

    for ( E cession : elements ) {
      if ( cession.isActiveAt( date ) ) {
        found.add( cession );
      }
    }
    return found;
  }

  /**
   * Returns the begin of the latest cession
   *
   * @return the begin of the latest cession
   */
  @Nullable
  public Date getLatestEntryDate() {
    if ( !hasElements() ) {
      return null;
    }
    return getYoungestEntry().getBegin().toDateTimeAtStartOfDay().toDate();
  }

  /**
   * <p>getFirstEntryDate</p>
   *
   * @return a {@link Date} object.
   */
  @Nullable
  public Date getFirstEntryDate() {
    if ( !hasElements() ) {
      return null;
    }
    return getOldestEntry().getBegin().toDateTimeAtStartOfDay().toDate();
  }
}
