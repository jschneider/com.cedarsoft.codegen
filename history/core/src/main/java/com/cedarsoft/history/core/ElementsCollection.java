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

import com.cedarsoft.concurrent.Lockable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.lang.Iterable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Contains a collection of entries
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 * @param <E> the type
 */
public class ElementsCollection<E> implements ObservableObjectAccess<E>, Lockable {
  /**
   * Constant <code>PROPERTY_ELEMENTS="elements"</code>
   */
  @Nonnull
  public static final String PROPERTY_ELEMENTS = "elements";

  private Long id;
  @Nonnull
  protected final List<E> elements = new ArrayList<E>();
  @Nonnull
  protected final CollectionSupport<E> collectionSupport = new CollectionSupport<E>( this );
  @Nonnull
  protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  /**
   * <p>Constructor for ElementsCollection.</p>
   */
  public ElementsCollection() {
  }

  /**
   * <p>Constructor for ElementsCollection.</p>
   *
   * @param elements a {@link Collection} object.
   */
  public ElementsCollection( @Nonnull Collection<? extends E> elements ) {
    this.elements.addAll( elements );
  }

  /**
   * <p>Constructor for ElementsCollection.</p>
   *
   * @param elements a E object.
   */
  public ElementsCollection( @Nonnull E... elements ) {
    this( Arrays.asList( elements ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public final void add( @Nonnull E element ) {
    addElement( element );
  }

  /**
   * Adds a new entry
   *
   * @param element the entry that is added
   */
  public void addElement( @Nonnull E element ) {
    lock.writeLock().lock();
    int index;
    try {
      elements.add( element );
      index = elements.indexOf( element );
    } finally {
      lock.writeLock().unlock();
    }
    collectionSupport.elementAdded( element, index );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void commit( @Nonnull E element ) {
    lock.readLock().lock();
    int index;
    try {
      if ( !elements.contains( element ) ) {
        throw new IllegalArgumentException( "Invalid element commited: " + element + ". Is not contained within this collection." );
      }
      index = elements.indexOf( element );
    } finally {
      lock.readLock().unlock();
    }
    collectionSupport.elementChanged( element, index );
  }

  /**
   * Whether this contains any entries
   *
   * @return whether this  contains any entries
   */
  public boolean hasElements() {
    lock.readLock().lock();
    try {
      return !elements.isEmpty();
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * <p>isEmpty</p>
   *
   * @return a boolean.
   */
  public boolean isEmpty() {
    return !hasElements();
  }

  /**
   * <p>addAll</p>
   *
   * @param additionalElements a {@link Iterable} object.
   */
  public void addAll( @Nonnull Iterable<? extends E> additionalElements ) {
    for ( E element : additionalElements ) {
      add( element );
    }
  }

  /**
   * <p>size</p>
   *
   * @return a int.
   */
  public int size() {
    lock.readLock().lock();
    try {
      return elements.size();
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   * <p/>
   * Returns all cessions
   */
  @Override
  @Nonnull
  public List<? extends E> getElements() {
    lock.readLock().lock();
    try {
      return Collections.unmodifiableList( elements );
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   * <p/>
   * Sets the elements
   */
  @Override
  public void setElements( @Nonnull List<? extends E> elements ) {
    Collection<E> newElements = new ArrayList<E>( elements );

    lock.writeLock().lock();
    try {
      for ( E element : new ArrayList<E>( this.elements ) ) {
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
  public final void remove( @Nonnull E element ) {
    removeEntry( element );
  }

  /**
   * <p>removeEntry</p>
   *
   * @param element a E object.
   * @return a boolean.
   */
  public boolean removeEntry( @Nonnull E element ) {
    lock.writeLock().lock();
    boolean removed;
    int index;
    try {
      index = elements.indexOf( element );
      removed = elements.remove( element );
    } finally {
      lock.writeLock().unlock();
    }

    collectionSupport.elementDeleted( element, index );

    return removed;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addElementListener( @Nonnull ElementsListener<? super E> listener ) {
    collectionSupport.addElementListener( listener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeElementListener( @Nonnull ElementsListener<? super E> listener ) {
    collectionSupport.removeElementListener( listener );
  }

  /**
   * <p>findElements</p>
   *
   * @param visitor a {@link ElementVisitor} object.
   * @return a {@link List} object.
   */
  @Nonnull
  public List<? extends E> findElements( @Nonnull ElementVisitor<? super E> visitor ) {
    List<E> found = new ArrayList<E>();

    lock.readLock().lock();
    try {
      for ( E element : elements ) {
        if ( visitor.fits( element ) ) {
          found.add( element );
        }
      }
    } finally {
      lock.readLock().unlock();
    }

    return Collections.unmodifiableList( found );
  }

  /**
   * Returns the first entry that matches the visistor
   *
   * @param visitor the visitor that identifies the entries
   * @return the first entry
   *
   * @throws NoElementFoundException
   *          if no entry has been found
   */
  @Nonnull
  public E findFirstElement( @Nonnull ElementVisitor<? super E> visitor ) throws NoElementFoundException {
    E found = findFirstElementNullable( visitor );
    if ( found == null ) {
      throw new NoElementFoundException( "No element found for " + visitor.getIdentifier() );
    }
    return found;
  }

  /**
   * <p>findFirstElementNullable</p>
   *
   * @param visitor a {@link ElementVisitor} object.
   * @return a E object.
   */
  @Nullable
  public E findFirstElementNullable( @Nonnull ElementVisitor<? super E> visitor ) {
    lock.readLock().lock();
    try {
      for ( E element : elements ) {
        if ( visitor.fits( element ) ) {
          return element;
        }
      }
    } finally {
      lock.readLock().unlock();
    }
    return null;
  }

  /**
   * <p>contains</p>
   *
   * @param element a E object.
   * @return a boolean.
   */
  public boolean contains( @Nonnull E element ) {
    lock.readLock().lock();
    try {
      return elements.contains( element );
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * <p>contains</p>
   *
   * @param visitor a {@link ElementVisitor} object.
   * @return a boolean.
   *
   * @throws NoElementFoundException
   *          if any.
   */
  public boolean contains( @Nonnull ElementVisitor<? super E> visitor ) throws NoElementFoundException {
    lock.readLock().lock();
    try {
      for ( E element : elements ) {
        if ( visitor.fits( element ) ) {
          return true;
        }
      }
    } finally {
      lock.readLock().unlock();
    }
    return false;
  }

  /**
   * Removes the entries
   *
   * @param visitor the visitor that describes the entries
   * @return the removed elements
   */
  @Nonnull
  public List<? extends E> removeElements( @Nonnull ElementVisitor<? super E> visitor ) {
    List<E> removed = new ArrayList<E>();
    lock.writeLock().lock();
    try {
      for ( int i = 0; i < elements.size(); ) {
        E element = elements.get( i );
        if ( visitor.fits( element ) ) {
          elements.remove( i );
          removed.add( element );
          collectionSupport.elementDeleted( element, i );
        } else {
          i++;
        }
      }
    } finally {
      lock.writeLock().unlock();
    }

    return removed;
  }

  /**
   * <p>clear</p>
   */
  public void clear() {
    if ( !collectionSupport.hasListeners() ) {
      lock.writeLock().lock();
      try {
        elements.clear();
      } finally {
        lock.writeLock().unlock();
      }
    }

    lock.writeLock().lock();
    try {
      for ( Iterator<E> it = elements.iterator(); it.hasNext(); ) {
        E element = it.next();
        it.remove();
        collectionSupport.elementDeleted( element, 0 );
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
  public ReentrantReadWriteLock getLock() {
    return lock;
  }

  /**
   * XStream
   *
   * @return this
   */
  @Nonnull
  private Object readResolve() {
    try {
      {
        Field field = ElementsCollection.class.getDeclaredField( "collectionSupport" );
        field.setAccessible( true );
        field.set( this, new CollectionSupport<E>( this ) );
      }
      {
        Field field = ElementsCollection.class.getDeclaredField( "lock" );
        field.setAccessible( true );
        field.set( this, new ReentrantReadWriteLock() );
      }
    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
    return this;
  }
}
