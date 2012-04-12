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

package com.cedarsoft.commons;

import javax.annotation.Nonnull;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;

/**
 * An Null-Lock that does nothing
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 * @noinspection Singleton
 */
public class NullLock implements Lock, ReadWriteLock {
  /**
   * Constant <code>LOCK</code>
   */
  @Nonnull
  public static final NullLock LOCK = new NullLock();

  private NullLock() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Lock readLock() {
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Lock writeLock() {
    return this;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void lock() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void lockInterruptibly() throws InterruptedException {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean tryLock() {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean tryLock( long time, TimeUnit unit ) throws InterruptedException {
    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void unlock() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Condition newCondition() {
    throw new NullPointerException( "Cannot create a condition for a null lock" );
  }
}
