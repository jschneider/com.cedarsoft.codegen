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

package com.cedarsoft.execution;

import javax.annotation.Nonnull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Executes a process and notifies execution listeners whenever the process has finished
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
@SuppressWarnings( {"UseOfProcessBuilder"} )
public class Executor {
  @Nonnull
  private final List<ExecutionListener> executionListeners = new ArrayList<ExecutionListener>();

  @Nonnull
  private final ProcessBuilder processBuilder;
  private boolean redirectStreams = true;

  /**
   * <p>Constructor for Executor.</p>
   *
   * @param processBuilder  a {@link ProcessBuilder} object.
   * @param redirectStreams a boolean.
   */
  public Executor( @Nonnull ProcessBuilder processBuilder, boolean redirectStreams ) {
    this.processBuilder = processBuilder;
    this.redirectStreams = redirectStreams;
  }

  /**
   * <p>Constructor for Executor.</p>
   *
   * @param processBuilder a {@link ProcessBuilder} object.
   */
  public Executor( @Nonnull ProcessBuilder processBuilder ) {
    this( processBuilder, true );
  }

  /**
   * <p>execute</p>
   *
   * @return a int.
   *
   * @throws IOException          if any.
   * @throws InterruptedException if any.
   */
  public int execute() throws IOException, InterruptedException {
    Process process = processBuilder.start();
    Thread[] threads = redirectStreams( process );
    notifyExecutionStarted( process );

    int answer = process.waitFor();

    //Wait for the redirecting threads
    for ( Thread thread : threads ) {
      thread.join();
    }

    notifyExecutionFinished( answer );
    return answer;
  }

  /**
   * <p>redirectStreams</p>
   *
   * @param process a {@link Process} object.
   * @return the redirecting threads (or an empty array)
   */
  @Nonnull
  protected Thread[] redirectStreams( @Nonnull Process process ) {
    if ( redirectStreams ) {
      return OutputRedirector.redirect( process );
    } else {
      return new Thread[0];
    }
  }

  /**
   * <p>executeAsync</p>
   *
   * @return the thread
   */
  @Nonnull
  public Thread executeAsync() {
    Thread thread = new Thread( new Runnable() {
      @Override
      public void run() {
        try {
          execute();
        } catch ( Exception e ) {
          throw new RuntimeException( e );
        }
      }
    } );
    thread.start();
    return thread;
  }

  private void notifyExecutionFinished( int answer ) {
    for ( ExecutionListener executionListener : executionListeners ) {
      executionListener.executionFinished( answer );
    }
  }

  private void notifyExecutionStarted( @Nonnull Process process ) {
    for ( ExecutionListener executionListener : executionListeners ) {
      executionListener.executionStarted( process );
    }
  }

  /**
   * <p>addExecutionListener</p>
   *
   * @param executionListener a {@link ExecutionListener} object.
   */
  public void addExecutionListener( @Nonnull ExecutionListener executionListener ) {
    this.executionListeners.add( executionListener );
  }

  /**
   * <p>removeExecutionListener</p>
   *
   * @param executionListener a {@link ExecutionListener} object.
   */
  public void removeExecutionListener( @Nonnull ExecutionListener executionListener ) {
    this.executionListeners.remove( executionListener );
  }

  /**
   * <p>isRedirectStreams</p>
   *
   * @return a boolean.
   */
  public boolean isRedirectStreams() {
    return redirectStreams;
  }

  /**
   * <p>Setter for the field <code>redirectStreams</code>.</p>
   *
   * @param redirectStreams a boolean.
   */
  public void setRedirectStreams( boolean redirectStreams ) {
    this.redirectStreams = redirectStreams;
  }
}
