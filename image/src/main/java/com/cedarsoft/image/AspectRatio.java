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

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents an aspect ratio
 */
public class AspectRatio {
  @Nonnull
  public static final AspectRatio AR_1_1 = new AspectRatio( 1, 1 );
  @Nonnull
  public static final AspectRatio AR_3_2 = new AspectRatio( 3, 2 );
  @Nonnull
  public static final AspectRatio AR_4_3 = new AspectRatio( 4, 3 );
  @Nonnull
  public static final AspectRatio AR_5_3 = new AspectRatio( 5, 3 );
  @Nonnull
  public static final AspectRatio AR_5_4 = new AspectRatio( 5, 4 );
  @Nonnull
  public static final AspectRatio AR_16_9 = new AspectRatio( 16, 9 );
  @Nonnull
  public static final AspectRatio AR_16_10 = new AspectRatio( 16, 10 );

  /**
   * Used for paper (A4)...
   */
  @Nonnull
  public static final AspectRatio AR_SQ2_1 = new AspectRatio( Math.sqrt( 2 ), 1 );

  /**
   * @noinspection PublicStaticCollectionField
   */
  @Nonnull
  public static final List<? extends AspectRatio> PRE_DEFINED = Collections.unmodifiableList( Arrays.asList( AR_1_1, AR_3_2, AR_4_3, AR_5_3, AR_5_4, AR_16_9, AR_16_10 ) );

  /**
   * The bounds used when finding the aspect ratio for a width/height
   */
  public static final double BOUNDS = 0.005;

  private final double widthFactor;
  private final double heightFactor;

  AspectRatio( double widthFactor, double heightFactor ) {
    this.widthFactor = widthFactor;
    this.heightFactor = heightFactor;
  }

  public double getRatio() {
    return widthFactor / heightFactor;
  }

  public double getWidthFactor() {
    return widthFactor;
  }

  public double getHeightFactor() {
    return heightFactor;
  }

  @Nonnull
  public AspectRatio invert() {
    //noinspection SuspiciousNameCombination
    return new AspectRatio( heightFactor, widthFactor );
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof AspectRatio ) ) return false;

    AspectRatio that = ( AspectRatio ) o;

    if ( heightFactor != that.heightFactor ) return false;
    if ( widthFactor != that.widthFactor ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    double result = widthFactor;
    result = 31 * result + heightFactor;
    return ( int ) result;
  }

  /**
   * Returns the predefined aspect ratio
   *
   * @param width  the width
   * @param height the height
   * @return the aspect ratio
   */
  @Nonnull
  public static AspectRatio get( double width, double height ) {
    double ratio = width / height;
    for ( AspectRatio aspectRatio : PRE_DEFINED ) {
      double delta = aspectRatio.getRatio() - ratio;
      if ( Math.abs( delta ) < BOUNDS ) {
        return aspectRatio;
      }
    }

    return new AspectRatio( width, height );
  }
}
