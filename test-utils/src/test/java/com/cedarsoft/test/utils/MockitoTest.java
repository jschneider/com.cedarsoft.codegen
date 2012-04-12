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

package com.cedarsoft.test.utils;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.junit.*;

import java.lang.Exception;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

/**
 * <p>MockitoTest class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class MockitoTest {
  /**
   * <p>testTemplate</p>
   *
   * @throws Exception if any.
   */
  @Test
  public void testTemplate() throws Exception {
    new MockitoTemplate() {
      @Mock
      private List<String> list;

      @Override
      protected void stub() throws Exception {
        when( list.size() ).thenReturn( 1 ).thenReturn( 2 );
        doThrow( new RuntimeException() ).when( list ).clear();
      }

      @Override
      protected void execute() throws Exception {
        list.add( "asdf" );
        list.add( "other" );

        assertEquals( 1, list.size() );
        assertEquals( 2, list.size() );

        try {
          list.clear();
          fail( "Where is the Exception" );
        } catch ( RuntimeException ignore ) {
        }
      }

      @Override
      protected void verifyMocks() throws Exception {
        verify( list, times( 2 ) ).size();

        verify( list ).add( "asdf" );
        verify( list ).add( "other" );
        verify( list ).clear();

        verify( list, never() ).iterator();
      }
    }.run();
  }

  /**
   * <p>testIt</p>
   */
  @Test
  public void testIt() {
    List<String> list = mock( List.class );

    when( list.size() ).thenReturn( 1 ).thenReturn( 2 );
    doThrow( new RuntimeException() ).when( list ).clear();

    list.add( "asdf" );
    list.add( "other" );

    assertEquals( 1, list.size() );
    assertEquals( 2, list.size() );

    try {
      list.clear();
      fail( "Where is the Exception" );
    } catch ( RuntimeException ignore ) {
    }

    verify( list, times( 2 ) ).size();

    verify( list ).add( "asdf" );
    verify( list ).add( "other" );
    verify( list ).clear();

    verify( list, never() ).iterator();
    verifyNoMoreInteractions( list );

    //    Mockito.verify( list ).add( "asdf" );
    //    Mockito.verify( list ).add( "other" );
  }


  @Mock
  private List<String> mock;

  /**
   * <p>testAnnotations</p>
   */
  @Test
  public void testAnnotations() {
    MockitoAnnotations.initMocks( this );

    assertNotNull( mock );
    mock.add( "asdf" );

    verify( mock ).add( "asdf" );
  }
}
