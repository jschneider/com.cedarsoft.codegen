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

import com.cedarsoft.commons.struct.DefaultNode;
import com.cedarsoft.commons.struct.Node;
import com.cedarsoft.commons.struct.StructPart;
import com.cedarsoft.lookup.Lookups;
import com.cedarsoft.swing.presenter.ButtonBarPresenter;

import javax.annotation.Nonnull;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JFrame;
import java.awt.Color;
import java.awt.event.ActionEvent;

/**
 *
 */
public class TwoFramesDemo {
  Node model;

  public TwoFramesDemo() {
    model = new DefaultNode( "basicGroupButtonBar" );
    model.addChild( new DefaultNode( "button0", Lookups.singletonLookup( Action.class, new GroupButtonAction( "button0" ) ) ) );
    model.addChild( new DefaultNode( "button1", Lookups.singletonLookup( Action.class, new GroupButtonAction( "button1" ) ) ) );
    model.addChild( new DefaultNode( "button2", Lookups.singletonLookup( Action.class, new GroupButtonAction( "button2" ) ) ) );
    model.addChild( new DefaultNode( "button3", Lookups.dynamicLookup( new GroupButtonAction( "button3" ), new MyBasicGroupButtonPresenter() ) ) );
    model.addChild( new DefaultNode( "button4", Lookups.singletonLookup( Action.class, new GroupButtonAction( "button4" ) ) ) );


    Node child = model.getChildren().get( 3 );
    if ( child.getLookup().lookup( Action.class ) == null ) {
      throw new IllegalStateException( "uups" );
    }
  }

  private void run() throws InterruptedException {
    JFrame frame0 = new JFrame();
    frame0.getContentPane().add( new BasicGroupButtonBarPresenter().present( model ) );
    frame0.pack();
    frame0.setVisible( true );
    frame0.setSize( 600, 200 );

    JFrame frame1 = new JFrame();
    frame1.getContentPane().add( new ButtonBarPresenter().present( model ) );
    frame1.pack();
    frame1.setVisible( true );
    frame1.setSize( 600, 200 );
    frame1.setLocation( 0, 500 );

    Thread.sleep( 2000 );

    //now change the model
    System.out.println( "removing child:" );
    model.detachChild( model.getChildren().get( 2 ) );
    Thread.sleep( 2000 );

    System.out.println( "Adding child" );
    model.addChild( 1, new DefaultNode( "newButton", Lookups.singletonLookup( Action.class, new GroupButtonAction( "newButton" ) ) ) );
  }

  public static void main( String[] args ) throws Exception {
    new TwoFramesDemo().run();
  }

  public static class GroupButtonAction extends AbstractAction {
    public GroupButtonAction( @Nonnull String name ) {
      super( name );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
      System.out.println( "clicked on: " + getValue( Action.NAME ) );
      setEnabled( false );
    }
  }

  private static class MyBasicGroupButtonPresenter implements BasicGroupButtonPresenter {
    @Nonnull
    public JButton present( @Nonnull StructPart struct ) {
      JButton button = new JButton();
      button.setBackground( Color.orange );
      button.setForeground( Color.WHITE );
      button.setAction( struct.getLookup().lookup( Action.class ) );
      return button;
    }

  }
}
