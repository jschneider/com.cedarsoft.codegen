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

package com.cedarsoft.concurrent;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * <p>MultiReadWriteLock class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class MultiReadWriteLock implements ReadWriteLock {
  @Nonnull
  private final MultiLock multiReadLock;
  @Nonnull
  private final MultiLock multiWriteLock;

  /**
   * <p>Constructor for MultiReadWriteLock.</p>
   *
   * @param locks a {@link ReadWriteLock} object.
   */
  public MultiReadWriteLock( @Nonnull ReadWriteLock... locks ) {
    this( Arrays.asList( locks ) );
  }

  /**
   * <p>Constructor for MultiReadWriteLock.</p>
   *
   * @param locks a {@link List} object.
   */
  public MultiReadWriteLock( @Nonnull Iterable<? extends ReadWriteLock> locks ) {
    List<Lock> readLocks = new ArrayList<Lock>();
    List<Lock> writeLocks = new ArrayList<Lock>();

    for ( ReadWriteLock lock : locks ) {
      readLocks.add( lock.readLock() );
      writeLocks.add( lock.writeLock() );
    }

    if ( readLocks.isEmpty() ) {
      throw new IllegalArgumentException( "Need at least one lock" );
    }

    multiReadLock = new MultiLock( readLocks );
    multiWriteLock = new MultiLock( writeLocks );
  }


  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Lock readLock() {
    return multiReadLock;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Lock writeLock() {
    return multiWriteLock;
  }
}
