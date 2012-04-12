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
import javax.annotation.Nullable;

import javax.swing.SwingUtilities;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Offers static thread utils
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ThreadUtils {
  /**
   * <p>isEventDispatchThread</p>
   *
   * @return a boolean.
   */
  public static boolean isEventDispatchThread() {
    return SwingUtilities.isEventDispatchThread();
  }

  /**
   * <p>assertEventDispatchThread</p>
   *
   * @throws IllegalThreadStateException if any.
   */
  public static void assertEventDispatchThread() throws IllegalThreadStateException {
    if ( !isEventDispatchThread() ) {
      throw new IllegalThreadStateException( "Not in EDT" );
    }
  }

  /**
   * <p>assertNotEventDispatchThread</p>
   *
   * @throws IllegalThreadStateException if any.
   */
  public static void assertNotEventDispatchThread() throws IllegalThreadStateException {
    if ( isEventDispatchThread() ) {
      throw new IllegalThreadStateException( "Is EDT" );
    }
  }

  /**
   * <p>inokeInOtherThread</p>
   *
   * @param callable a {@link Callable} object.
   * @return a T object.
   *
   * @throws ExecutionException   if any.
   * @throws InterruptedException if any.
   */
  @Nullable
  public static <T> T inokeInOtherThread( @Nonnull Callable<T> callable ) throws ExecutionException, InterruptedException {
    ExecutorService executor = Executors.newSingleThreadExecutor();
    try {
      Future<T> future = executor.submit( callable );
      return future.get();
    } finally {
      executor.shutdown();
    }
  }

  /**
   * Invokes the runnable within the EDT
   *
   * @param runnable a {@link Runnable} object.
   */
  public static void invokeInEventDispatchThread( @Nonnull Runnable runnable ) {
    if ( isEventDispatchThread() ) {
      runnable.run();
    } else {
      try {
        SwingUtilities.invokeAndWait( runnable );
      } catch ( InterruptedException e ) {
        throw new RuntimeException( e );
      } catch ( InvocationTargetException e ) {
        Throwable targetException = e.getTargetException();
        if ( targetException instanceof RuntimeException ) {
          throw ( RuntimeException ) targetException;
        }
        //noinspection ThrowInsideCatchBlockWhichIgnoresCaughtException
        throw new RuntimeException( targetException );
      }
    }
  }

  public static void waitForEventDispatchThread() {
    invokeInEventDispatchThread( new Runnable() {
      @Override
      public void run() {
      }
    } );
  }
}
