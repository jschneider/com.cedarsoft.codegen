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

package com.cedarsoft.io;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

/**
 * @noinspection RefusedBequest,SynchronizedMethod
 */
public class ReaderInputStream extends InputStream {
  @Nonnull
  private final Reader in;

  @Nonnull
  private final String encoding;

  @Nullable
  private byte[] slack;

  private int begin;

  public ReaderInputStream( @Nonnull Reader in ) {
    this( in, null );
  }

  public ReaderInputStream( @Nonnull Reader in, @Nonnull @Nullable String encoding ) {
    this.in = in;
    if ( encoding == null ) {
      this.encoding = System.getProperty( "file.encoding" );
    } else {
      this.encoding = encoding;
    }
  }

  /**
   * Reads from the <CODE>Reader</CODE>, returning the same value.
   *
   * @return the value of the next character in the <CODE>Reader</CODE>.
   *
   * @throws IOException if the original <code>Reader</code> fails to be read
   */
  @Override
  public int read() throws IOException {
    byte result;
    if ( slack != null && begin < slack.length ) {
      result = slack[begin];
      if ( ++begin == slack.length ) {
        slack = null;
      }
    } else {
      byte[] buf = new byte[1];
      if ( read( buf, 0, 1 ) <= 0 ) {
        result = -1;
      }
      result = buf[0];
    }

    if ( result < -1 ) {
      result += 256;
    }

    return result;
  }

  /**
   * Reads from the <code>Reader</code> into a byte array
   *
   * @param b   the byte array to read into
   * @param off the offset in the byte array
   * @param len the length in the byte array to fill
   * @return the actual number read into the byte array, -1 at
   *         the end of the stream
   *
   * @throws IOException if an error occurs
   */
  @Override
  public int read( byte[] b, int off, int len ) throws IOException {
    while ( slack == null ) {
      char[] buf = new char[len]; // might read too much
      int n = in.read( buf );
      if ( n == -1 ) {
        return -1;
      }
      if ( n > 0 ) {
        slack = new String( buf, 0, n ).getBytes( encoding );
        begin = 0;
      }
    }

    if ( len > slack.length - begin ) {
      len = slack.length - begin;
    }

    System.arraycopy( slack, begin, b, off, len );
    if ( ( begin += len ) >= slack.length ) {
      slack = null;
    }

    return len;
  }

  @Override
  public synchronized void mark( final int readlimit ) {
    try {
      in.mark( readlimit );
    } catch ( IOException ioe ) {
      throw new RuntimeException( ioe.getMessage() );
    }
  }

  @Override
  public synchronized int available() throws IOException {
    if ( slack != null ) {
      return slack.length - begin;
    }
    if ( in.ready() ) {
      return 1;
    } else {
      return 0;
    }
  }

  @Override
  public boolean markSupported() {
    return false;   // would be imprecise
  }

  @Override
  public synchronized void reset() throws IOException {
    slack = null;
    in.reset();
  }

  @Override
  public void close() throws IOException {
    in.close();
    slack = null;
  }
}
