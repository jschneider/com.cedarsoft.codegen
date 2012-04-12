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

package com.cedarsoft.image;

import org.junit.*;
import org.junit.rules.*;

import java.awt.Dimension;
import java.util.Arrays;
import java.util.List;

/**
 *
 */
public class ResolutionsTest {
  private Resolutions resolutions;

  @Before
  public void setUp() throws Exception {
    resolutions = new Resolutions();
  }

  @Test
  public void testScreenResolutions() {
    Assert.assertEquals( 25, resolutions.getScreenResolutions().size() );

    for ( Resolution resolution : resolutions.getScreenResolutions() ) {
      Assert.assertTrue( resolution.toString(), resolution.getWidth() > 0 );
      Assert.assertTrue( resolution.toString(), resolution.getHeight() > 0 );
    }
  }

  @Test
  public void testSorting() {
    Resolution first = resolutions.getScreenResolutions().get( 0 );
    Assert.assertTrue( first.compareTo( resolutions.getScreenResolutions().get( 1 ) ) > 0 );

    List<? extends Resolution> screenResolutions = resolutions.getScreenResolutions();
    for ( int i = 0; i < screenResolutions.size(); i++ ) {
      Resolution resolution = screenResolutions.get( i );

      for ( int j = i + 1; j < screenResolutions.size(); j++ ) {
        Assert.assertTrue( resolution.compareTo( screenResolutions.get( j ) ) > 0 );
      }
    }
  }

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void testNone() {
    try {
      Assert.assertEquals( resolutions.getBest( -10, -10 ), new Dimension( 160, 160 ) );
      Assert.fail( "Where is the Exception" );
    } catch ( IllegalArgumentException ignore ) {
    }

    expectedException.expect( IllegalArgumentException.class );
    expectedException.expectMessage( "No resolution found for 10/10" );
    Assert.assertEquals( resolutions.getBest( 10, 10 ), new Dimension( 160, 160 ) );
  }

  @Test
  public void testSizes() {
    Assert.assertEquals( Resolutions._160_160, resolutions.getBest( 160, 160 ) );
    Assert.assertEquals( Resolutions._160_160, resolutions.getBest( 161, 161 ) );
    Assert.assertEquals( Resolutions._4096_2304, resolutions.getBest( Integer.MAX_VALUE, Integer.MAX_VALUE ) );
  }

  @Test
  public void testConstructor() {
    Resolution small = new Resolution( 100, 100 );
    resolutions = new Resolutions( Arrays.asList( small ) );

    Assert.assertEquals( resolutions.getBest( 160, 160 ), small );
    Assert.assertEquals( resolutions.getBest( 161, 161 ), small );
    Assert.assertEquals( resolutions.getBest( Integer.MAX_VALUE, Integer.MAX_VALUE ), small );
  }
}
