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

package com.cedarsoft.version;

import org.junit.*;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 *
 */
public class VersionRangeTest {
  @Test
  public void testConstructor() {
    VersionRange.from( 1, 0, 0 ).to( 2, 0, 0 );
    VersionRange.from( 1, 0, 0 ).to( 1, 0, 0 );

    try {
      VersionRange.from( 1, 0, 1 ).to( 1, 0, 0 );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }
  }

  @Test
  public void testFormat() {
    assertThat( VersionRange.single( 1, 0, 0 ).format(), is( "[1.0.0]" ) );
    assertThat( VersionRange.from( 1, 0, 0 ).to( 1, 0, 0 ).format(), is( "[1.0.0]" ) );
    assertThat( VersionRange.from( 1, 0, 0 ).to( 2, 0, 0 ).format(), is( "[1.0.0-2.0.0]" ) );
  }

  @Test
  public void testSingle() {
    assertThat( VersionRange.single( 1, 0, 0 ).getMax(), is( Version.valueOf( 1, 0, 0 ) ) );
    assertThat( VersionRange.single( 1, 1, 0 ).getMax(), is( Version.valueOf( 1, 1, 0 ) ) );
    assertThat( VersionRange.single( Version.valueOf( 1, 0, 0 ) ).getMax(), is( Version.valueOf( 1, 0, 0 ) ) );
    assertThat( VersionRange.single( Version.valueOf( 1, 1, 0 ) ).getMax(), is( Version.valueOf( 1, 1, 0 ) ) );
  }

