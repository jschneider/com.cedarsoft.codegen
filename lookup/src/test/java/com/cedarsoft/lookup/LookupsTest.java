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
 * Tests all availabe types of lookups
 */
public class LookupsTest {
  private static final String VALUE = "asdf";
  private List<Lookup> lookups;

  @Before
  public void setUp() throws Exception {
    lookups = new ArrayList<Lookup>();
    lookups.add( new SingletonLookup<String>( String.class, VALUE ) );
    lookups.add( new DynamicLookup( VALUE ) );
    lookups.add( new InstantiatorLookup<String>( new Instantiater.Typed<String>() {
      @Override
      @Nonnull
      public Class<? extends String> getType() {
        return String.class;
      }

      @Override
      @Nonnull
      public String createInstance() throws InstantiationFailedException {
        return VALUE;
      }
    } ) );

    lookups.add( new LookupWrapper( new DynamicLookup( VALUE ) ) );
    {
      MappedLookup lookup = new MappedLookup();
      lookup.store( String.class, VALUE );
      lookups.add( lookup );
    }

    {
      MockLookup lookup = new MockLookup();
      lookup.store( String.class, VALUE );
      lookups.add( lookup );
    }
  }

  @Test
  public void testResolve() {
    for ( Lookup lookup : lookups ) {
      assertFalse( lookup.lookups().isEmpty() );
      assertEquals( VALUE, lookup.lookups().get( String.class ) );
      assertEquals( VALUE, lookup.lookup( String.class ) );
    }
  }

  @Test
  public void testBind() {
    for ( Lookup lookup : lookups ) {
      final String[] called = new String[1];
      lookup.bind( String.class, new LookupChangeListener<String>() {
        @Override
        public void lookupChanged( @Nonnull LookupChangeEvent<? extends String> event ) {
          called[0] = event.getNewValue();
        }
      } );
      assertEquals( VALUE, called[0] );
    }
  }

  @Test
  public void testBind2() {
    for ( Lookup lookup : lookups ) {
      final String[] called = new String[1];
      lookup.bind( new TypedLookupChangeListener<String>() {
        @Override
        public void lookupChanged( @Nonnull LookupChangeEvent<? extends String> event ) {
          called[0] = event.getNewValue();
        }

        @Override
        @Nonnull
        public Class<String> getType() {
          return String.class;
        }
      } );
      assertEquals( VALUE, called[0] );
    }
  }

  @Test
  public void testBindWeak() {
    for ( Lookup lookup : lookups ) {
      final String[] called = new String[1];
      lookup.bindWeak( String.class, new LookupChangeListener<String>() {
        @Override
        public void lookupChanged( @Nonnull LookupChangeEvent<? extends String> event ) {
          called[0] = event.getNewValue();
        }
      } );
      assertEquals( "Failed at " + lookup.getClass().getName(), VALUE, called[0] );
    }
  }

  @Test
  public void testBindWeak2() {
    for ( Lookup lookup : lookups ) {
      final String[] called = new String[1];
      lookup.bindWeak( new TypedLookupChangeListener<String>() {
        @Override
        public void lookupChanged( @Nonnull LookupChangeEvent<? extends String> event ) {
          called[0] = event.getNewValue();
        }

        @Override
        @Nonnull
        public Class<String> getType() {
          return String.class;
        }
      } );
      assertEquals( "Failed at " + lookup.getClass().getName(), VALUE, called[0] );
    }
  }
}
