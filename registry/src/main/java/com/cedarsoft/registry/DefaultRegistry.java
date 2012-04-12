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

package com.cedarsoft.registry;

import com.cedarsoft.commons.Converter;
import com.cedarsoft.exceptions.NotFoundException;
import com.cedarsoft.exceptions.StillContainedException;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>DefaultRegistry class.</p>
 *
 * @param <T> the type that is stored within this registry
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class DefaultRegistry<T> implements Registry<T> {
  @Nonnull
  protected final List<T> storedObjects = new ArrayList<T>();
  @Nonnull
  protected final ReadWriteLock lock = new ReentrantReadWriteLock();

  /**
   * This comparator may optionally be set to ensure the registry only contains unique values
   */
  @Nullable
  protected final Comparator<T> comparator;

  /**
   * Creates an empty registry
   */
  public DefaultRegistry() {
    comparator = null;
  }

  /**
   * Creates a registry with containing the given objects.
   * No comparator is set
   *
   * @param storedObjects the stored objects
   */
  public DefaultRegistry( @Nonnull Collection<? extends T> storedObjects ) {
    this( storedObjects, null );
  }

  /**
   * Creates an empty registry with the given (optional) comparator
   *
   * @param comparator the comparator
   */
  public DefaultRegistry( @Nullable Comparator<T> comparator ) {
    this.comparator = comparator;
  }

  /**
   * Creates a new registry
   *
   * @param storedObjects the initially stored objects
   * @param comparator    the (optional) comparator
   * @throws StillContainedException if any.
   */
  public DefaultRegistry( @Nonnull Collection<? extends T> storedObjects, @Nullable Comparator<T> comparator ) throws StillContainedException {
    this.comparator = comparator;

    //todo, really necessary???
    if ( comparator != null ) {
      Collection<T> set = new TreeSet<T>( comparator );
      set.addAll( storedObjects );
      if ( storedObjects.size() != set.size() ) {
        throw new StillContainedException( "The stored objects collections contains duplicate entries" );
      }
    }

    this.storedObjects.addAll( storedObjects );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<? extends T> getStoredObjects() {
    lock.readLock().lock();
    try {
      return Collections.unmodifiableList( storedObjects );
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nullable
  public T findStoredObject( @Nonnull Matcher<T> matcher ) {
    lock.readLock().lock();
    try {
      for ( T object : storedObjects ) {
        if ( matcher.matches( object ) ) {
          return object;
        }
      }

      return null;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * <p>findStoredObject</p>
   *
   * @param matcher         a Matcher object.
   * @param notFoundMessage a {@link String} object.
   * @return a T object.
   *
   * @throws NotFoundException if any.
   */
  @Override
  @Nonnull
  public T findStoredObject( @Nonnull Matcher<T> matcher, @Nonnull String notFoundMessage ) throws NotFoundException {
    T found = findStoredObject( matcher );
    if ( found == null ) {
      throw new NotFoundException( notFoundMessage );
    }

    return found;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<? extends T> findStoredObjects( @Nonnull Matcher<T> matcher ) {
    lock.readLock().lock();
    try {
      List<T> found = new ArrayList<T>();
      for ( T object : storedObjects ) {
        if ( matcher.matches( object ) ) {
          found.add( object );
        }
      }

      return found;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public <C> List<? extends C> findStoredObjects( @Nonnull Matcher<T> matcher, @Nonnull Converter<T, C> converter ) {
    lock.readLock().lock();
    try {
      List<C> found = new ArrayList<C>();
      for ( T object : storedObjects ) {
        if ( matcher.matches( object ) ) {
          found.add( converter.convert( object ) );
        }
      }

      return found;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   * <p/>
   * Stores the object within the registry
   */
  @Override
  public void store( @Nonnull T object ) throws StillContainedException {
    lock.writeLock().lock();
    try {
      if ( comparator != null ) {
        for ( T storedObject : storedObjects ) {
          if ( comparator.compare( storedObject, object ) == 0 ) {
            throw new StillContainedException( object );
          }
        }
      }

      storedObjects.add( object );

      for ( Listener<T> listener : new ArrayList<Listener<T>>( listeners ) ) {
        listener.objectStored( object );
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void updated( @Nonnull T object ) throws NotFoundException {
    lock.writeLock().lock();
    try {
      for ( Listener<T> listener : new ArrayList<Listener<T>>( listeners ) ) {
        listener.objectUpdated( object );
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  @Override
  public void remove( @Nonnull T object ) throws NotFoundException {
    remove( object, "Cannot remove: Not found <" + object + ">" );
  }

  @Override
  public void remove( @Nonnull T object, @Nonnull String removeMessage ) throws NotFoundException {
    try {
      lock.writeLock().lock();
      if ( !storedObjects.remove( object ) ) {
        throw new NotFoundException( removeMessage );
      }
      for ( Listener<T> listener : new ArrayList<Listener<T>>( listeners ) ) {
        listener.objectRemoved( object );
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nullable
  public Comparator<T> getComparator() {
    return comparator;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean containsOnlyUniqueElements() {
    return comparator != null;
  }

  @Nonnull
  protected final List<Listener<T>> listeners = new ArrayList<Listener<T>>();

  /**
   * {@inheritDoc}
   */
  @Override
  public void addListener( @Nonnull Listener<T> listener ) {
    lock.writeLock().lock();
    try {
      this.listeners.add( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeListener( @Nonnull Listener<T> listener ) {
    lock.writeLock().lock();
    try {
      this.listeners.remove( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }
}
