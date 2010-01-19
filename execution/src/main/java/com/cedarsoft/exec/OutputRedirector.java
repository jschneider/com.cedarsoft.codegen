/**
 * Copyright (C) 2010 cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce.txt
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

package com.cedarsoft.exec;

import org.jetbrains.annotations.NotNull;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Redirects a stream into another
 */
public class OutputRedirector implements Runnable {
  /**
   * Redirects the output of the given process to {@link System#out} and
   * {@link System#err}
   *
   * @param process the process
   */
  public static void redirect( @NotNull Process process ) {
    PrintStream targetOut = System.out;
    PrintStream targetErr = System.err;
    redirect( process, targetOut, targetErr );
  }

  public static void redirect( @NotNull Process process, @NotNull OutputStream targetOut, @NotNull OutputStream targetErr ) {
    new Thread( new OutputRedirector( process.getInputStream(), targetOut ) ).start();
    new Thread( new OutputRedirector( process.getErrorStream(), targetErr ) ).start();
  }

  private final InputStream in;
  private final OutputStream out;

  /**
   * Redirect the given input stream to the output stream
   *
   * @param in  the input stream
   * @param out the output stream
   */
  public OutputRedirector( @NotNull InputStream in, @NotNull OutputStream out ) {
    this.in = in;
    this.out = out;
  }

  @Override
  public void run() {
    try {
      BufferedInputStream inputStream = null;
      try {
        inputStream = new BufferedInputStream( in );
        int c;
        while ( ( c = inputStream.read() ) > -1 ) {
          out.write( ( char ) c );
        }
      } finally {
        if ( inputStream != null ) {
          inputStream.close();
        }
      }
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }
}
