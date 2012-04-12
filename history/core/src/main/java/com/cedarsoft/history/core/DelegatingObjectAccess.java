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

import com.cedarsoft.commons.NullLock;
import com.cedarsoft.concurrent.Lockable;
import com.cedarsoft.objectaccess.PartTimeObjectAdd;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * An object access that is based on the current selection
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class DelegatingObjectAccess<T> implements ObservableObjectAccess<T>, PartTimeObjectAdd<T> {
  @Nonnull
  private final ElementsListener<T> delegatingListener = new ElementsListener<T>() {
    @Override
    public void elementsDeleted( @Nonnull ElementsChangedEvent<? extends T> event ) {
      for ( ElementsListener<? super T> listener : listeners ) {
        listener.elementsDeleted( event );
      }
    }

    @Override
    public void elementsAdded( @Nonnull ElementsChangedEvent<? extends T> event ) {
      for ( ElementsListener<? super T> listener : listeners ) {
        listener.elementsAdded( event );
      }
    }

    @Override
    public void elementsChanged( @Nonnull ElementsChangedEvent<? extends T> event ) {
      for ( ElementsListener<? super T> listener : listeners ) {
        listener.elementsChanged( event );
      }
    }
  };

  @Nonnull
  private final List<ElementsListener<? super T>> listeners = new ArrayList<ElementsListener<? super T>>();

  @Nonnull
  private final List<DelegateListener<T>> delegateListeners = new ArrayList<DelegateListener<T>>();

  @Nonnull
  private final List<PartTimeListener> partTimeListeners = new ArrayList<PartTimeListener>();

  /**
   * Creates a new delegating object access
   *
   * @param currentDelegate the current  delegate
   */
  public DelegatingObjectAccess( @Nullable ObservableObjectAccess<T> currentDelegate ) {
    this.currentDelegate = currentDelegate;
  }

  /**
   * Returns the current delegate
   *
   * @return the current delegate
   */
  @Nonnull
  protected ObservableObjectAccess<T> getCurrentDelegateSafe() {
    if ( currentDelegate == null ) {
      throw new IllegalStateException( "No current delegate available" );
    }
    return currentDelegate;
  }

  /**
   * <p>Getter for the field <code>currentDelegate</code>.</p>
   *
   * @return a {@link ObservableObjectAccess} object.
   */
  @Nullable
  public ObservableObjectAccess<T> getCurrentDelegate() {
    return currentDelegate;
  }

  @Nullable
  protected ObservableObjectAccess<T> currentDelegate;

  /**
   * <p>isCurrentDelegatingObjectAccessAvailable</p>
   *
   * @return a boolean.
   */
  public boolean isCurrentDelegatingObjectAccessAvailable() {
    return currentDelegate != null;
  }

  /**
   * Sets the current delegate
   *
   * @param currentDelegate the current delegate
   */
  public void setCurrentDelegate( @Nullable ObservableObjectAccess<T> currentDelegate ) {
    //noinspection ObjectEquality
    if ( this.currentDelegate == currentDelegate ) {
      return;
    }

    ObservableObjectAccess<T> oldDelegate = this.currentDelegate;
    this.currentDelegate = currentDelegate;
    unregister( oldDelegate );
    register();

    //Notify the listeners
    for ( DelegateListener<T> delegateListener : delegateListeners ) {
      delegateListener.delegateChanged( currentDelegate );
    }

    if ( this.currentDelegate == null ) { //has changed to *not* available
      for ( PartTimeListener partTimeListener : partTimeListeners ) {
        partTimeListener.addNotAvailable();
      }
    } else if ( oldDelegate == null ) { //has changed to available
      for ( PartTimeListener partTimeListener : partTimeListeners ) {
        partTimeListener.addAvailable();
      }
    }
  }

  private void register() {
    ObservableObjectAccess<T> delegate = currentDelegate;
    if ( delegate != null ) {
      ReadWriteLock lock = getLock();

      lock.writeLock().lock();
      try {
        //Remove all meetings
        for ( ElementsListener<? super T> listener : listeners ) {
          listener.elementsAdded( new ElementsChangedEvent<T>( delegate, delegate.getElements(), 0, delegate.getElements().size() - 1 ) );
        }
        delegate.addElementListener( delegatingListener );
      } finally {
        lock.writeLock().unlock();
      }
    }
  }

  private void unregister( @Nullable ObservableObjectAccess<T> oldDelegate ) {
    if ( oldDelegate == null ) {
      return;
    }

    ReadWriteLock lock = getLock( oldDelegate );

    lock.writeLock().lock();
    try {
      //Notify the listeners about removal of all elements
      for ( ElementsListener<? super T> listener : listeners ) {
        listener.elementsDeleted( new ElementsChangedEvent<T>( oldDelegate, oldDelegate.getElements(), 0, oldDelegate.getElements().size() - 1 ) );
      }
      oldDelegate.removeElementListener( delegatingListener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   * <p/>
   * Returns the lock for the current delegate
   */
  @Override
  @Nonnull
  public ReadWriteLock getLock() {
    ObservableObjectAccess<T> delegate = getCurrentDelegateSafe();
    return getLock( delegate );
  }

  @Nonnull
  private ReadWriteLock getLock( @Nullable ObservableObjectAccess<T> delegate ) {
    if ( delegate != null && delegate instanceof Lockable ) {
      return ( (Lockable) delegate ).getLock();
    } else {
      return NullLock.LOCK;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void commit( @Nonnull T element ) {
    getCurrentDelegateSafe().commit( element );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<? extends T> getElements() {
    if ( isCurrentDelegatingObjectAccessAvailable() ) {
      return getCurrentDelegateSafe().getElements();
    } else {
      return Collections.emptyList();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void add( @Nonnull T element ) {
    getCurrentDelegateSafe().add( element );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setElements( @Nonnull List<? extends T> elements ) {
    getCurrentDelegateSafe().setElements( elements );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove( @Nonnull T element ) {
    getCurrentDelegateSafe().remove( element );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addElementListener( @Nonnull ElementsListener<? super T> listener ) {
    this.listeners.add( listener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeElementListener( @Nonnull ElementsListener<? super T> listener ) {
    this.listeners.remove( listener );
  }

  /**
   * <p>addDelegateListener</p>
   *
   * @param listener a {@link DelegatingObjectAccess.DelegateListener} object.
   */
  public void addDelegateListener( @Nonnull DelegateListener<T> listener ) {
    this.delegateListeners.add( listener );
  }

  /**
   * <p>removeDelegateListener</p>
   *
   * @param listener a {@link DelegatingObjectAccess.DelegateListener} object.
   */
  public void removeDelegateListener( @Nonnull DelegateListener<T> listener ) {
    this.delegateListeners.remove( listener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean canAdd() {
    return isCurrentDelegatingObjectAccessAvailable();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addPartTimeListener( @Nonnull PartTimeListener listener ) {
    partTimeListeners.add( listener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removePartTimeListener( @Nonnull PartTimeListener listener ) {
    partTimeListeners.remove( listener );
  }

  public interface DelegateListener<T> {
    /**
     * Is called if the delegate has changed
     *
     * @param newDelegate the new delegate
     */
    void delegateChanged( @Nullable ObservableObjectAccess<T> newDelegate );
  }
}
