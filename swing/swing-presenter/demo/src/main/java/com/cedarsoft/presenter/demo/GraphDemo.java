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

package com.cedarsoft.swing.presenter.demo;

import com.cedarsoft.swing.presenter.demo.graph.Graph2DPresenter;
import javax.annotation.Nonnull;
import y.view.Graph2D;
import y.view.Graph2DView;
import y.view.NavigationMode;

import javax.swing.JFrame;

/**
 *
 */
public class GraphDemo extends MenuDemo {
  public static void main( String[] args ) {
    new GraphDemo().run();
  }

  @Override
  @Nonnull
  protected JFrame createFrame() {
    JFrame frame = super.createFrame();

    Graph2D graph2D = new Graph2DPresenter().present( rootNode );

    Graph2DView graph2DView = new Graph2DView( graph2D );
    graph2DView.addViewMode( new NavigationMode() );
    graph2DView.fitContent();
    frame.getContentPane().add( graph2DView );
    return frame;
  }
}
