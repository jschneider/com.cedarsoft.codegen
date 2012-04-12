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
import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.WeakHashMap;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * For several dates a hole history of values is stored.
 * This can be used to store a hole history of entries related to a given date.
 * <p/>
 * Be careful: Listeners that have ben registered to a sub history aren't notified of entries changes!
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class DiscreteHistory<E extends DiscreteHistoryEntry> implements History<E> {
  @Nonnull
  private final ClusteredHistoryListenerSupport<E> listenerSupport = new ClusteredHistoryListenerSupport<E>();

  private Long id;

  @Nonnull
  private final ReadWriteLock lock = new ReentrantReadWriteLock();

  @Nonnull
  protected final SortedMap<LocalDate, DefaultHistory<E>> histories = new TreeMap<LocalDate, DefaultHistory<E>>();//todo make navigable

  /**
   * Returns the entries for the given date
   *
   * @param date the date the entries are searched for
   * @return the entries
   *
   * @throws HistoryNotFoundException
   *          if no history could be found for the given date
   */
  @Nonnull
  public List<? extends E> getEntries( @Nonnull LocalDate date ) throws HistoryNotFoundException {
    return getSubHistory( date ).getEntries();
  }

  /**
   * Returns the histories
   *
   * @return the histories
   */
  @Nonnull
  Map<LocalDate, DefaultHistory<E>> getHistories() {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      return Collections.unmodifiableMap( histories );
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Returns the latest entry for the given date
   *
   * @param date the date
   * @return the latest entry for the given date
   *
   * @throws HistoryNotFoundException
   *          if no history could be found for the given date
   */
  @Nonnull
  public E getLatestEntry( @Nonnull LocalDate date ) throws HistoryNotFoundException {
    return getSubHistory( date ).getLatestEntry();
  }

  /**
   * Returns the latest entry of each subhistory
   *
   * @return the latest entry of each subhistory
   */
  @Nonnull
  public List<? extends E> getLatestEntries() {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      List<E> latestEntries = new ArrayList<E>( histories.size() );

      for ( History<E> subHistory : histories.values() ) {
        latestEntries.add( subHistory.getLatestEntry() );
      }
      return latestEntries;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isLatestEntry( @Nonnull E entry ) {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      for ( History<E> subHistory : histories.values() ) {
        if ( subHistory.isLatestEntry( entry ) ) {
          return true;
        }
      }
      return false;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   * <p/>
   * Returns all entries
   */
  @Override
  @Nonnull
  public List<? extends E> getEntries() {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      List<E> entries = new ArrayList<E>( histories.size() );

      for ( History<E> subHistory : histories.values() ) {
        entries.addAll( subHistory.getEntries() );
      }
      return entries;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   * <p/>
   * Removes all entries
   */
  @Override
  public void clear() {
    lock.writeLock().lock();
    try {
      List<? extends E> entries = getEntries();
      for ( E entry : entries ) {
        removeEntry( entry );
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   * <p/>
   * Adds an entry.
   */
  @Override
  public void addEntry( @Nonnull E entry ) {
    getOrCreateSubHistory( entry.getValidityDate() ).addEntry( entry );
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
  public boolean hasEntries() {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      if ( histories.isEmpty() ) {
        return false;
      }
      for ( DefaultHistory<E> history : histories.values() ) {
        if ( history.hasEntries() ) {
          return true;
        }
      }
    } finally {
      lock.readLock().unlock();
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean removeEntry( @Nonnull E entry ) {
    ensureListenersRegistered();

    lock.writeLock().lock();
    try {
      for ( Iterator<DefaultHistory<E>> historyIterator = histories.values().iterator(); historyIterator.hasNext(); ) {
        DefaultHistory<E> history = historyIterator.next();
        if ( history.removeEntry( entry ) ) {
          //Remove history if empty
          if ( !history.hasEntries() ) {
            historyIterator.remove();
            HistoryListener<E> listener = getListeners().get( history );
            if ( listener != null ) {
              history.removeHistoryListener( listener );
            }
          }
          return true;
        }
      }
    } finally {
      lock.writeLock().unlock();
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public E getLatestEntry() throws NoValidElementFoundException {
    try {
      return getLastSubHistory().getLatestEntry();
    } catch ( HistoryNotFoundException e ) {
      throw new NoValidElementFoundException( e );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public E getFirstEntry() throws NoValidElementFoundException {
    try {
      return getFirstSubHistory().getFirstEntry();
    } catch ( HistoryNotFoundException e ) {
      throw new NoValidElementFoundException( e );
    }
  }

  @Nonnull
  private History<E> getOrCreateSubHistory( @Nonnull LocalDate date ) {
    ensureListenersRegistered();

    lock.writeLock().lock();
    try {
      DefaultHistory<E> history = histories.get( date );
      if ( history == null ) {
        history = new DefaultHistory<E>();
        registerListener( history );
        histories.put( date, history );
      }
      return history;
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Registers the listener for the given history
   *
   * @param history the history the listener is registered for
   */
  private void registerListener( @Nonnull DefaultHistory<E> history ) {
    lock.writeLock().lock();
    try {
      HistoryListener<E> listener = new HistoryListener<E>() {
        @Override
        public void entryAdded( @Nonnull E entry ) {
          listenerSupport.notifyEntryAdded( entry );
        }

        @Override
        public void entryChanged( @Nonnull E entry ) {
          listenerSupport.notifyEntryChanged( entry );
        }

        @Override
        public void entryRemoved( @Nonnull E entry ) {
          listenerSupport.notifyEntryRemoved( entry );
        }
      };
      history.addHistoryListener( listener );
      getListeners().put( history, listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * Returns the sub history for the given date
   *
   * @param date the date the sub history is searched for
   * @return the sub history for the given date
   *
   * @throws HistoryNotFoundException
   *          if any.
   */
  @Nonnull
  public History<E> getSubHistory( @Nonnull LocalDate date ) throws HistoryNotFoundException {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      History<E> subHistory = histories.get( date );
      if ( subHistory == null ) {
        throw new HistoryNotFoundException( "No sub history found for " + date );
      }
      return subHistory;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Returns the sub history before the given date
   *
   * @param date the date
   * @return the sub history before the given date
   *
   * @throws HistoryNotFoundException
   *          if any.
   */
  @Nonnull
  public History<E> getSubHistoryBefore( @Nonnull LocalDate date ) throws HistoryNotFoundException {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      if ( histories.isEmpty() ) {
        throw new HistoryNotFoundException( "No histories are available yet" );
      }

      //Iterating backwards would be much easier
      Map.Entry<LocalDate, DefaultHistory<E>> currentlyBest = null;
      for ( Map.Entry<LocalDate, DefaultHistory<E>> entry : histories.entrySet() ) {
        //if we are no longer before --> return the best value
        if ( !entry.getKey().isBefore( date ) ) {
          break;
        }

        if ( currentlyBest == null || currentlyBest.getKey().isBefore( entry.getKey() ) ) {
          currentlyBest = entry;
        }
      }

      //Now return the best value
      if ( currentlyBest == null ) {
        throw new HistoryNotFoundException( "No history found before " + date );
      } else {
        DefaultHistory<E> value = currentlyBest.getValue();
        if ( value == null ) {
          throw new IllegalStateException( "value is null for " + currentlyBest.getKey() );
        }
        return value;
      }
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Returns the best sub history for the given date.
   * This method returns the sub history for the given date (if there is one) or the
   * closest history *before* the given date
   *
   * @param date the date
   * @return the best history
   *
   * @throws HistoryNotFoundException
   *          if absolutly no history could be found
   */
  @Nonnull
  public History<E> getBestSubHistoryFor( @Nonnull LocalDate date ) throws HistoryNotFoundException {
    //Check for direct hit
    try {
      return getSubHistory( date );
    } catch ( HistoryNotFoundException ignore ) {
    }
    return getSubHistoryBefore( date );
  }

  /**
   * Returns the most up to date entry (over all histories)
   *
   * @return the most up to date entry (over all histories)
   *
   * @throws HistoryNotFoundException
   *          if any.
   */
  @Nonnull
  public E getMostUpToDateEntry() throws HistoryNotFoundException {
    ensureListenersRegistered();

    lock.readLock().lock();
    E upToDate = null;
    try {
      for ( History<E> history : histories.values() ) {
        for ( E entry : history.getEntries() ) {
          if ( upToDate == null || upToDate.getVerificationDate().isBefore( entry.getVerificationDate() ) ) {
            upToDate = entry;
          }
        }
      }
    } finally {
      lock.readLock().unlock();
    }
    if ( upToDate == null ) {
      throw new HistoryNotFoundException( "No cash value informations available" );
    }
    return upToDate;
  }

  /**
   * <p>getBestEntryFor</p>
   *
   * @param date a {@link LocalDate} object.
   * @return a E object.
   *
   * @throws HistoryNotFoundException
   *          if any.
   */
  @Nonnull
  public E getBestEntryFor( @Nonnull LocalDate date ) throws HistoryNotFoundException {
    return getBestSubHistoryFor( date ).getLatestEntry();
  }

  /**
   * <p>getLastSubHistory</p>
   *
   * @return a {@link History} object.
   *
   * @throws HistoryNotFoundException
   *          if any.
   */
  @Nonnull
  public History<E> getLastSubHistory() throws HistoryNotFoundException {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      if ( histories.isEmpty() ) {
        throw new HistoryNotFoundException( "No Subhistories available" );
      }
      return histories.get( histories.lastKey() );
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * <p>getFirstSubHistory</p>
   *
   * @return a {@link History} object.
   *
   * @throws HistoryNotFoundException
   *          if any.
   */
  @Nonnull
  public History<E> getFirstSubHistory() throws HistoryNotFoundException {
    ensureListenersRegistered();

    lock.readLock().lock();
    try {
      if ( histories.isEmpty() ) {
        throw new HistoryNotFoundException( "No Subhistories available" );
      }
      return histories.get( histories.firstKey() );
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
    throw new UnsupportedOperationException( "Cannot set the elements yet" ); //todo implement
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
   * <p>addHistoryListener</p>
   *
   * @param historyListener a {@link HistoryListener} object.
   * @param isTransient     a boolean.
   */
  public void addHistoryListener( @Nonnull HistoryListener<E> historyListener, boolean isTransient ) {
    listenerSupport.addHistoryListener( historyListener, isTransient );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<? extends HistoryListener<E>> getHistoryListeners() {
    return listenerSupport.getTransientHistoryListeners();
  }

  private transient Map<History<E>, HistoryListener<E>> listeners;

  @Nonnull
  private Map<History<E>, HistoryListener<E>> getListeners() {
    //First try with read access
    lock.readLock().lock();
    try {
      if ( listeners != null ) {
        return listeners;
      }
    } finally {
      lock.readLock().unlock();
    }

    //Now with write access
    lock.writeLock().lock();
    try {
      if ( listeners == null ) {
        listeners = new WeakHashMap<History<E>, HistoryListener<E>>();

        //Now create the listeners for the existing histories
        for ( DefaultHistory<E> history : histories.values() ) {
          this.registerListener( history );
        }
      }
      //noinspection ReturnOfCollectionOrArrayField
      return listeners;
    } finally {
      lock.writeLock().unlock();
    }
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

  /**
   * This method ensures that the listeners are initialized properly.
   * This is necessary to update the listeners on deserialized objects.
   */
  private void ensureListenersRegistered() {
    getListeners();
  }
}
