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

package com.cedarsoft.cmd;

import org.junit.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: Mar 7, 2007<br>
 * Time: 10:42:16 AM<br>
 */
public class DefaultCmdLineTest {
  private StringCmdLine cmdLine;

  @Before
  public void setUp() throws Exception {
    cmdLine = new StringCmdLine();
  }

  @Test
  public void testReadWIthSelectionAndPreselection() {
    cmdLine.addExpectedOut( "asdf" );
    cmdLine.addExpectedOut( "\t0\t-->0" );
    cmdLine.addExpectedOut( "\t1\t-->1" );

    cmdLine.addAnswer( "" );

    List<String> values = new ArrayList<String>();
    values.add( "-->0" );
    values.add( "-->1" );
    String value = cmdLine.read( "asdf", values, "other" );

    assertEquals( "other", value );
  }

  @Test
  public void testListOwn() {
    cmdLine.addAnswer( "d" );
    cmdLine.addExpectedOut( "message" );
    cmdLine.addExpectedOut( "\t0\ta" );
    cmdLine.addExpectedOut( "\t1\tb" );
    cmdLine.addExpectedOut( "\t2\tc" );

    assertEquals( "d", cmdLine.read( "message", Arrays.asList( "a", "b", "c" ) ) );
  }

  @Test
  public void testReadWithSelection() throws IOException {
    cmdLine.addExpectedOut( "asdf" );
    cmdLine.addExpectedOut( "\t0\t-->0" );
    cmdLine.addExpectedOut( "\t1\t-->1" );

    cmdLine.addAnswer( "daFreeValue" );

    List<String> values = new ArrayList<String>();
    values.add( "-->0" );
    values.add( "-->1" );
    String value = cmdLine.read( "asdf", values );

    assertEquals( "daFreeValue", value );
  }

  @Test
  public void testReadWithSelection2() throws IOException {
    cmdLine.addExpectedOut( "asdf" );
    cmdLine.addExpectedOut( "\t0\t-->0" );
    cmdLine.addExpectedOut( "\t1\t-->1" );

    cmdLine.addAnswer( "0" );

    List<String> values = new ArrayList<String>();
    values.add( "-->0" );
    values.add( "-->1" );

    assertEquals( "-->0", cmdLine.read( "asdf", values ) );
  }
}
