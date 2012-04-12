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
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Redirects a stream into another
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class OutputRedirector implements Runnable {
  /**
   * Redirects the output of the given process to {@link System#out} and
   * {@link System#err}
   *
   * @param process the process
   * @return the redirecting threads
   */
  public static Thread[] redirect( @Nonnull Process process ) {
    PrintStream targetOut = System.out;
    PrintStream targetErr = System.err;
    return redirect( process, targetOut, targetErr );
  }

  /**
   * <p>redirect</p>
   *
   * @param process   a {@link Process} object.
   * @param targetOut a {@link OutputStream} the target output stream
   * @param targetErr a {@link OutputStream} the target input stream
   * @return the two redirecting threads
   */
  public static Thread[] redirect( @Nonnull Process process, @Nonnull OutputStream targetOut, @Nonnull OutputStream targetErr ) {
    Thread inputThread = new Thread( new OutputRedirector( process.getInputStream(), targetOut ) );
    inputThread.start();
    Thread outputThread = new Thread( new OutputRedirector( process.getErrorStream(), targetErr ) );
    outputThread.start();

    return new Thread[]{inputThread, outputThread};
  }

  private final InputStream in;
  private final OutputStream out;

  private int waitingPeriod = 100;
  private int bufferSize = 1024;

  /**
   * Redirect the given input stream to the output stream
   *
   * @param in  the input stream
   * @param out the output stream
   */
  public OutputRedirector( @Nonnull InputStream in, @Nonnull OutputStream out ) {
    this.in = in;
    this.out = out;
  }

  public int getBufferSize() {
    return bufferSize;
  }

  public void setBufferSize( int bufferSize ) {
    this.bufferSize = bufferSize;
  }

  /**
   * The waiting period that is used to wait for the stream
   *
   * @return the waiting period
   */
  public int getWaitingPeriod() {
    return waitingPeriod;
  }

  /**
   * Sets the waiting period
   *
   * @param waitingPeriod the waiting period
   */
  public void setWaitingPeriod( int waitingPeriod ) {
    this.waitingPeriod = waitingPeriod;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void run() {
    try {
      byte[] buffer = new byte[bufferSize];

      int readBytes;
      while ( !Thread.interrupted() && ( readBytes = in.read( buffer ) ) > -1 ) {
        out.write( buffer, 0, readBytes );
      }
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }
}
