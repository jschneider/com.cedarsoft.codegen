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

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Contains common dimensions for different screens
 */
public class Resolutions {
  @Nonnull
  public static final Resolution A4_300 = new Resolution( 2480, 3508 );
  @Nonnull
  public static final Resolution A4_72 = new Resolution( 595, 842 );

  @Nonnull
  private final List<Resolution> screenResolutions = new ArrayList<Resolution>();

  @Nonnull
  public static final Resolution FOUR_K = new Resolution( 4096, 2304 );
  @Nonnull
  public static final Resolution _4096_2304 = FOUR_K;
  @Nonnull
  public static final Resolution QUXGA = new Resolution( 3200, 2400 );
  @Nonnull
  public static final Resolution _3200_2400 = QUXGA;
  @Nonnull
  public static final Resolution WQXGA = new Resolution( 2560, 1600 );
  @Nonnull
  public static final Resolution _2560_1600 = WQXGA;
  @Nonnull
  public static final Resolution WQHD = new Resolution( 2560, 1440 );
  @Nonnull
  public static final Resolution _2560_1440 = WQHD;
  @Nonnull
  public static final Resolution QXGA = new Resolution( 2048, 1536 );
  @Nonnull
  public static final Resolution _2048_1536 = QXGA;
  @Nonnull
  public static final Resolution WUXGA = new Resolution( 1920, 1200 );
  @Nonnull
  public static final Resolution _1920_1200 = WUXGA;
  @Nonnull
  public static final Resolution HD_1080 = new Resolution( 1920, 1080 );
  @Nonnull
  public static final Resolution _1920_1080 = HD_1080;
  @Nonnull
  public static final Resolution WSXGA_PLUS = new Resolution( 1680, 1050 );
  @Nonnull
  public static final Resolution _1680_1050 = WSXGA_PLUS;
  @Nonnull
  public static final Resolution UXGA = new Resolution( 1600, 1200 );
  @Nonnull
  public static final Resolution _1600_1200 = UXGA;
  @Nonnull
  public static final Resolution WSXGA = new Resolution( 1440, 900 );
  @Nonnull
  public static final Resolution _1140_900 = WSXGA;
  @Nonnull
  public static final Resolution WXGA = new Resolution( 1366, 768 );
  @Nonnull
  public static final Resolution _1366_768 = WXGA;
  @Nonnull
  public static final Resolution SXGA = new Resolution( 1280, 1024 );
  @Nonnull
  public static final Resolution _1280_1024 = SXGA;
  @Nonnull
  public static final Resolution SXGA_960 = new Resolution( 1280, 960 );
  @Nonnull
  public static final Resolution _1280_960 = SXGA_960;
  @Nonnull
  public static final Resolution WXGA_800 = new Resolution( 1280, 800 );
  @Nonnull
  public static final Resolution _1280_800 = WXGA_800;
  @Nonnull
  public static final Resolution WXGA_768 = new Resolution( 1280, 768 );
  @Nonnull
  public static final Resolution _1280_768 = WXGA_768;
  @Nonnull
  public static final Resolution HD_720 = new Resolution( 1280, 720 );
  @Nonnull
  public static final Resolution _1280_720 = HD_720;
  @Nonnull
  public static final Resolution XGA_PLUS = new Resolution( 1152, 864 );
  @Nonnull
  public static final Resolution _1152_864 = XGA_PLUS;
  @Nonnull
  public static final Resolution XGA = new Resolution( 1024, 768 );
  @Nonnull
  public static final Resolution _1024_768 = XGA;
  @Nonnull
  public static final Resolution SVGA = new Resolution( 800, 600 );
  @Nonnull
  public static final Resolution _800_600 = SVGA;
  @Nonnull
  public static final Resolution WVGA = new Resolution( 800, 480 );
  @Nonnull
  public static final Resolution _800_480 = XGA;
  @Nonnull
  public static final Resolution DVD = new Resolution( 720, 576 );
  @Nonnull
  public static final Resolution _720_576 = DVD;
  @Nonnull
  public static final Resolution VGA = new Resolution( 640, 480 );
  @Nonnull
  public static final Resolution _640_480 = VGA;
  @Nonnull
  public static final Resolution HVGA = new Resolution( 480, 320 );
  @Nonnull
  public static final Resolution _480_320 = HVGA;
  @Nonnull
  public static final Resolution WQVGA = new Resolution( 400, 240 );
  @Nonnull
  public static final Resolution _400_240 = WQVGA;
  @Nonnull
  public static final Resolution QVGA = new Resolution( 320, 240 );
  @Nonnull
  public static final Resolution _320_240 = QVGA;
  @Nonnull
  public static final Resolution HQVGA = new Resolution( 240, 160 );
  @Nonnull
  public static final Resolution _240_160 = HQVGA;
  /**
   * @noinspection ConstantNamingConvention
   */
  @Nonnull
  public static final Resolution LoRES = new Resolution( 160, 160 );
  @Nonnull
  public static final Resolution _160_160 = LoRES;
  @Nonnull
  public static final Resolution QQVGA = new Resolution( 160, 120 );
  @Nonnull
  public static final Resolution _160_120 = QQVGA;

  public Resolutions() {
    this( Arrays.asList(
      FOUR_K,
      QUXGA,
      QXGA,
      WUXGA,
      HD_1080,
      WSXGA_PLUS,
      UXGA,
      WSXGA,
      WXGA,
      SXGA,
      SXGA_960,
      WXGA_800,
      WXGA_768,
      HD_720,
      XGA_PLUS,
      XGA,
      SVGA,
      DVD,
      VGA,

      //new Resolution( "", 480, 480 ),
      HVGA,
      WQVGA,
      QVGA,
      //new Resolution( "", 256, 256 ),
      HQVGA,
      //new Resolution( "", 208, 208 ),
      LoRES,
      QQVGA
    )
    );
  }

  public Resolutions( @Nonnull Iterable<? extends Resolution> screenResolutions ) {
    for ( Resolution resolution : screenResolutions ) {
      this.screenResolutions.add( resolution );
    }

    Collections.sort( this.screenResolutions );
    Collections.reverse( this.screenResolutions );
  }

  /**
   * Returns the common dimension that is the same or smaller
   *
   * @param maxWidth  the max width
   * @param maxHeight the max height
   * @return the best dimension for the max width/height
   */
  @Nonnull
  public Resolution getBest( int maxWidth, int maxHeight ) {
    for ( Resolution resolution : screenResolutions ) {
      if ( maxWidth >= resolution.getWidth() && maxHeight >= resolution.getHeight() ) {
        return resolution;
      }
    }

    throw new IllegalArgumentException( "No resolution found for " + maxWidth + "/" + maxHeight );
  }

  /**
   * Returns the common dimensions
   *
   * @return the common dimensions
   */
  @Nonnull
  public List<? extends Resolution> getScreenResolutions() {
    return Collections.unmodifiableList( screenResolutions );
  }
}
