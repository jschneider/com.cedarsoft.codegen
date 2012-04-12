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

import org.junit.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: 06.10.2006<br>
 * Time: 16:48:38<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class LookupTest {
  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testSimpleLookup() {
    Lookup lookup = Lookups.createLookup( "a", 5 );
    assertEquals( 2, lookup.lookups().size() );
    assertTrue( lookup.lookups().keySet().contains( String.class ) );
    assertTrue( lookup.lookups().keySet().contains( Integer.class ) );
  }

  @Test
  public void testException() {
    Lookup lookup = Lookups.createLookup( "a", 5 );

    assertEquals( "a", lookup.lookupNonNull( String.class ) );

    try {
      lookup.lookupNonNull( List.class );
      fail( "Where is the Exception" );
    } catch ( IllegalArgumentException ignore ) {
    }
  }

  @Test
  public void testLookup() {
    checkLookupStore( new MappedLookup() );
  }

  void checkLookupStore( LookupStore lookup ) {
    assertNull( lookup.lookup( String.class ) );
    lookup.store( String.class, "asdf" );
    assertEquals( "asdf", lookup.lookup( String.class ) );
  }

  @Test
  public void testLookups() {
    MappedLookup lookup = new MappedLookup();
    assertTrue( lookup.lookups().isEmpty() );

    lookup.store( String.class, "asdf" );
    assertEquals( 1, lookup.lookups().size() );
    Map.Entry<Class<?>, Object> entry = lookup.lookups().entrySet().iterator().next();
    assertSame( String.class, entry.getKey() );
    assertEquals( "asdf", entry.getValue() );
  }

  @Test
  public void testLookupsFromList() {
    List<Object> objects = new ArrayList<Object>();
    assertEquals( 0, Lookups.dynamicLookupFromList( objects ).lookups().size() );

    objects.add( "asdf" );
    assertEquals( "asdf", Lookups.dynamicLookupFromList( objects ).lookup( String.class ) );
  }
}
