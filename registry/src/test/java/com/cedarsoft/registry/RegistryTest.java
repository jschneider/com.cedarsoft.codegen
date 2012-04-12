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

package com.cedarsoft.registry;

import com.cedarsoft.exceptions.StillContainedException;
import com.cedarsoft.test.utils.MockitoTemplate;
import org.easymock.classextension.EasyMock;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Comparator;

import static org.junit.Assert.*;

/**
 *
 */
public class RegistryTest {
  private Registry<String> registry;

  @Before
  public void setUp() throws Exception {
    registry = new DefaultRegistry<String>();
  }

  @Test
  public void testIt() {
    assertTrue( registry.getStoredObjects().isEmpty() );
    registry.store( "asdf" );
    assertFalse( registry.getStoredObjects().isEmpty() );
    assertEquals( 1, registry.getStoredObjects().size() );
  }

  @Test
  public void addExisting() {
    assertTrue( registry.getStoredObjects().isEmpty() );
    registry.store( "asdf" );
    assertEquals( 1, registry.getStoredObjects().size() );
    registry.store( "asdf" );
    assertEquals( 2, registry.getStoredObjects().size() );

    registry = new DefaultRegistry<String>( new Comparator<String>() {
      @Override
      public int compare( String o1, String o2 ) {
        return o1.compareTo( o2 );
      }
    } );

    registry.store( "asdf" );
    assertEquals( 1, registry.getStoredObjects().size() );
    try {
      registry.store( "asdf" );
      fail( "Where is the Exception" );
    } catch ( StillContainedException ignore ) {
    }
    assertEquals( 1, registry.getStoredObjects().size() );
  }

  @Test
  public void testInitiWithComp() {
    try {
      new DefaultRegistry<String>( Arrays.asList( "a", "b", "a" ), new Comparator<String>() {
        @Override
        public int compare( String o1, String o2 ) {
          return o1.compareTo( o2 );
        }
      } );
      fail( "Where is the Exception" );
    } catch ( StillContainedException ignore ) {
    }
  }

  @Test
  public void testListener() throws Exception {
    new MockitoTemplate() {
      @Mock
      private DefaultRegistry.Listener<String> listener;

      @Override
      protected void stub() throws Exception {
      }

      @Override
      protected void execute() throws Exception {
        registry.addListener( listener );
        registry.store( "asdf" );
      }

      @Override
      protected void verifyMocks() throws Exception {
        Mockito.verify( listener ).objectStored( "asdf" );
        Mockito.verifyNoMoreInteractions( listener );
      }
    }.run();
  }
}
