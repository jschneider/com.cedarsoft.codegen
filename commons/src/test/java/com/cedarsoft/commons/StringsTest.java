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

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: May 29, 2007<br>
 * Time: 2:12:13 PM<br>
 */
public class StringsTest {
  @Test
  public void testCut() {
    assertEquals( "asd", Strings.cut( "asdf", 3 ) );
    assertEquals( "asdf", Strings.cut( "asdf", 4 ) );
    assertEquals( "asdf", Strings.cut( "asdf", 45 ) );
    assertEquals( "", Strings.cut( "asdf", 0 ) );

    assertNull( Strings.cutNull( null, 3 ) );
  }

  @Test
  public void testMaxLengt() {
    assertEquals( "", Strings.cut( "asdf", 0 ) );
    assertEquals( "a", Strings.cut( "asdf", 1 ) );
    assertEquals( "as", Strings.cut( "asdf", 2 ) );
    assertEquals( "asd", Strings.cut( "asdf", 3 ) );
    assertEquals( "asdf", Strings.cut( "asdf", 4 ) );
    assertEquals( "asdf", Strings.cut( "asdf", 5 ) );
    assertEquals( "asdf", Strings.cut( "asdf", 995 ) );
  }

  @Test
  public void testQuote() {
    assertEquals( "", Strings.stripQuotes( "" ) );
    assertEquals( "a", Strings.stripQuotes( "a" ) );
    assertEquals( "a", Strings.stripQuotes( "\"a" ) );
    assertEquals( "a", Strings.stripQuotes( "\"a\"" ) );
    assertEquals( "a", Strings.stripQuotes( "a\"" ) );
  }
}
