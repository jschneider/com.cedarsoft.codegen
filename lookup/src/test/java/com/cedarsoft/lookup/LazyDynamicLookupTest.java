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

package com.cedarsoft.lookup;

import javax.annotation.Nonnull;
import org.junit.*;

import java.io.Serializable;
import java.util.Map;

import static org.junit.Assert.*;

/**
 *
 */
public class LazyDynamicLookupTest {
  private LazyLookup<String> lookup;

  private boolean called;

  @Before
  public void setUp() throws Exception {
    called = false;
    lookup = new LazyLookup<String>() {
      @Override
      public Class<? extends String> getType() {
        return String.class;
      }

      @Override
      @Nonnull
      protected String createInstance() {
        if ( called ) {
          throw new IllegalStateException( "has still been called" );
        }
        called = true;
        return "asdf";
      }
    };
  }

  @Test
  public void testLazy() {
    assertFalse( called );
    Map<Class<?>, Object> lookups = lookup.lookups();
    assertTrue( lookups.keySet().contains( String.class ) );
    assertTrue( lookups.keySet().contains( Serializable.class ) );
    assertTrue( lookups.keySet().contains( Comparable.class ) );
    assertTrue( lookups.keySet().contains( CharSequence.class ) );
    assertEquals( 4, lookups.size() );

    assertFalse( called );
    assertEquals( "asdf", lookup.getValue() );
    assertTrue( called );
    assertEquals( "asdf", lookup.lookup( String.class ) );
  }

  @Test
  public void testFail() {
    Map.Entry<Class<?>, Object> entry = lookup.lookups().entrySet().iterator().next();
    assertFalse( called );
    assertEquals( "asdf", entry.getValue() );
    assertTrue( called );

    try {
      entry.setValue( "asf" );
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
    }
  }
}
