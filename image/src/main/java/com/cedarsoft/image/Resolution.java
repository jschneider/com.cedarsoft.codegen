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

import java.awt.Dimension;

/**
 * Represents a resolution.
 * Resolutions can be compared using the width as first criteria and the height as second.
 * <p/>
 * This means that 100/1 is larger than 99/Integer.MAX
 */
public class Resolution implements Comparable<Resolution> {
  private final int width;
  private final int height;
  @Nonnull
  private final AspectRatio aspectRatio;

  /**
   * Creates a new resolution based on the dimension
   *
   * @param dimension the dimension
   */
  public Resolution( @Nonnull Dimension dimension ) {
    this( dimension.width, dimension.height );
  }

  /**
   * Creates a new resolution
   *
   * @param width  the width
   * @param height the height
   */
  public Resolution( int width, int height ) {
    this.width = width;
    this.height = height;
    this.aspectRatio = AspectRatio.get( width, height );
  }

  /**
   * Returns the aspect ratio
   *
   * @return the aspect ratio
   */
  @Nonnull
  public AspectRatio getAspectRatio() {
    return aspectRatio;
  }

  /**
   * Returns the width
   *
   * @return the width
   */
  public int getWidth() {
    return width;
  }

  /**
   * The height
   *
   * @return the height
   */
  public int getHeight() {
    return height;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof Resolution ) ) return false;

    Resolution that = ( Resolution ) o;

    if ( height != that.height ) return false;
    if ( width != that.width ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result = width;
    result = 31 * result + height;
    return result;
  }

  @Override
  public int compareTo( Resolution o ) {
    if ( width != o.width ) {
      return Integer.valueOf( width ).compareTo( o.width );
    }
    return Integer.valueOf( height ).compareTo( o.height );
  }

  @Override
  public String toString() {
    return "(" + width + "/" + height + ")";
  }

  /**
   * Converts the resolution to a dimensino
   *
   * @return a dimension with the same width/height
   */
  @Nonnull
  public Dimension toDimension() {
    return new Dimension( width, height );
  }
}
