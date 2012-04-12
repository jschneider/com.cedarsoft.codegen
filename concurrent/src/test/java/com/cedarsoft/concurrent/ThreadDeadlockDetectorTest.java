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

import com.cedarsoft.test.utils.MockitoTemplate;
import com.google.common.collect.Sets;
import javax.annotation.Nonnull;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Set;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.junit.Assert.*;

/**
 *
 */
public class ThreadDeadlockDetectorTest {

  @Test( timeout = 2000 )
  public void testBasic() throws Exception {
    final Lock lock1 = new ReentrantLock();
    final Lock lock2 = new ReentrantLock();

    final CyclicBarrier barrier = new CyclicBarrier( 2 );

    final Thread thread1 = new Thread( new MyRunnable( lock1, lock2, barrier ), "dead-thread1" );
    final Thread thread2 = new Thread( new MyRunnable( lock2, lock1, barrier ), "dead-thread2" );

    new MockitoTemplate() {
      @Mock
      private ThreadDeadlockDetector.Listener listener;
      @Nonnull
      private final ByteArrayOutputStream out = new ByteArrayOutputStream();

      @Override
      protected void stub() throws Exception {
      }

      @Override
      protected void execute() throws Exception {
        ThreadDeadlockDetector deadlockDetector = new ThreadDeadlockDetector( 10 );
        deadlockDetector.addListener( listener );
        deadlockDetector.addListener( new DefaultDeadlockListener( new PrintStream( out ) ) );
        thread1.start();
        thread2.start();


        while ( lock1.tryLock() ) {
          lock1.unlock();
        }
        while ( lock2.tryLock() ) {
          lock2.unlock();
        }


        final Lock hasBeenRunLock = new ReentrantLock();
        final Condition hasBeenRun = hasBeenRunLock.newCondition();
        deadlockDetector.addListener( new ThreadDeadlockDetector.DetailedListener() {
          @Override
          public void checkHasBeenRun() {
            hasBeenRunLock.lock();
            try {
              hasBeenRun.signalAll();
            } finally {
              hasBeenRunLock.unlock();
            }
          }

          @Override
          public void deadlockDetected( @Nonnull Set<? extends Thread> deadlockedThreads ) {
          }
        } );

        hasBeenRunLock.lock();
        try {
          deadlockDetector.start();
          hasBeenRun.await();
        } finally {
          hasBeenRunLock.unlock();
        }

        deadlockDetector.stop();

        assertTrue( out.toString(), out.toString().contains( "Deadlocked Threads:" ) );
        assertTrue( out.toString(), out.toString().contains( "Thread[dead-thread1,5,main]" ) );
        assertTrue( out.toString(), out.toString().contains( "Thread[dead-thread2,5,main]" ) );
        assertTrue( out.toString(), out.toString().contains( "sun.misc.Unsafe.park(Native Method)" ) );
      }

      @Override
      protected void verifyMocks() throws Exception {
        Mockito.verify( listener ).deadlockDetected( Sets.newHashSet( thread1, thread2 ) );
        Mockito.verifyNoMoreInteractions( listener );
      }
    }.run();
  }

  private static class MyRunnable implements Runnable {
    private final Lock lock1;
    private final CyclicBarrier barrier;
    private final Lock lock2;

    private MyRunnable( Lock lock1, Lock lock2, CyclicBarrier barrier ) {
      this.lock1 = lock1;
      this.barrier = barrier;
      this.lock2 = lock2;
    }

    @Override
    public void run() {
      lock1.lock();
      try {
        barrier.await();
      } catch ( InterruptedException e ) {
        throw new RuntimeException( e );
      } catch ( BrokenBarrierException e ) {
        throw new RuntimeException( e );
      }

      lock2.lock();
    }
  }
}
