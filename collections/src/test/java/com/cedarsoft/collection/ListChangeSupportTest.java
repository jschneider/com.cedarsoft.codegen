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

package com.cedarsoft.collection;

import javax.annotation.Nonnull;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: 24.08.2006<br>
 * Time: 18:51:14<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class ListChangeSupportTest {
  private List<String> backend;
  private ListChangeSupport<String> listChangeSupport;

  @Before
  public void setUp() throws Exception {
    backend = new ArrayList<String>();
    listChangeSupport = new ListChangeSupport<String>();
  }

  @Test
  public void testIt() {
    final List<Integer> eventIndicies = new ArrayList<Integer>();
    final List<String> eventStrings = new ArrayList<String>();

    listChangeSupport.addListener( new ListChangeListener<String>() {
      @Override
      public void elementAdded( int index, @Nonnull String element ) {
        eventIndicies.add( index );
        eventStrings.add( element );
      }

      @Override
      public void elementRemoved( int index, @Nonnull String element ) {
        eventIndicies.add( index );
        eventStrings.add( element );
      }
    } );

    assertEquals( 0, eventIndicies.size() );
    assertEquals( 0, eventStrings.size() );

    listChangeSupport.add( backend, "daString0" );

    assertEquals( 1, eventIndicies.size() );
    assertEquals( 0, ( int ) eventIndicies.get( 0 ) );

    assertEquals( 1, eventStrings.size() );
    assertEquals( "daString0", eventStrings.get( 0 ) );

    listChangeSupport.add( backend, "daString1" );

    assertEquals( 2, eventIndicies.size() );
    assertEquals( 1, ( int ) eventIndicies.get( 1 ) );

    assertEquals( 2, eventStrings.size() );
    assertEquals( "daString1", eventStrings.get( 1 ) );

    listChangeSupport.remove( backend, "daString0" );

    assertEquals( 3, eventIndicies.size() );
    assertEquals( 0, ( int ) eventIndicies.get( 2 ) );

    assertEquals( 3, eventStrings.size() );
    assertEquals( "daString0", eventStrings.get( 2 ) );
  }
}
