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

import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * A view on an ObservableObjectAccess that shows only some elements of the base object access...
 * This can be used to create an object access for only a sub type of the elements contains within an object access.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ObservableObjectAccessView<E> implements ClusteredObservableObjectAccess<E> {
  @Nonnull
  private final ClusteredObservableObjectAccess<? super E> base;

  @Nonnull
  private final ClusteredElementsCollection<E> view = new ClusteredElementsCollection<E>();

  /**
   * <p>Constructor for ObservableObjectAccessView.</p>
   *
   * @param base   a {@link ClusteredObservableObjectAccess} object.
   * @param bridge a {@link ObservableObjectAccessView.Bridge} object.
   */
  public ObservableObjectAccessView( @Nonnull ClusteredObservableObjectAccess<? super E> base, @Nonnull final Bridge<E> bridge ) {
    this.base = base;

    //fill with the initial values
    for ( Object element : base.getElements() ) {
      E bridged = bridge.getBridged( element );
      if ( bridged != null ) {
        view.add( bridged );
      }
    }

    this.base.addElementListener( new SingleElementsListener<Object>() {
      @Override
      public void elementDeleted( @Nonnull ObservableCollection<? extends Object> source, @Nonnull Object element, int index ) {
        E bridged = bridge.getBridged( element );
        if ( bridged != null ) {
          view.remove( bridged );
        }
      }

      @Override
      public void elementAdded( @Nonnull ObservableCollection<? extends Object> source, @Nonnull Object element, int index ) {
        E bridged = bridge.getBridged( element );
        if ( bridged != null ) {
          view.add( bridged );
        }
      }

      @Override
      public void elementChanged( @Nonnull ObservableCollection<? extends Object> source, @Nonnull Object element, int index ) {
        E bridged = bridge.getBridged( element );
        if ( bridged != null ) {
          view.commit( bridged );
        }
      }
    }, false );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void commit( @Nonnull E element ) {
    base.commit( element );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove( @Nonnull E element ) {
    base.remove( element );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void add( @Nonnull E element ) {
    base.add( element );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setElements( @Nonnull List<? extends E> elements ) {
    base.setElements( elements );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<? extends E> getElements() {
    return view.getElements();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addElementListener( @Nonnull ElementsListener<? super E> listener ) {
    view.addElementListener( listener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addElementListener( @Nonnull ElementsListener<? super E> listener, boolean isTransient ) {
    view.addElementListener( listener, isTransient );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeElementListener( @Nonnull ElementsListener<? super E> listener ) {
    view.removeElementListener( listener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public ReentrantReadWriteLock getLock() {
    return view.getLock();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<? extends ElementsListener<? super E>> getTransientElementListeners() {
    return view.getTransientElementListeners();
  }

  public interface Bridge<T> {
    /**
     * returns the bridged object (or null if there isn't a bridged object
     *
     * @param element the element
     * @return the bridged object or null
     */
    @Nullable
    T getBridged( @Nonnull Object element );
  }

  public static class TypeBridge<T> implements Bridge<T> {
    @Nonnull
    private final Class<T> type;

    public TypeBridge( @Nonnull Class<T> type ) {
      this.type = type;
    }

    @Override
    @Nullable
    public T getBridged( @Nonnull Object element ) {
      if ( type.isAssignableFrom( element.getClass() ) ) {
        return type.cast( element );
      } else {
        return null;
      }
    }
  }
}
