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

import com.cedarsoft.cluster.event.ClusteredPropertyChangeSupport;

import javax.annotation.Nonnull;
import org.joda.time.LocalDate;

import java.beans.PropertyChangeListener;
import java.lang.String;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * An entry within a history.
 * Every entry spans an interval.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class DefaultContinuousEntry implements ContinuousEntry {
  @Nonnull
  protected ReadWriteLock lock = new ReentrantReadWriteLock();

  @Nonnull
  protected final ClusteredPropertyChangeSupport pcs = new ClusteredPropertyChangeSupport( this );

  private Long id;

  @Nonnull
  private LocalDate begin;

  /**
   * Hibernate
   */
  @Deprecated
  public DefaultContinuousEntry() {
  }

  /**
   * <p>Constructor for DefaultContinuousEntry.</p>
   *
   * @param begin a {@link LocalDate} object.
   */
  public DefaultContinuousEntry( @Nonnull LocalDate begin ) {
    this.begin = begin;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public LocalDate getBegin() {
    lock.readLock().lock();
    try {
      return begin;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setBegin( @Nonnull LocalDate begin ) {
    lock.writeLock().lock();
    try {
      pcs.firePropertyChange( PROPERTY_BEGIN, this.begin, this.begin = begin );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo( ContinuousEntry o ) {
    return getBegin().compareTo( o.getBegin() );
  }

  /**
   * <p>removePropertyChangeListener</p>
   *
   * @param listener a {@link PropertyChangeListener} object.
   */
  public void removePropertyChangeListener( @Nonnull PropertyChangeListener listener ) {
    lock.writeLock().lock();
    try {
      pcs.removePropertyChangeListener( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>removePropertyChangeListener</p>
   *
   * @param propertyName a {@link String} object.
   * @param listener     a {@link PropertyChangeListener} object.
   */
  public void removePropertyChangeListener( @Nonnull String propertyName, @Nonnull PropertyChangeListener listener ) {
    lock.writeLock().lock();
    try {
      pcs.removePropertyChangeListener( propertyName, listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>addPropertyChangeListener</p>
   *
   * @param listener a {@link PropertyChangeListener} object.
   */
  public void addPropertyChangeListener( @Nonnull PropertyChangeListener listener ) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener( listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>addPropertyChangeListener</p>
   *
   * @param listener    a {@link PropertyChangeListener} object.
   * @param isTransient a boolean.
   */
  public void addPropertyChangeListener( @Nonnull PropertyChangeListener listener, boolean isTransient ) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener( listener, isTransient );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>addPropertyChangeListener</p>
   *
   * @param propertyName a {@link String} object.
   * @param listener     a {@link PropertyChangeListener} object.
   */
  public void addPropertyChangeListener( @Nonnull String propertyName, @Nonnull PropertyChangeListener listener ) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener( propertyName, listener );
    } finally {
      lock.writeLock().unlock();
    }
  }

  /**
   * <p>addPropertyChangeListener</p>
   *
   * @param propertyName a {@link String} object.
   * @param listener     a {@link PropertyChangeListener} object.
   * @param isTransient  a boolean.
   */
  public void addPropertyChangeListener( @Nonnull String propertyName, @Nonnull PropertyChangeListener listener, boolean isTransient ) {
    lock.writeLock().lock();
    try {
      pcs.addPropertyChangeListener( propertyName, listener, isTransient );
    } finally {
      lock.writeLock().unlock();
    }
  }
}
