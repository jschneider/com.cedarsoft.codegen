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

import com.cedarsoft.objectaccess.CommitableObjectAccess;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A history is an ordered collection of several {@link DefaultContinuousEntry}s.
 * Each entries covers a given interval.
 * <p/>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 * @param <E> the type of the entries
 */
public class ContinuousEntriesInformation<E extends ContinuousEntry> implements CommitableObjectAccess<E> {
  /**
   * Constant <code>PROPERTY_ENTRIES="entries"</code>
   */
  @Nonnull
  public static final String PROPERTY_ENTRIES = "entries";

  private Long id;

  @Nonnull
  private final List<E> entries = new ArrayList<E>();

  @Nonnull
  private LocalDate begin;
  @Nonnull
  private LocalDate end;

  @Nonnull
  private final ReadWriteLock lock = new ReentrantReadWriteLock();


  /**
   * Hibernate
   */
  @Deprecated
  protected ContinuousEntriesInformation() {
    begin = null;
    end = null;
  }

  /**
   * Creates a new continuous history
   *
   * @param begin the begin of the history
   * @param end   the end of the history
   */
  public ContinuousEntriesInformation( @Nonnull LocalDate begin, @Nonnull LocalDate end ) {
    this.begin = begin;
    this.end = end;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void commit( @Nonnull E element ) {
    commitEntry( element );
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
  public void remove( @Nonnull E element ) {
    removeEntry( element );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void add( @Nonnull E element ) {
    addEntry( element );
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
   * <p>Getter for the field <code>entries</code>.</p>
   *
   * @return a {@link List} object.
   */
  @Nonnull
  public List<? extends E> getEntries() {
    lock.readLock().lock();
    try {
      return Collections.unmodifiableList( entries );
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * <p>hasEntries</p>
   *
   * @return a boolean.
   */
  public boolean hasEntries() {
    lock.readLock().lock();
    try {
      return !entries.isEmpty();
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Removes the last entry
   */
  public void removeLastEntry() {
    lock.writeLock().lock();
    E removed;
    try {
      if ( entries.isEmpty() ) {
        throw new NoValidElementFoundException( "No elements left" );
      }
      removed = entries.remove( entries.size() - 1 );
    } finally {
      lock.writeLock().unlock();
    }
    notifyEntryRemoved( removed );
  }

  /**
   * <p>Setter for the field <code>begin</code>.</p>
   *
   * @param begin a {@link LocalDate} object.
   */
  public void setBegin( @Nonnull LocalDate begin ) {
    lock.writeLock().lock();
    try {
      this.begin = begin;

      //Now update the first enty
      try {
        E firstEntry = getFirstEntry();
        if ( firstEntry.getBegin().isBefore( begin ) ) {
          firstEntry.setBegin( begin );
        }
      } catch ( NoValidElementFoundException ignore ) {
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>Setter for the field <code>end</code>.</p>
   *
   * @param end a {@link LocalDate} object.
   */
  public void setEnd( @Nonnull LocalDate end ) {
    lock.writeLock().lock();
    try {
      this.end = end;
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>removeEntry</p>
   *
   * @param entry a E object.
   * @return a boolean.
   */
  public boolean removeEntry( @Nonnull E entry ) {
    lock.writeLock().lock();
    try {
      return entries.remove( entry );
    } finally {
      lock.writeLock().unlock();
      notifyEntryRemoved( entry );
    }
  }

  /**
   * <p>clear</p>
   */
  public void clear() {
    lock.writeLock().lock();
    try {
      List<? extends E> entries = new ArrayList<E>( getEntries() );
      for ( E entry : entries ) {
        removeEntry( entry );
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>getLatestEntry</p>
   *
   * @return a E object.
   *
   * @throws NoValidElementFoundException
   *          if any.
   */
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
   * Returns the first entry
   *
   * @return the first entry
   *
   * @throws NoValidElementFoundException
   *          if any.
   */
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
   * <p>isLatestEntry</p>
   *
   * @param entry a E object.
   * @return a boolean.
   */
  public boolean isLatestEntry( @Nonnull E entry ) {
    //noinspection ObjectEquality
    return getLatestEntry() == entry;
  }

  /**
   * Returns the begin
   *
   * @return the begin
   */
  @Nonnull
  public LocalDate getBegin() {
    return begin;
  }

  /**
   * Returns the end
   *
   * @return the end
   */
  @Nonnull
  public LocalDate getEnd() {
    return end;
  }

  /**
   * Appends an entry. The begin of the given entry must fit the end date of the last entry
   *
   * @param entry the entry that is added
   */
  public void addEntry( @Nonnull E entry ) {
    lock.writeLock().lock();
    try {
      validate( entry );

      this.entries.add( entry );

      //now sort the elements
      Collections.sort( entries );
    } finally {
      lock.writeLock().unlock();
    }
    notifyEntryAdded( entry );
  }

  /**
   * Is called when the entry has been changed
   *
   * @param entry the entry that has been changed
   */
  public void entryChanged( @Nonnull E entry ) {
    lock.writeLock().lock();
    try {
      //now sort the elements
      Collections.sort( entries );
    } finally {
      lock.writeLock().unlock();
    }
    notifyEntryChanged( entry );
  }

  /**
   * Returns the entry for the given date
   *
   * @param date the date
   * @return the entry
   *
   * @throws NoValidElementFoundException
   *          if any.
   */
  @Nonnull
  public E findEntry( @Nonnull LocalDate date ) throws NoValidElementFoundException {
    if ( date.isBefore( begin ) ) {
      throw new IllegalArgumentException( "Date " + date + " is before beginning of the history: " + begin );
    }
    if ( !date.isBefore( end ) ) {
      throw new IllegalArgumentException( "Date " + date + " is after end of the history: " + end );
    }

    lock.readLock().lock();
    E lastEntry = null;
    try {
      for ( E entry : entries ) {
        if ( entry.getBegin().isAfter( date ) ) {
          break;
        }
        lastEntry = entry;
      }
    } finally {
      lock.readLock().unlock();
    }
    if ( lastEntry == null ) {
      throw new NoValidElementFoundException( "Date too early: No entry found for " + date );
    } else {
      return lastEntry;
    }
  }

  /**
   * <p>findNextEntry</p>
   *
   * @param entry a E object.
   * @return a E object.
   *
   * @throws NoValidElementFoundException
   *          if any.
   */
  @Nonnull
  public E findNextEntry( @Nonnull E entry ) throws NoValidElementFoundException {
    for ( Iterator<E> it = entries.iterator(); it.hasNext(); ) {
      E actual = it.next();
      //noinspection ObjectEquality
      if ( actual == entry ) {
        if ( it.hasNext() ) {
          return it.next();
        } else {
          throw new NoValidElementFoundException( "Entry " + entry + " is the last entry in information" );
        }
      }
    }
    throw new IllegalArgumentException( "Entry " + entry + " is not in information" );
  }

  /**
   * <p>findEntries</p>
   *
   * @param begin a {@link LocalDate} object.
   * @param end   a {@link LocalDate} object.
   * @return a {@link List} object.
   */
  @Nonnull
  public List<? extends E> findEntries( @Nonnull LocalDate begin, @Nonnull LocalDate end ) {
    if ( end.isBefore( this.begin ) ) {
      throw new IllegalArgumentException( "End " + end + " is before beginning of the history: " + this.begin );
    }
    if ( begin.isAfter( this.end ) ) {
      throw new IllegalArgumentException( "Begin " + begin + " is after ending of the history: " + this.end );
    }

    List<E> foundEntries = new ArrayList<E>();
    lock.readLock().lock();
    try {
      for ( E entry : this.entries ) {
        if ( DateUtils.isBetween( entry.getBegin(), begin, end ) ) {
          foundEntries.add( entry );
        }
      }
    } finally {
      lock.readLock().unlock();
    }
    return foundEntries;
  }

  /**
   * <p>replaceEntry</p>
   *
   * @param entry a E object.
   */
  public void replaceEntry( @Nonnull E entry ) {
    lock.writeLock().lock();
    try {
      E replaced = findEntryWithBegin( entry.getBegin() );
      if ( replaced == null ) {
        throw new NoValidElementFoundException( "No entry found with begin that could be replaced " + entry.getBegin() );
      }
      removeEntry( replaced );
      addEntry( entry );
    } finally {
      lock.writeLock().unlock();
    }
  }


  /**
   * <p>findEntryWithBegin</p>
   *
   * @param date a {@link LocalDate} object.
   * @return a E object.
   */
  @Nullable
  protected E findEntryWithBegin( @Nonnull LocalDate date ) {
    lock.readLock().lock();
    try {
      for ( E entry : entries ) {
        if ( entry.getBegin().equals( date ) ) {
          return entry;
        }
      }
    } finally {
      lock.readLock().unlock();
    }
    return null;
  }

  /**
   * <p>validate</p>
   *
   * @param entry a E object.
   * @throws InvalidEntryException
   *          if any.
   */
  protected void validate( @Nonnull E entry ) throws InvalidEntryException {
    if ( findEntryWithBegin( entry.getBegin() ) != null ) {
      throw new InvalidEntryException( "An entry with the same begin still exists " + entry.getBegin() );
    }

    if ( entry.getBegin().isBefore( getBegin() ) ) {
      throw new InvalidEntryException( "Begin date does not fit. <" + entry.getBegin() + "> is before <" + getBegin() + ">" );
    }

    if ( entry.getBegin().isAfter( getEnd() ) ) {
      throw new InvalidEntryException( "End date is too late. <" + entry.getBegin() + "> is after <" + getEnd() + '>' );
    }
  }

  /**
   * Returns the end date for the given premium.
   *
   * @param entry the premium information the end is returned for
   * @return the end for the premium information
   */
  @Nonnull
  public LocalDate getEnd( @Nonnull E entry ) {
    lock.readLock().lock();
    try {
      for ( Iterator<E> it = entries.iterator(); it.hasNext(); ) {
        E current = it.next();
        //noinspection ObjectEquality
        if ( current == entry ) {
          if ( it.hasNext() ) {
            return it.next().getBegin();
          } else {
            return getEnd();
          }
        }
      }
    } finally {
      lock.readLock().unlock();
    }
    throw new IllegalArgumentException( "Entry does not exist within this history. " + entry );
  }

  /**
   * <p>removeHistoryListener</p>
   *
   * @param historyListener a {@link HistoryListener} object.
   */
  public void removeHistoryListener( @Nonnull HistoryListener<E> historyListener ) {
    listenerSupport.removeHistoryListener( historyListener );
  }

  /**
   * <p>addHistoryListener</p>
   *
   * @param historyListener a {@link HistoryListener} object.
   */
  public void addHistoryListener( @Nonnull HistoryListener<E> historyListener ) {
    listenerSupport.addHistoryListener( historyListener, true );
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
   * <p>getTransientHistoryListeners</p>
   *
   * @return a {@link List} object.
   */
  public List<? extends HistoryListener<E>> getTransientHistoryListeners() {
    return listenerSupport.getTransientHistoryListeners();
  }

  @Nonnull
  private final ClusteredHistoryListenerSupport<E> listenerSupport = new ClusteredHistoryListenerSupport<E>();

  private void notifyEntryRemoved( @Nonnull E removed ) {
    listenerSupport.notifyEntryRemoved( removed );
  }

  private void notifyEntryAdded( @Nonnull E entry ) {
    listenerSupport.notifyEntryAdded( entry );
  }

  private void notifyEntryChanged( @Nonnull E entry ) {
    listenerSupport.notifyEntryChanged( entry );
  }

  /**
   * <p>commitEntry</p>
   *
   * @param entry a E object.
   */
  public void commitEntry( @Nonnull E entry ) {
    listenerSupport.notifyEntryChanged( entry );
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
}
