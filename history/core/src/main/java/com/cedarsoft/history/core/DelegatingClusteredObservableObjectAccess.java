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

import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * <p>Abstract DelegatingClusteredObservableObjectAccess class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public abstract class DelegatingClusteredObservableObjectAccess<T> implements ClusteredObservableObjectAccess<T> {
  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public ReadWriteLock getLock() {
    return getDelegate().getLock();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void commit( @Nonnull T element ) {
    getDelegate().commit( element );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void remove( @Nonnull T element ) {
    getDelegate().remove( element );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void add( @Nonnull T element ) {
    getDelegate().add( element );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setElements( @Nonnull List<? extends T> elements ) {
    getDelegate().setElements( elements );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<? extends T> getElements() {
    return getDelegate().getElements();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addElementListener( @Nonnull ElementsListener<? super T> listener ) {
    getDelegate().addElementListener( listener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addElementListener( @Nonnull ElementsListener<? super T> listener, boolean isTransient ) {
    getDelegate().addElementListener( listener, isTransient );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeElementListener( @Nonnull ElementsListener<? super T> listener ) {
    getDelegate().removeElementListener( listener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<? extends ElementsListener<? super T>> getTransientElementListeners() {
    return getDelegate().getTransientElementListeners();
  }

  /**
   * Returns the delegate
   *
   * @return the delegate
   */
  @Nonnull
  public abstract ClusteredObservableObjectAccess<T> getDelegate();
}
