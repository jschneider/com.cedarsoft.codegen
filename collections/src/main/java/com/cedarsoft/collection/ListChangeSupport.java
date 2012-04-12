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

package com.cedarsoft.collection;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Supplies support for list change support.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ListChangeSupport<T> {
  private List<ListChangeListener<T>> listeners = new ArrayList<ListChangeListener<T>>();

  /**
   * <p>add</p>
   *
   * @param backend a {@link List} object.
   * @param element a T object.
   * @return a boolean.
   */
  public boolean add( @Nonnull List<T> backend, @Nonnull T element ) {
    int index = backend.size();
    boolean returnValue = backend.add( element );
    fireAddEvent( index, element );
    return returnValue;
  }

  /**
   * <p>fireAddEvent</p>
   *
   * @param index   a int.
   * @param element a T object.
   */
  public void fireAddEvent( int index, @Nonnull T element ) {
    if ( listeners.isEmpty() ) return;
    for ( ListChangeListener<T> listener : new ArrayList<ListChangeListener<T>>( listeners ) ) {
      listener.elementAdded( index, element );
    }
  }

  /**
   * <p>fireRemoveEvent</p>
   *
   * @param index   a int.
   * @param element a T object.
   */
  public void fireRemoveEvent( int index, @Nonnull T element ) {
    if ( listeners.isEmpty() ) return;
    for ( ListChangeListener<T> listener : new ArrayList<ListChangeListener<T>>( listeners ) ) {
      listener.elementRemoved( index, element );
    }
  }

  /**
   * <p>remove</p>
   *
   * @param backend a {@link List} object.
   * @param element a T object.
   * @return a boolean.
   */
  public boolean remove( @Nonnull List<T> backend, @Nonnull T element ) {
    int index = backend.indexOf( element );
    boolean returnValue = backend.remove( element );
    fireRemoveEvent( index, element );
    return returnValue;
  }

  /**
   * <p>addListener</p>
   *
   * @param listener a {@link ListChangeListener} object.
   */
  public void addListener( @Nonnull ListChangeListener<T> listener ) {
    listeners.add( listener );
  }

  /**
   * <p>removeListener</p>
   *
   * @param listener a {@link ListChangeListener} object.
   */
  public void removeListener( @Nonnull ListChangeListener<T> listener ) {
    listeners.remove( listener );
  }

  /**
   * <p>Getter for the field <code>listeners</code>.</p>
   *
   * @return a {@link List} object.
   */
  @Nonnull
  public List<ListChangeListener<T>> getListeners() {
    return Collections.unmodifiableList( listeners );
  }
}
