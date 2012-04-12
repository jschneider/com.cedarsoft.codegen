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
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>HistoryListenerSupport class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class HistoryListenerSupport<E> {
  @Nonnull
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  /**
   * <p>addHistoryListener</p>
   *
   * @param historyListener a {@link HistoryListener} object.
   */
  public void addHistoryListener( @Nonnull HistoryListener<E> historyListener ) {
    lock.writeLock().lock();
    try {
      getListeners().add( historyListener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>removeHistoryListener</p>
   *
   * @param historyListener a {@link HistoryListener} object.
   */
  public void removeHistoryListener( @Nonnull HistoryListener<E> historyListener ) {
    lock.writeLock().lock();
    try {
      getListeners().remove( historyListener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>notifyEntryChanged</p>
   *
   * @param entry a E object.
   */
  public void notifyEntryChanged( @Nonnull E entry ) {
    lock.writeLock().lock();
    List<HistoryListener<E>> copy;
    try {
      copy = new ArrayList<HistoryListener<E>>( getListeners() );
    } finally {
      lock.writeLock().unlock();
    }
    for ( HistoryListener<E> listener : copy ) {
      listener.entryChanged( entry );
    }
  }

  /**
   * <p>notifyEntryAdded</p>
   *
   * @param entry a E object.
   */
  public void notifyEntryAdded( @Nonnull E entry ) {
    lock.writeLock().lock();
    List<HistoryListener<E>> copy;
    try {
      copy = new ArrayList<HistoryListener<E>>( getListeners() );
    } finally {
      lock.writeLock().unlock();
    }
    for ( HistoryListener<E> listener : copy ) {
      listener.entryAdded( entry );
    }
  }

  /**
   * <p>notifyEntryRemoved</p>
   *
   * @param entry a E object.
   */
  public void notifyEntryRemoved( @Nonnull E entry ) {
    lock.writeLock().lock();
    List<HistoryListener<E>> copy;
    try {
      copy = new ArrayList<HistoryListener<E>>( getListeners() );
    } finally {
      lock.writeLock().unlock();
    }
    for ( HistoryListener<E> listener : copy ) {
      listener.entryRemoved( entry );
    }
  }

  private transient List<HistoryListener<E>> listeners;

  @Nonnull
  private List<HistoryListener<E>> getListeners() {
    lock.writeLock().lock();
    try {
      if ( listeners == null ) {
        listeners = new ArrayList<HistoryListener<E>>();
      }
      return listeners;
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>getHistoryListeners</p>
   *
   * @return a {@link List} object.
   */
  @Nonnull
  public List<? extends HistoryListener<E>> getHistoryListeners() {
    return Collections.unmodifiableList( getListeners() );
  }
}
