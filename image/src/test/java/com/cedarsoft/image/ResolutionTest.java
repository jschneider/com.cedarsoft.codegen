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

import static org.junit.Assert.*;

/**
 *
 */
public class ResolutionTest {
  @Test
  public void testIt() {
    Resolution resolution = new Resolution( 1024, 768 );
    assertEquals( 1024, resolution.getWidth() );
    assertEquals( 768, resolution.getHeight() );
    assertEquals( AspectRatio.AR_4_3, resolution.getAspectRatio() );
  }

  @Test
  public void testAspectRatio() {
    assertEquals( AspectRatio.AR_1_1.getRatio(), 1.0 / 1.0, 0 );
    assertEquals( AspectRatio.AR_3_2.getRatio(), 3.0 / 2.0, 0 );
    assertEquals( AspectRatio.AR_4_3.getRatio(), 4.0 / 3.0, 0 );
    assertEquals( AspectRatio.AR_5_3.getRatio(), 5.0 / 3.0, 0 );
    assertEquals( AspectRatio.AR_5_4.getRatio(), 5.0 / 4.0, 0 );
    assertEquals( AspectRatio.AR_16_9.getRatio(), 16.0 / 9.0, 0 );
    assertEquals( AspectRatio.AR_16_10.getRatio(), 16.0 / 10.0, 0 );
  }

  @Test
  public void testSelect() {
    assertEquals( AspectRatio.AR_4_3, AspectRatio.get( 4, 3 ) );
    assertEquals( AspectRatio.AR_4_3, AspectRatio.get( 400, 300 ) );
    assertEquals( AspectRatio.AR_4_3, AspectRatio.get( 1024, 768 ) );
    assertEquals( AspectRatio.AR_5_4, AspectRatio.get( 1280, 1024 ) );

    assertEquals( 1, AspectRatio.get( 1, 17 ).getWidthFactor(), 0 );
    assertEquals( 17, AspectRatio.get( 1, 17 ).getHeightFactor(), 0 );
  }

  @Test
  public void testNikon() {
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 4256, 2832 ) ); //D3
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 6048, 4032 ) ); //D3X
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 4256, 2832 ) ); //D3s
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 4256, 2832 ) ); //D700
  }

  @Test
  public void testCanonRes() {
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 3888, 2592 ) ); //1000D

    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 3072, 2048 ) ); //300D
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 3456, 2304 ) ); //350D
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 3888, 2592 ) ); //400D
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 4272, 2848 ) ); //450D
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 4752, 3168 ) ); //500D
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 5184, 3456 ) ); //550D

    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 4752, 3168 ) ); //50D
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 3888, 2592 ) ); //40D
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 3504, 2336 ) ); //30D

    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 5184, 3456 ) ); //7D
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 4368, 2912 ) ); //5D
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 5616, 3744 ) ); //5D2

    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 3888, 2592 ) ); //1D3
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 4896, 3264 ) ); //1D4

    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 2464, 1648 ) ); //1D
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 4064, 2704 ) ); //1Ds
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 4992, 3328 ) ); //1Ds2
    assertEquals( AspectRatio.AR_3_2, AspectRatio.get( 5616, 3744 ) ); //1Ds3
  }

  @Test
  public void testComparable() {
    assertTrue( new Resolution( 100, 100 ).equals( new Resolution( 100, 100 ) ) );

    assertTrue( new Resolution( 100, 100 ).compareTo( new Resolution( 100, 100 ) ) == 0 );

    assertTrue( new Resolution( 100, 100 ).compareTo( new Resolution( 100, 101 ) ) < 0 );
    assertTrue( new Resolution( 100, 100 ).compareTo( new Resolution( 101, 101 ) ) < 0 );
    assertTrue( new Resolution( 100, 100 ).compareTo( new Resolution( 101, 100 ) ) < 0 );

    assertTrue( new Resolution( 100, 100 ).compareTo( new Resolution( 100, 99 ) ) > 0 );
    assertTrue( new Resolution( 100, 100 ).compareTo( new Resolution( 99, 99 ) ) > 0 );
    assertTrue( new Resolution( 100, 100 ).compareTo( new Resolution( 99, 100 ) ) > 0 );

    assertTrue( new Resolution( 100, 100 ).compareTo( new Resolution( 101, 99 ) ) < 0 );
    assertTrue( new Resolution( 100, 100 ).compareTo( new Resolution( 99, 101 ) ) > 0 );
  }
}
