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

import static org.junit.Assert.*;

/**
 *
 */
public class InstantiatorLookupTest {
  private InstantiatorLookup<String> lookup;
  private boolean called;

  @Before
  public void setUp() throws Exception {
    Instantiater<String> instantiater = new Instantiater<String>() {
      @Override
      @Nonnull
      public String createInstance() throws InstantiationFailedException {
        if ( called ) {
          throw new IllegalStateException();
        }
        called = true;
        return "asdf";
      }
    };
    lookup = new InstantiatorLookup<String>( String.class, instantiater );
  }

  @Test
  public void testIt() {
    assertFalse( called );
    assertEquals( 4, lookup.lookups().size() );
    assertFalse( called );
    assertNull( lookup.lookup( Integer.class ) );
    assertFalse( called );
    assertEquals( "asdf", lookup.lookup( String.class ) );
    assertTrue( called );
    assertEquals( "asdf", lookup.lookup( String.class ) );
    assertTrue( called );
  }
}
