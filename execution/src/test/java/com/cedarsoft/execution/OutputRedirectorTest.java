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

import org.junit.*;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class OutputRedirectorTest {
  @Test( timeout = 1000 )
  public void testDefault() {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    OutputRedirector outputRedirector = new OutputRedirector( new ByteArrayInputStream( "asdf".getBytes() ), out );
    outputRedirector.run();

    assertEquals( "asdf", out.toString() );
  }


  @Test( timeout = 1000 )
  public void testThreaded() throws InterruptedException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    OutputRedirector outputRedirector = new OutputRedirector( new ByteArrayInputStream( "asdf".getBytes() ), out );
    Thread thread = new Thread( outputRedirector );
    thread.start();

    thread.join();
    assertEquals( "asdf", out.toString() );
  }

  @Test
  public void testBlocking() throws Exception {
    ByteArrayOutputStream out = new ByteArrayOutputStream();

    OutputRedirector outputRedirector = new OutputRedirector( new BlockingInputStream(), out );
    Thread thread = new Thread( outputRedirector );
    thread.start();

    thread.join( 100 );
    assertTrue( thread.isAlive() );
    assertEquals( "*", out.toString() );

    thread.join( 100 );
    assertTrue( thread.isAlive() );
    thread.interrupt();
    thread.join( 100 );
    assertFalse( thread.isAlive() );
  }

  @Test
  public void testStreams() throws Exception {
    ByteArrayInputStream stream = new ByteArrayInputStream( "asdf".getBytes() );
    assertEquals( 4, stream.available() );
    stream.read();
    assertEquals( 3, stream.available() );
    stream.read();
    assertEquals( 2, stream.available() );
    stream.read();
    assertEquals( 1, stream.available() );
    stream.read();
    assertEquals( 0, stream.available() );
    assertEquals( 0, stream.available() );
    assertEquals( -1, stream.read() );
  }

  @Test
  public void testStreamsBuffered() throws Exception {
    InputStream stream = new BufferedInputStream( new ByteArrayInputStream( "asdf".getBytes() ) );
    assertEquals( 4, stream.available() );
    stream.read();
    assertEquals( 3, stream.available() );
    stream.read();
    assertEquals( 2, stream.available() );
    stream.read();
    assertEquals( 1, stream.available() );
    stream.read();
    assertEquals( 0, stream.available() );
    assertEquals( 0, stream.available() );
    assertEquals( -1, stream.read() );
  }

  private static class BlockingInputStream extends InputStream {
    private volatile boolean blocking = false;

    @Override
    public int available() throws IOException {
      return 1;
    }

    @Override
    public int read( byte[] b, int off, int len ) throws IOException {
      if ( !blocking ) {
        blocking = true;
        b[0] = 42;
        return 1;
      }

      try {
        Thread.sleep( 10000000 );
      } catch ( InterruptedException e ) {
      }

      return -1;
    }

    @Override
    public int read() throws IOException {
      throw new UnsupportedOperationException();
    }
  }
}
