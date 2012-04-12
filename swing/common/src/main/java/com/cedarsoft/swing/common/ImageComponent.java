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

package com.cedarsoft.swing.common;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * The image component is able to render images
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ImageComponent extends JComponent {
  private List<Painter> painters = new ArrayList<Painter>();
  private Image image;
  private Dimension imageSize;

  /**
   * {@inheritDoc}
   */
  @Override
  protected void paintComponent( @Nonnull Graphics g ) {
    super.paintComponent( g );
    if ( image != null )
      g.drawImage( image, 0, 0, this );

    if ( !painters.isEmpty() ) {
      for ( Painter painter : painters ) {
        painter.paint( g, this );
      }
    }
  }

  /**
   * Adds a custom paointer
   *
   * @param painter the painter
   */
  public void addPainter( @Nonnull Painter painter ) {
    painters.add( painter );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Dimension getPreferredSize() {
    if ( isPreferredSizeSet() ) {
      return super.getPreferredSize();
    }
    if ( imageSize != null ) {
      return imageSize;
    }
    return super.getPreferredSize();
  }

  /**
   * Sets the image
   *
   * @param imageFile the image file
   * @throws IOException if any.
   */
  public void setImage( @Nonnull File imageFile ) throws IOException {
    setImage( ImageIO.read( imageFile ) );
  }

  /**
   * Sets the image
   *
   * @param image the image
   */
  public void setImage( @Nullable Image image ) {
    this.image = image;
    if ( image == null ) {
      imageSize = null;
    } else {
      int width = image.getWidth( this );
      int height = image.getHeight( this );
      this.imageSize = new Dimension( width, height );
    }
    revalidate();
    repaint();
  }

  /**
   * Extends the painting of the ImageComponent
   */
  public interface Painter {
    /**
     * Paint the custom painting
     *
     * @param g         the context
     * @param component the component
     */
    void paint( @Nonnull Graphics g, @Nonnull JComponent component );
  }
}
