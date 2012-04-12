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

import com.cedarsoft.commons.struct.DefaultNode;
import com.cedarsoft.commons.struct.Node;
import com.cedarsoft.lookup.Lookups;
import com.cedarsoft.presenter.StructStringPresenter;
import com.cedarsoft.swing.presenter.JMenuBarPresenter;

import javax.annotation.Nonnull;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.List;

/**
 *
 */
public class AvatMenuDemo {
  public static void main( String[] args ) throws Exception {
    new AvatMenuDemo().run();
  }

  protected Node rootNode;

  public AvatMenuDemo() throws Exception {
    Product product = Read.deserialize( getClass().getResource( "productFixed.xml" ).openStream() );
    //    Product product = Read.deserialize( getClass().getResource( "productVariableSubGroupsMasks.xml" ).openStream() );

    //Build the structure
    rootNode = new DefaultNode( "jviewStructure", Lookups.dynamicLookup( product ) );
    for ( Group group : product.getStructure().getGroups() ) {
      DefaultNode groupNode = new DefaultNode( group.getName(), Lookups.dynamicLookup( group, createAction( group ) ) );
      rootNode.addChild( groupNode );

      List<SubGroup> subGroups = group.getSubGroups();
      if ( subGroups != null && !subGroups.isEmpty() ) {
        for ( SubGroup subGroup : subGroups ) {
          DefaultNode subGroupNode = new DefaultNode( subGroup.getName(), Lookups.dynamicLookup( subGroup, createAction( subGroup ) ) );
          groupNode.addChild( subGroupNode );

          for ( Mask mask : subGroup.getMasks() ) {
            subGroupNode.addChild( new DefaultNode( mask.getName(), Lookups.dynamicLookup( mask, createAction( mask ) ) ) );
          }
        }
      }
    }

    //output the structure
    System.out.println( new StructStringPresenter().present( rootNode ) );
  }

  private static Action createAction( @Nonnull Mask mask ) {
    return new CommandAction( mask.getText(), mask.getCommand() );
  }

  private static Action createAction( @Nonnull SubGroup subGroup ) {
    return new CommandAction( subGroup.getText(), subGroup.getCommand() );
  }

  @Nonnull
  private static Action createAction( @Nonnull Group group ) {
    CommandAction action = new CommandAction( group.getCommand() );
    attachIcon( action, group.getIcon() );
    return action;
  }

  private static void attachIcon( CommandAction action, String pathToIcon ) {
    URL resource = AvatMenuDemo.class.getResource( pathToIcon.substring( 1 ) );
    if ( resource == null ) {
      System.out.println( "Resource not found: " + pathToIcon );
    } else {
      action.putValue( Action.SMALL_ICON, new ImageIcon( resource ) );
      action.putValue( Action.NAME, "" );
    }
  }

  protected void run() {
    JFrame frame = createFrame();
    applyPresentation( frame );
    frame.setVisible( true );
  }

  protected void applyPresentation( @Nonnull JFrame frame ) {
    frame.setJMenuBar( new JMenuBarPresenter().present( rootNode ) );
  }

  @Nonnull
  protected JFrame createFrame() {
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation( JFrame.DISPOSE_ON_CLOSE );

    frame.pack();
    frame.setSize( 800, 600 );
    frame.setLocationRelativeTo( null );
    return frame;
  }

  public static final class CommandAction extends AbstractAction {
    @Nonnull
    @Nonnull
    private final String command;

    public CommandAction( @Nonnull String command ) {
      this( command, command );
    }

    public CommandAction( @Nonnull String text, @Nonnull String command ) {
      super( text );
      this.command = command;
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
      System.out.println( "Performing command: " + command );
    }
  }
}
