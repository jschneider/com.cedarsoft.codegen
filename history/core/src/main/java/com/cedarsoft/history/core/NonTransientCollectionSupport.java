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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>NonTransientCollectionSupport class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class NonTransientCollectionSupport<E> {
  @Nonnull
  private final ObservableCollection<E> source;

  @Nonnull
  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  private final List<ElementsListener<? super E>> listeners = new ArrayList<ElementsListener<? super E>>();

  /**
   * <p>Constructor for NonTransientCollectionSupport.</p>
   *
   * @param source a {@link ObservableCollection} object.
   */
  public NonTransientCollectionSupport( @Nonnull ObservableCollection<E> source ) {
    this.source = source;
  }

  /**
   * <p>addEntryListener</p>
   *
   * @param listener a {@link ElementsListener} object.
   */
  public void addEntryListener( @Nonnull ElementsListener<? super E> listener ) {
    lock.writeLock().lock();
    try {
      listeners.add( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>removeEntryListener</p>
   *
   * @param listener a {@link ElementsListener} object.
   */
  public void removeEntryListener( @Nonnull ElementsListener<? super E> listener ) {
    lock.writeLock().lock();
    try {
      listeners.remove( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>elementDeleted</p>
   *
   * @param element a E object.
   * @param index   a int.
   */
  public void elementDeleted( @Nonnull E element, int index ) {
    lock.readLock().lock();
    try {
      ElementsChangedEvent<E> event = new ElementsChangedEvent<E>( source, Collections.singletonList( element ), Collections.singletonList( index ) );
      for ( ElementsListener<? super E> listener : listeners ) {
        listener.elementsDeleted( event );
      }
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * <p>elementChanged</p>
   *
   * @param element a E object.
   * @param index   a int.
   */
  public void elementChanged( @Nonnull E element, int index ) {
    lock.readLock().lock();
    try {
      ElementsChangedEvent<E> event = new ElementsChangedEvent<E>( source, Collections.singletonList( element ), Collections.singletonList( index ) );
      for ( ElementsListener<? super E> listener : listeners ) {
        listener.elementsChanged( event );
      }
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * <p>elementAdded</p>
   *
   * @param element a E object.
   * @param index   a int.
   */
  public void elementAdded( @Nonnull E element, int index ) {
    lock.readLock().lock();
    try {
      ElementsChangedEvent<E> event = new ElementsChangedEvent<E>( source, Collections.singletonList( element ), Collections.singletonList( index ) );
      for ( ElementsListener<? super E> listener : listeners ) {
        listener.elementsAdded( event );
      }
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * <p>hasListeners</p>
   *
   * @return a boolean.
   */
  public boolean hasListeners() {
    lock.readLock().lock();
    try {
      return !listeners.isEmpty();
    } finally {
      lock.readLock().unlock();
    }
  }
}
