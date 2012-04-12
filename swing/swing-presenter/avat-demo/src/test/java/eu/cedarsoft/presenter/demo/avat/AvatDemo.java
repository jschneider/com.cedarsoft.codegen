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

package com.cedarsoft.swing.presenter.demo.avat;

import com.cedarsoft.commons.struct.StructPart;
import com.cedarsoft.presenter.Presenter;
import com.cedarsoft.swing.presenter.ButtonBarPresenter;
import com.cedarsoft.swing.presenter.FancyButtonPresenter;
import com.cedarsoft.swing.presenter.JButtonPresenter;
import com.cedarsoft.swing.presenter.JComboBoxPresenter;

import javax.annotation.Nonnull;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;

/**
 *
 */
public class AvatDemo extends AvatMenuDemo {
  public static void main( String[] args ) throws Exception {
    new AvatDemo().run();
  }

  public AvatDemo() throws Exception {
  }

  @Override
  protected void applyPresentation( @Nonnull JFrame frame ) {
    frame.add( new ButtonBarPresenter() {
      @Override
      @Nonnull
      protected Presenter<? extends JComponent> getChildPresenter( @Nonnull StructPart child ) {
        //        return super.getChildPresenter( child );
        return new FancyButtonPresenter();
      }
    }.present( rootNode ), BorderLayout.NORTH );


    JPanel rightPanel = new JPanel( new BorderLayout() );

    //ich nehme einfach das zweite Kind - hier muss der Mask-Manager entscheiden, welcher Knoten benutzt werden soll
    //Das zweite Kind deshalb, weil das die meisten Unterpunkte hat.
    rightPanel.add( new JComboBoxPresenter().present( rootNode.getChildren().get( 1 ) ), BorderLayout.NORTH );


    JPanel buttons = new JPanel();
    buttons.add( new ButtonBarPresenter( ButtonBarPresenter.Orientation.Vertical ) {
      @Override
      @Nonnull
      protected Presenter<? extends JComponent> getChildPresenter( @Nonnull StructPart child ) {
        return new JButtonPresenter();
      }

      //Hier nehme ich einfach das erste Kind
    }.present( rootNode.getChildren().get( 1 ).getChildren().get( 0 ) ) );

    rightPanel.add( buttons );
    frame.add( rightPanel, BorderLayout.EAST );
  }
}
