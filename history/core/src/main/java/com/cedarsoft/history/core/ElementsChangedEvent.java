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

import java.util.ArrayList;
import java.util.List;

/**
 * <p>ElementsChangedEvent class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ElementsChangedEvent<T> {
  @Nonnull
  private final ObservableCollection<T> source;

  /**
   * Contains the changed elements
   */
  @Nonnull
  private final List<? extends T> elements;

  /**
   * Contains the indicies
   */
  @Nonnull
  private final List<? extends Integer> indicies;

  /**
   * <p>Constructor for ElementsChangedEvent.</p>
   *
   * @param source       a {@link ObservableCollection} object.
   * @param elements     a {@link List} object.
   * @param lowestIndex  a int.
   * @param highestIndex a int.
   */
  public ElementsChangedEvent( @Nonnull ObservableCollection<T> source, @Nonnull List<? extends T> elements, int lowestIndex, int highestIndex ) {
    this( source, elements, createIndicies( lowestIndex, highestIndex ) );
  }

  /**
   * <p>Constructor for ElementsChangedEvent.</p>
   *
   * @param source   a {@link ObservableCollection} object.
   * @param elements a {@link List} object.
   * @param indicies a {@link List} object.
   */
  public ElementsChangedEvent( @Nonnull ObservableCollection<T> source, @Nonnull List<? extends T> elements, @Nonnull List<? extends Integer> indicies ) {
    this.source = source;

    if ( elements.size() != indicies.size() ) {
      throw new IllegalArgumentException( "Invalid arguments. Indicies must be of same size as elements" );
    }

    //noinspection AssignmentToCollectionOrArrayFieldFromParameter
    this.elements = elements;
    //noinspection AssignmentToCollectionOrArrayFieldFromParameter
    this.indicies = indicies;
  }

  /**
   * <p>Getter for the field <code>source</code>.</p>
   *
   * @return a {@link ObservableCollection} object.
   */
  @Nonnull
  public ObservableCollection<T> getSource() {
    return source;
  }

  /**
   * <p>Getter for the field <code>elements</code>.</p>
   *
   * @return a {@link List} object.
   */
  @Nonnull
  public List<? extends T> getElements() {
    //noinspection ReturnOfCollectionOrArrayField
    return elements;
  }

  /**
   * <p>Getter for the field <code>indicies</code>.</p>
   *
   * @return a {@link List} object.
   */
  @Nonnull
  public List<? extends Integer> getIndicies() {
    //noinspection ReturnOfCollectionOrArrayField
    return indicies;
  }

  /**
   * Creates a new list with indicies from the lower to the upper (inclusive)
   *
   * @param lower the lower (inclusive)
   * @param upper the upper border (inclusive)
   * @return a list of indicies
   */
  @Nonnull
  public static List<Integer> createIndicies( int lower, int upper ) {
    List<Integer> indicies = new ArrayList<Integer>();

    for ( int i = lower; i <= upper; i++ ) {
      indicies.add( i );
    }

    return indicies;
  }
}
