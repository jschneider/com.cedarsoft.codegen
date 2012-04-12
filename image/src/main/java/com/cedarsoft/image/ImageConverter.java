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
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;

/**
 * <p>ImageConverter class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ImageConverter {
  /**
   * Calculates the new dimension of the image
   *
   * @param original           the original image
   * @param originalDPI the original resolution
   * @param targetDPI   the target resolution
   * @return the dimension
   */
  public Dimension calculateNewDimensions( @Nonnull RenderedImage original, @Nonnull DPI originalDPI, @Nonnull DPI targetDPI ) {
    int newWidth = original.getWidth() * targetDPI.getDpi() / originalDPI.getDpi();
    int newHeight = original.getHeight() * targetDPI.getDpi() / originalDPI.getDpi();
    return new Dimension( newWidth, newHeight );
  }

  /**
   * Resizes the image to the given size
   *
   * @param original        a {@link BufferedImage} object.
   * @param targetDimension a {@link Dimension} object.
   * @return a {@link BufferedImage} object.
   */
  @Nonnull
  public BufferedImage resize( @Nonnull Image original, @Nonnull Dimension targetDimension ) {
    BufferedImage resized = new BufferedImage( targetDimension.width, targetDimension.height, BufferedImage.TYPE_INT_RGB );
    Graphics2D graphics2D = resized.createGraphics();
    graphics2D.setRenderingHint( RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR );
    graphics2D.drawImage( original, 0, 0, targetDimension.width, targetDimension.height, null );
    return resized;
  }

  /**
   * <p>resize</p>
   *
   * @param original           a {@link BufferedImage} object.
   * @param originalDPI a {@link DPI} object.
   * @param targetDPI   a {@link DPI} object.
   * @return a {@link BufferedImage} object.
   */
  @Nonnull
  public BufferedImage resize( @Nonnull BufferedImage original, @Nonnull DPI originalDPI, @Nonnull DPI targetDPI ) {
    return resize( original, calculateNewDimensions( original, originalDPI, targetDPI ) );
  }
}
