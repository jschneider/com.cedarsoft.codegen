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

import static org.junit.Assert.*;

/**
 * Stores the parent of children in a weak map
 */
public class ParentCacheTest {
  private ParentCache cache;

  @Before
  public void setUp() throws Exception {
    cache = new ParentCache();
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testIndex() {
    cache.storeIndex( "c", 1 );
    assertEquals( new Integer( 1 ), cache.findIndex( "c" ) );

    cache.storeIndex( "c", 1 );
    cache.storeIndex( "c", 2 );
    assertEquals( new Integer( 2 ), cache.findIndex( "c" ) );

    cache.remove( "c" );
    assertNull( cache.findIndex( "c" ) );
    cache.storeIndex( "c", 3 );
    assertEquals( new Integer( 3 ), cache.findIndex( "c" ) );
  }

  @Test
  public void testRemove() {
    String child = "child";
    cache.store( child, "parent", 0 );
    assertSame( "parent", cache.findParent( child ) );

    cache.remove( child );
    assertNull( cache.findParent( child ) );
  }

  @Test
  public void testOverwrite() {
    String child = "child";
    cache.store( child, "parent", 0 );
    cache.store( child, "parent", 0 );
    try {
      cache.store( child, "parent2", 0 );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }
  }

  @Test
  public void testSimple() {
    String child = "asdf";
    assertNull( cache.findParent( child ) );
    String parent = "parent";
    cache.store( child, parent, 0 );
    assertSame( parent, cache.findParent( child ) );
  }

  @Test
  public void testWeak() {
    Object child = new Object();
    assertNull( cache.findParent( child ) );
    fill( child );
    assertNotNull( cache.findParent( child ) );
    System.gc();
    assertNull( cache.findParent( child ) );
  }

  private void fill( @Nonnull Object child ) {
    cache.store( child, new Object(), 0 );
  }
}
