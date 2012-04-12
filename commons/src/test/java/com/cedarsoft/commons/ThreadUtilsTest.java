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

import org.junit.*;
import org.junit.rules.*;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 *
 */
public class ThreadUtilsTest {
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void testIt() {
    ThreadUtils.assertNotEventDispatchThread();

    expectedException.expect( IllegalThreadStateException.class );
    expectedException.expectMessage( "Is EDT" );

    ThreadUtils.invokeInEventDispatchThread( new Runnable() {
      @Override
      public void run() {
        ThreadUtils.assertNotEventDispatchThread();
      }
    } );
  }

  @Test
  public void testEdt() {
    expectedException.expect( IllegalThreadStateException.class );
    expectedException.expectMessage( "Not in EDT" );

    ThreadUtils.assertEventDispatchThread();
  }

  @Test
  public void testInvoke() {
    final boolean[] called = {false};

    ThreadUtils.invokeInEventDispatchThread( new Runnable() {
      @Override
      public void run() {
        called[0] = true;
        assertTrue( ThreadUtils.isEventDispatchThread() );
        ThreadUtils.assertEventDispatchThread();
      }
    } );

    ThreadUtils.waitForEventDispatchThread();
    assertTrue( called[0] );
  }

  @Test
  public void testOther() throws ExecutionException, InterruptedException {
    final boolean[] called = {false};

    assertEquals( "asdf", ThreadUtils.inokeInOtherThread( new Callable<Object>() {
      @Override
      public Object call() throws Exception {
        called[0] = true;
        assertFalse( ThreadUtils.isEventDispatchThread() );
        ThreadUtils.assertNotEventDispatchThread();
        return "asdf";
      }
    } ) );

    assertTrue( called[0] );
  }
}
