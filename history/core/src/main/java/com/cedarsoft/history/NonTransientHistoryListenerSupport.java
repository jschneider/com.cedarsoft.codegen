/**
 * Copyright (C) 2010 cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce.txt
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

package com.cedarsoft.history;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 */
public class NonTransientHistoryListenerSupport<E> {
  @NotNull
  private List<HistoryListener<E>> listeners = new ArrayList<HistoryListener<E>>();

  @NotNull
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  public void addHistoryListener( @NotNull HistoryListener<E> historyListener ) {
    lock.writeLock().lock();
    try {
      listeners.add( historyListener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void removeHistoryListener( @NotNull HistoryListener<E> historyListener ) {
    lock.writeLock().lock();
    try {
      listeners.remove( historyListener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  public void notifyEntryChanged( @NotNull E entry ) {
    lock.readLock().lock();
    List<HistoryListener<E>> copy;
    try {
      copy = new ArrayList<HistoryListener<E>>( listeners );
    } finally {
      lock.readLock().unlock();
    }
    for ( HistoryListener<E> listener : copy ) {
      listener.entryChanged( entry );
    }
  }

  public void notifyEntryAdded( @NotNull E entry ) {
    lock.readLock().lock();
    List<HistoryListener<E>> copy;
    try {
      copy = new ArrayList<HistoryListener<E>>( listeners );
    } finally {
      lock.readLock().unlock();
    }
    for ( HistoryListener<E> listener : copy ) {
      listener.entryAdded( entry );
    }
  }

  public void notifyEntryRemoved( @NotNull E entry ) {
    lock.readLock().lock();
    List<HistoryListener<E>> copy;
    try {
      copy = new ArrayList<HistoryListener<E>>( listeners );
    } finally {
      lock.readLock().unlock();
    }
    for ( HistoryListener<E> listener : copy ) {
      listener.entryRemoved( entry );
    }
  }
}
