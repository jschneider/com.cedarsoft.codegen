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

package com.cedarsoft.lock;

import org.jetbrains.annotations.NotNull;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadInfo;
import java.lang.management.ThreadMXBean;
import java.util.Collection;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 *
 */
public class ThreadDeadlockDetector {
  @NotNull
  private final Timer threadCheck = new Timer( "ThreadDeadlockDetector", true );
  @NotNull
  private final ThreadMXBean mbean = ManagementFactory.getThreadMXBean();
  @NotNull
  private final Collection<Listener> listeners = new CopyOnWriteArraySet<Listener>();

  /**
   * The number of milliseconds between checking for deadlocks.
   * It may be expensive to check for deadlocks, and it is not
   * critical to know so quickly.
   */
  private static final int DEFAULT_DEADLOCK_CHECK_PERIOD = 10000;

  public ThreadDeadlockDetector() {
    this( DEFAULT_DEADLOCK_CHECK_PERIOD );
  }

  public ThreadDeadlockDetector( int deadlockCheckPeriod ) {
    threadCheck.schedule( new TimerTask() {
      @Override
      public void run() {
        checkForDeadlocks();
      }
    }, 10, deadlockCheckPeriod );
  }

  private void checkForDeadlocks() {
    long[] ids = findDeadlockedThreads();
    if ( ids != null && ids.length > 0 ) {
      Thread[] threads = new Thread[ids.length];
      for ( int i = 0; i < threads.length; i++ ) {
        threads[i] = findMatchingThread( mbean.getThreadInfo( ids[i] ) );
      }
      fireDeadlockDetected( threads );
    }
  }

  private long[] findDeadlockedThreads() {
    // JDK 1.5 only supports the findMonitorDeadlockedThreads()
    // method, so you need to comment out the following three lines
    if ( mbean.isSynchronizerUsageSupported() ) {
      return mbean.findDeadlockedThreads();
    } else {
      return mbean.findMonitorDeadlockedThreads();
    }
  }

  private void fireDeadlockDetected( Thread[] threads ) {
    for ( Listener l : listeners ) {
      l.deadlockDetected( threads );
    }
  }

  private static Thread findMatchingThread( ThreadInfo inf ) {
    for ( Thread thread : Thread.getAllStackTraces().keySet() ) {
      if ( thread.getId() == inf.getThreadId() ) {
        return thread;
      }
    }
    throw new IllegalStateException( "Deadlocked Thread not found" );
  }

  public boolean addListener( Listener l ) {
    return listeners.add( l );
  }

  public boolean removeListener( Listener l ) {
    return listeners.remove( l );
  }

  /**
   * This is called whenever a problem with threads is detected.
   */
  public interface Listener {
    void deadlockDetected( Thread[] deadlockedThreads );
  }
}
