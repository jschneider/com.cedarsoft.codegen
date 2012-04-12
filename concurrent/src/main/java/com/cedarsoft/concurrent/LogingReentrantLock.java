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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>LogingReentrantLock class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class LogingReentrantLock implements ReadWriteLock {
  @Nonnull
  private final ReadWriteLock delegate = new ReentrantReadWriteLock();

  @Nonnull
  private final List<Thread> readLockingThreads = new ArrayList<Thread>();
  private final List<Thread> writeLockingThreads = new ArrayList<Thread>();

  @Nonnull
  private final LogingLock readLock = new LogingLock( delegate.readLock(), readLockingThreads );
  @Nonnull
  private final LogingLock writeLock = new LogingLock( delegate.writeLock(), writeLockingThreads ) {
    @Override
    public void lock() {
      if ( !readLockingThreads.isEmpty() ) {
        throw new InvalidLockStateException( readLockingThreads );
      }
      super.lock();
    }
  };


  /**
   * {@inheritDoc}
   */
  @Override
  public Lock readLock() {
    return readLock;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Lock writeLock() {
    return writeLock;
  }

  private static class DelegatingLock implements Lock {
    private final Lock delegate;

    protected DelegatingLock( @Nonnull Lock delegate ) {
      this.delegate = delegate;
    }

    @Override
    public void lock() {
      delegate.lock();
    }

    @Override
    public void lockInterruptibly() throws InterruptedException {
      delegate.lockInterruptibly();
    }

    @Override
    public boolean tryLock() {
      return delegate.tryLock();
    }

    @Override
    public boolean tryLock( long time, TimeUnit unit ) throws InterruptedException {
      return delegate.tryLock( time, unit );
    }

    @Override
    public void unlock() {
      delegate.unlock();
    }

    @Override
    public Condition newCondition() {
      return delegate.newCondition();
    }
  }

  private static class LogingLock extends DelegatingLock {
    @Nonnull
    private final List<Thread> lockingThreads;

    private LogingLock( @Nonnull Lock delegate, @Nonnull List<Thread> lockingThreads ) {
      super( delegate );
      this.lockingThreads = lockingThreads;
    }

    @Override
    public void lock() {
      lockingThreads.add( 0, Thread.currentThread() );
      super.lock();
    }

    @Override
    public void unlock() {
      lockingThreads.remove( Thread.currentThread() );
      super.unlock();
    }

    @Nonnull
    public List<? extends Thread> getLockingThreads() {
      return Collections.unmodifiableList( lockingThreads );
    }
  }
}
