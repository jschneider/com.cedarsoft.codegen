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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class MergingLookupTest {
  private MappedLookup lookup0;
  private MappedLookup lookup1;
  private MergingLookup mergingLookup;

  @Before
  public void setUp() throws Exception {
    lookup0 = new MappedLookup();
    lookup0.store( String.class, "0" );
    lookup0.store( Integer.class, 1 );

    lookup1 = new MappedLookup();
    lookup1.store( CharSequence.class, "1" );
    lookup1.store( Integer.class, 2 );

    mergingLookup = new MergingLookup( lookup0, lookup1 );
  }

  @Test
  public void testMerging() {
    assertEquals( "0", mergingLookup.lookup( String.class ) );
    assertEquals( "1", mergingLookup.lookup( CharSequence.class ) );
    assertEquals( 1, ( int ) mergingLookup.lookup( Integer.class ) );

    assertEquals( 3, mergingLookup.lookups().size() );
    assertTrue( mergingLookup.lookups().containsKey( String.class ) );
    assertTrue( mergingLookup.lookups().containsKey( CharSequence.class ) );
    assertTrue( mergingLookup.lookups().containsKey( Integer.class ) );
    assertEquals( new Integer( 1 ), mergingLookup.lookups().get( Integer.class ) );
  }

  @Test
  public void testListener() {
    final List<String> newValues = new ArrayList<String>();

    mergingLookup.addChangeListener( String.class, new LookupChangeListener<String>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends String> event ) {
        newValues.add( event.getNewValue() );
      }
    } );

    lookup0.store( String.class, "1" );
    assertEquals( 1, newValues.size() );
    assertEquals( "1", newValues.get( 0 ) );
    assertEquals( "1", mergingLookup.lookup( String.class ) );

    newValues.clear();
    lookup1.store( String.class, "2" );
    assertEquals( 0, newValues.size() );
    assertEquals( "1", mergingLookup.lookup( String.class ) );
    assertEquals( "1", lookup0.lookup( String.class ) );
    assertEquals( "2", lookup1.lookup( String.class ) );
  }
}
