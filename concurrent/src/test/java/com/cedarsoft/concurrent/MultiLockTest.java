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

import com.cedarsoft.commons.ThreadUtils;
import javax.annotation.Nullable;

import org.junit.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;

/**
 *
 */
public class MultiLockTest {
  private Lock lock0;
  private Lock lock1;
  private MultiLock multiLock;

  @Before
  public void setUp() throws Exception {
    lock0 = new ReentrantLock();
    lock1 = new ReentrantLock();
    multiLock = new MultiLock( lock0, lock1 );
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testBasic() throws ExecutionException, InterruptedException {
    multiLock.lock();

    ThreadUtils.inokeInOtherThread(new Callable<Object>() {
      @Override
      @Nullable
      public Object call() throws Exception {
        assertFalse(lock0.tryLock());
        assertFalse(lock1.tryLock());
        return null;
      }
    });

    multiLock.unlock();
  }
}
