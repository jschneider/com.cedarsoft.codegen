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

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <p>ThreadDeadlockDetector class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ThreadDeadlockDetector {
  /**
   * The number of milliseconds between checking for deadlocks.
   * It may be expensive to check for deadlocks, and it is not
   * critical to know so quickly.
   */
  private static final long DEFAULT_DEADLOCK_CHECK_PERIOD = 10000;
  @Nonnull
  private final Timer threadCheck = new Timer( "ThreadDeadlockDetector", true );
  @Nonnull
  private final ThreadMXBean mXBean = ManagementFactory.getThreadMXBean();

  private final long deadlockCheckPeriod;
  @Nonnull
  private final Collection<Listener> listeners = new CopyOnWriteArraySet<Listener>();

  /**
   * <p>Constructor for ThreadDeadlockDetector.</p>
   */
  public ThreadDeadlockDetector() {
    this( DEFAULT_DEADLOCK_CHECK_PERIOD );
  }

  /**
   * <p>Constructor for ThreadDeadlockDetector.</p>
   *
   * @param deadlockCheckPeriod a int.
   */
  public ThreadDeadlockDetector( long deadlockCheckPeriod ) {
    this.deadlockCheckPeriod = deadlockCheckPeriod;
  }

  public void start() {
    threadCheck.schedule( new TimerTask() {
      @Override
      public void run() {
        checkForDeadlocks();
      }
    }, this.deadlockCheckPeriod );
  }

  public void stop() {
    threadCheck.cancel();
  }

  private void checkForDeadlocks() {
    long[] ids = findDeadlockedThreads();
    if ( ids != null && ids.length > 0 ) {
      Set<Thread> threads = new HashSet<Thread>();

      for ( long id : ids ) {
        threads.add( findMatchingThread( mXBean.getThreadInfo( id ) ) );
      }
      fireDeadlockDetected( threads );
    }
    fireCheckRun(  );
  }

  private long[] findDeadlockedThreads() {
    // JDK 1.5 only supports the findMonitorDeadlockedThreads()
    // method, so you need to comment out the following three lines
    if ( mXBean.isSynchronizerUsageSupported() ) {
      return mXBean.findDeadlockedThreads();
    } else {
      return mXBean.findMonitorDeadlockedThreads();
    }
  }

  private void fireCheckRun() {
    for ( Listener listener : listeners ) {
      if ( listener instanceof DetailedListener ) {
        ( ( DetailedListener ) listener ).checkHasBeenRun();
      }
    }
  }

  private void fireDeadlockDetected( @Nonnull Set<? extends Thread> threads ) {
    for ( Listener listener : listeners ) {
      listener.deadlockDetected( threads );
    }
  }

  @Nonnull
  private static Thread findMatchingThread( @Nonnull ThreadInfo threadInfo ) {
    for ( Thread thread : Thread.getAllStackTraces().keySet() ) {
      if ( thread.getId() == threadInfo.getThreadId() ) {
        return thread;
      }
    }
    throw new IllegalStateException( "Deadlocked Thread not found" );
  }

  /**
   * <p>addListener</p>
   *
   * @param listener a {@link ThreadDeadlockDetector.Listener} object.
   * @return a boolean.
   */
  public boolean addListener( @Nonnull Listener listener ) {
    return listeners.add( listener );
  }

  /**
   * <p>removeListener</p>
   *
   * @param listener a {@link ThreadDeadlockDetector.Listener} object.
   * @return a boolean.
   */
  public boolean removeListener( @Nonnull Listener listener ) {
    return listeners.remove( listener );
  }

  /**
   * This is called whenever a problem with threads is detected.
   */
  public interface Listener {
    void deadlockDetected( @Nonnull Set<? extends Thread> deadlockedThreads );
  }

  public interface DetailedListener extends Listener{
    void checkHasBeenRun();
  }
}
