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

import java.util.List;

/**
 * <p>ClusteredCollectionSupport class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ClusteredCollectionSupport<E> {
  @Nonnull
  private final CollectionSupport<E> transientSupport;
  @Nonnull
  private final NonTransientCollectionSupport<E> nonTransientSupport;

  /**
   * <p>Constructor for ClusteredCollectionSupport.</p>
   *
   * @param source a {@link ObservableCollection} object.
   */
  public ClusteredCollectionSupport( @Nonnull ObservableCollection<E> source ) {
    transientSupport = new CollectionSupport<E>( source );
    nonTransientSupport = new NonTransientCollectionSupport<E>( source );
  }

  /**
   * <p>addElementListener</p>
   *
   * @param listener    a {@link ElementsListener} object.
   * @param isTransient a boolean.
   */
  public void addElementListener( @Nonnull ElementsListener<? super E> listener, boolean isTransient ) {
    if ( isTransient ) {
      transientSupport.addElementListener( listener );
    } else {
      nonTransientSupport.addEntryListener( listener );
    }
  }

  /**
   * <p>removeElementListener</p>
   *
   * @param listener a {@link ElementsListener} object.
   */
  public void removeElementListener( @Nonnull ElementsListener<? super E> listener ) {
    transientSupport.removeElementListener( listener );
    nonTransientSupport.removeEntryListener( listener );
  }

  /**
   * <p>elementDeleted</p>
   *
   * @param entry a E object.
   * @param index a int.
   */
  public void elementDeleted( @Nonnull E entry, int index ) {
    transientSupport.elementDeleted( entry, index );
    nonTransientSupport.elementDeleted( entry, index );
  }

  /**
   * <p>elementChanged</p>
   *
   * @param entry a E object.
   * @param index a int.
   */
  public void elementChanged( @Nonnull E entry, int index ) {
    transientSupport.elementChanged( entry, index );
    nonTransientSupport.elementChanged( entry, index );
  }

  /**
   * <p>elementAdded</p>
   *
   * @param entry a E object.
   * @param index a int.
   */
  public void elementAdded( @Nonnull E entry, int index ) {
    transientSupport.elementAdded( entry, index );
    nonTransientSupport.elementAdded( entry, index );
  }

  /**
   * <p>hasListeners</p>
   *
   * @return a boolean.
   */
  public boolean hasListeners() {
    return !transientSupport.getListeners().isEmpty() || nonTransientSupport.hasListeners();
  }

  /**
   * <p>getTransientElementListeners</p>
   *
   * @return a {@link List} object.
   */
  @Nonnull
  public List<? extends ElementsListener<? super E>> getTransientElementListeners() {
    return transientSupport.getListeners();
  }
}
