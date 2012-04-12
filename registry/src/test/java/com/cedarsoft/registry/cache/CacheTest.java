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

package com.cedarsoft.registry.cache;

import javax.annotation.Nonnull;
import org.junit.*;
import org.junit.rules.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static org.junit.Assert.*;

/**
 *
 */
public class CacheTest {
  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  private HashedCache<Integer, String> cache;
  private int counter;

  @Before
  public void setUp() throws Exception {
    cache = new HashedCache<Integer, String>( new Cache.Factory<Integer, String>() {
      @Override
      @Nonnull
      public String create( @Nonnull Integer key ) {
        counter++;
        return String.valueOf( key );
      }
    } );
    counter = 0;
  }

  @Test
  public void testRemove() {
    assertEquals( "3", cache.get( 3 ) );
    assertEquals( 1, cache.entrySet().size() );
    Iterator<Map.Entry<Integer, String>> iterator = cache.entrySet().iterator();
    Assert.assertTrue( iterator.hasNext() );
    iterator.next();
    iterator.remove();
    assertEquals( 0, cache.entrySet().size() );
  }

  @Test
  public void testPut() {
    expectedException.expect( UnsupportedOperationException.class );
    cache.put( 4, "as" );
  }

  @Test
  public void testPutAll() {
    expectedException.expect( UnsupportedOperationException.class );
    cache.putAll( new HashMap<Integer, String>() );
  }

  @Test
  public void testClear() {
    expectedException.expect( UnsupportedOperationException.class );
    cache.clear();
  }

  @Test
  public void testIt() {
    assertEquals( 0, counter );
    assertEquals( 0, cache.size() );
    assertTrue( cache.isEmpty() );
    assertEquals( "5", cache.get( 5 ) );
    assertEquals( "4", cache.get( 4 ) );
    assertEquals( "4", cache.get( 4 ) );

    assertFalse( cache.isEmpty() );
    assertTrue( cache.containsKey( 4 ) );
    assertTrue( cache.containsValue( "4" ) );

    assertEquals( 2, cache.size() );
    assertEquals( 2, counter );

    assertEquals( 2, cache.values().size() );
  }
}
