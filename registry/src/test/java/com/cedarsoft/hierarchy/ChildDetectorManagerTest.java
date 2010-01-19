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

package com.cedarsoft.hierarchy;

import org.jetbrains.annotations.NotNull;
import org.testng.annotations.*;

import javax.swing.JFrame;
import java.util.Collections;
import java.util.List;

import static org.testng.Assert.*;

/**
 * <p/>
 * Date: Apr 23, 2007<br>
 * Time: 1:06:08 PM<br>
 */
public class ChildDetectorManagerTest {
  private ChildDetectorManager manager;

  @BeforeMethod
  protected void setUp() throws Exception {
    manager = new ChildDetectorManager();
    manager.addChildDetector( String.class, new AbstractChildDetector<String, String>() {
      @Override
      @NotNull
      public List<? extends String> findChildren( @NotNull String parent ) {
        return Collections.emptyList();
      }
    } );
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testIt() {
    assertNotNull( manager.getChildDetector( String.class ) );
    try {
      manager.getChildDetector( JFrame.class );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }
  }
}
