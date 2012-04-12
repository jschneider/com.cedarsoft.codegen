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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A simple history that contains several entries.
 * Each entry has a verification date
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class DefaultHistory<E extends HistoryEntry> implements History<E> {
  private Long id;
  @Nonnull
  protected final List<E> entries = new ArrayList<E>();

  @Nonnull
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  @Nonnull
  private final ClusteredHistoryListenerSupport<E> listenerSupport = new ClusteredHistoryListenerSupport<E>();

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<? extends E> getEntries() {
    return Collections.unmodifiableList( entries );
  }

  /**
   * <p>getEntry</p>
   *
   * @param index a int.
   * @return a E object.
   *
   * @throws NoValidElementFoundException
   *          if any.
   */
  @Nonnull
  protected E getEntry( int index ) throws NoValidElementFoundException {
    lock.readLock().lock();
    try {
      if ( entries.size() <= index ) {
        throw new NoValidElementFoundException( "No entry with index " + index + " found. Only have " + entries.size() + " elements." );
      }
      return entries.get( index );
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasEntries() {
    lock.readLock().lock();
    try {
      return !entries.isEmpty();
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addEntry( @Nonnull E entry ) {
    lock.writeLock().lock();
    try {
      this.entries.add( entry );
      Collections.sort( this.entries );
    } finally {
      lock.writeLock().unlock();
    }
    listenerSupport.notifyEntryAdded( entry );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeEntry( @Nonnull E entry ) {
    lock.writeLock().lock();
    try {
      return entries.remove( entry );
    } finally {
      lock.writeLock().unlock();
      listenerSupport.notifyEntryRemoved( entry );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void commitEntry( @Nonnull E entry ) {
    listenerSupport.notifyEntryChanged( entry );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public E getFirstEntry() throws NoValidElementFoundException {
    lock.readLock().lock();
    try {
      if ( entries.isEmpty() ) {
        throw new NoValidElementFoundException();
      }
      return entries.get( 0 );
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void clear() {
    lock.writeLock().lock();
    try {
      List<? extends E> toRemove = new ArrayList<E>( getEntries() );
      for ( E entry : toRemove ) {
        removeEntry( entry );
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public E getLatestEntry() throws NoValidElementFoundException {
    lock.readLock().lock();
    try {
      if ( entries.isEmpty() ) {
        throw new NoValidElementFoundException();
      }
      return entries.get( entries.size() - 1 );
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLatestEntry( @Nonnull E entry ) {
    lock.readLock().lock();
    try {
      if ( entries.isEmpty() ) {
        return false;
      }
      //noinspection ObjectEquality
      return getLatestEntry() == entry;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<? extends E> getElements() {
    return getEntries();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setElements( @Nonnull List<? extends E> elements ) {
    List<E> newElements = new ArrayList<E>( elements );

    lock.writeLock().lock();
    try {
      for ( E element : new ArrayList<E>( this.entries ) ) {
        if ( !newElements.remove( element ) ) {
          remove( element );
        }
      }

      for ( E newElement : newElements ) {
        add( newElement );
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void add( @Nonnull E element ) {
    addEntry( element );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove( @Nonnull E element ) {
    removeEntry( element );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeHistoryListener( @Nonnull HistoryListener<E> historyListener ) {
    listenerSupport.removeHistoryListener( historyListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addHistoryListener( @Nonnull HistoryListener<E> historyListener ) {
    listenerSupport.addHistoryListener( historyListener, true );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<? extends HistoryListener<E>> getHistoryListeners() {
    return listenerSupport.getTransientHistoryListeners();
  }

  /**
   * <p>addHistoryListener</p>
   *
   * @param historyListener a {@link HistoryListener} object.
   * @param isTransient     a boolean.
   */
  public void addHistoryListener( @Nonnull HistoryListener<E> historyListener, boolean isTransient ) {
    listenerSupport.addHistoryListener( historyListener, isTransient );
  }

  /**
   * <p>Getter for the field <code>lock</code>.</p>
   *
   * @return a {@link ReadWriteLock} object.
   */
  @Nonnull
  public ReadWriteLock getLock() {
    return lock;
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