  @Test
  public void testFluent() {
    assertEquals( VersionRange.from( 1, 0, 0 ).to( 2, 0, 0 ), new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ) ) );
  }

  @Test
  public void testMinMax() {
    {
      VersionRange range = new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ) );
      assertEquals( range.getMin(), new Version( 1, 0, 0 ) );
      assertEquals( range.getMax(), new Version( 2, 0, 0 ) );
    }
    {
      VersionRange range = new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), false, false );
      assertEquals( range.getMin(), new Version( 1, 0, 0 ) );
      assertEquals( range.getMax(), new Version( 2, 0, 0 ) );
    }
  }

  @Test
  public void testOverlap() {
    VersionRange range = new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ) );

    assertTrue( range.overlaps( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ) ) ) );
    assertTrue( range.overlaps( new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ) ) );
    assertTrue( range.overlaps( new VersionRange( new Version( 2, 0, 0 ), new Version( 2, 0, 0 ) ) ) );

    assertFalse( range.overlaps( new VersionRange( new Version( 0, 0, 0 ), new Version( 0, 99, 99 ) ) ) );
    assertFalse( range.overlaps( new VersionRange( new Version( 0, 0, 0 ), new Version( 1, 0, 0 ), true, false ) ) );
    assertTrue( range.overlaps( new VersionRange( new Version( 0, 0, 0 ), new Version( 1, 0, 0 ), true, true ) ) );

    assertFalse( range.overlaps( new VersionRange( new Version( 2, 0, 1 ), new Version( 3, 0, 0 ) ) ) );
    assertFalse( range.overlaps( new VersionRange( new Version( 2, 0, 0 ), new Version( 3, 0, 0 ), false, true ) ) );
    assertTrue( range.overlaps( new VersionRange( new Version( 2, 0, 0 ), new Version( 3, 0, 0 ), true, true ) ) );
  }

  @Test
  public void testContains() {
    assertTrue( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ) ).containsCompletely( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ) ) ) );
    assertTrue( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ) ).containsCompletely( new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 0, 0 ) ) ) );
    assertTrue( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ) ).containsCompletely( new VersionRange( new Version( 2, 0, 0 ), new Version( 2, 0, 0 ) ) ) );

    assertFalse( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), false, true ).containsCompletely( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ) ) ) );
    assertFalse( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), true, false ).containsCompletely( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ) ) ) );

    assertTrue( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), true, true ).containsCompletely( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), false, false ) ) );
    assertTrue( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), true, true ).containsCompletely( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), true, false ) ) );
    assertTrue( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), true, true ).containsCompletely( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), false, true ) ) );
    assertTrue( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), true, true ).containsCompletely( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), true, true ) ) );

    assertFalse( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), false, true ).containsCompletely( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), true, true ) ) );
    assertFalse( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), false, true ).containsCompletely( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), true, false ) ) );
    assertFalse( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), true, false ).containsCompletely( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), true, true ) ) );
    assertFalse( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), true, false ).containsCompletely( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), false, true ) ) );


    assertFalse( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), false, false ).containsCompletely( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), true, true ) ) );
  }

  @Test
  public void testExclude() {
    {
      VersionRange range = new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), true, true );
      assertTrue( range.contains( new Version( 1, 0, 0 ) ) );
      assertTrue( range.contains( new Version( 1, 0, 1 ) ) );
      assertTrue( range.contains( new Version( 1, 99, 99 ) ) );
      assertTrue( range.contains( new Version( 2, 0, 0 ) ) );
      assertEquals( "[1.0.0-2.0.0]", range.toString() );
    }

    {
      VersionRange range = new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), true, false );
      assertTrue( range.contains( new Version( 1, 0, 0 ) ) );
      assertTrue( range.contains( new Version( 1, 0, 1 ) ) );
      assertTrue( range.contains( new Version( 1, 99, 99 ) ) );
      assertFalse( range.contains( new Version( 2, 0, 0 ) ) );
      assertEquals( "[1.0.0-2.0.0[", range.toString() );
    }

    {
      VersionRange range = new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), false, true );
      assertFalse( range.contains( new Version( 1, 0, 0 ) ) );
      assertTrue( range.contains( new Version( 1, 0, 1 ) ) );
      assertTrue( range.contains( new Version( 1, 99, 99 ) ) );
      assertTrue( range.contains( new Version( 2, 0, 0 ) ) );
      assertEquals( "]1.0.0-2.0.0]", range.toString() );
    }

    {
      VersionRange range = new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 0, 0 ), false, false );
      assertFalse( range.contains( new Version( 1, 0, 0 ) ) );
      assertTrue( range.contains( new Version( 1, 0, 1 ) ) );
      assertTrue( range.contains( new Version( 1, 99, 99 ) ) );
      assertFalse( range.contains( new Version( 2, 0, 0 ) ) );
      assertEquals( "]1.0.0-2.0.0[", range.toString() );
    }
  }

  @Test
  public void testIt() {
    VersionRange range = new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 1, 90 ) );

    assertTrue( range.contains( new Version( 1, 0, 0 ) ) );
    assertTrue( range.contains( new Version( 1, 1, 0 ) ) );
    assertTrue( range.contains( new Version( 1, 1, 89 ) ) );
    assertTrue( range.contains( new Version( 1, 1, 90 ) ) );

    assertFalse( range.contains( new Version( 1, 1, 91 ) ) );
    assertFalse( range.contains( new Version( 1, 2, 0 ) ) );
    assertFalse( range.contains( new Version( 0, 99, 99 ) ) );
  }

  @Test
  public void testEquals() {
    assertEquals( new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 1, 90 ) ), new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 1, 90 ) ) );
    assertEquals( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 1, 90 ) ), new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 1, 90 ) ) );

    assertFalse( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 1, 90 ) ).equals( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 1, 91 ) ) ) );
    assertFalse( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 1, 90 ) ).equals( new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 1, 90 ) ) ) );
    assertFalse( new VersionRange( new Version( 1, 0, 1 ), new Version( 2, 1, 90 ) ).equals( new VersionRange( new Version( 1, 0, 0 ), new Version( 2, 1, 90 ) ) ) );
  }

  @Test
  public void testToString() {
    assertEquals( "[1.0.0-1.1.90]", new VersionRange( new Version( 1, 0, 0 ), new Version( 1, 1, 90 ) ).toString() );
  }
}
