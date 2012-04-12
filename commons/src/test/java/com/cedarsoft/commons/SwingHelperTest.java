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

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.lang.reflect.InvocationTargetException;

import static org.junit.Assert.*;

/**
 *
 */
public class SwingHelperTest {
  @Test
  public void testThreads() {
    SwingHelper.assertNotEventThread();

    SwingHelper.invokeAndWait( new Runnable() {
      @Override
      public void run() {
        SwingHelper.assertEventThread();
        assertTrue( SwingHelper.isEventDispatchThread() );
        SwingHelper.waitForSwingThread();
      }
    } );

    assertFalse( SwingHelper.isEventDispatchThread() );
  }

  @Test
  public void testEarly() {
    final boolean[] called = {false};

    SwingHelper.early( new Runnable() {
      @Override
      public void run() {
        SwingHelper.early( new Runnable() {
          @Override
          public void run() {
            called[0] = true;
          }
        } );
      }
    } );

    SwingHelper.waitForSwingThread();
    assertTrue( called[0] );
  }

  @Test
  public void testPane() throws InvocationTargetException, InterruptedException {
    final JPanel panel = new JPanel();
    final JButton button = new JButton( "asdf" );
    panel.add( button );

    final JFrame frame = SwingHelper.showFrame( panel );

    final boolean[] called = {false};

    SwingHelper.early( new Runnable() {
      @Override
      public void run() {
        assertTrue( frame.isVisible() );

        assertSame( frame, SwingHelper.rootFrame( panel ) );
        assertSame( frame, SwingHelper.rootFrame( button ) );

        frame.dispose();
        assertFalse( frame.isVisible() );
        called[0] = true;
      }
    } );

    SwingHelper.waitForSwingThread();
    assertTrue( called[0] );
  }
}
