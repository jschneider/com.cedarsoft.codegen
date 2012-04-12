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
import com.cedarsoft.lookup.DynamicLookup;
import com.cedarsoft.lookup.Lookups;
import com.cedarsoft.swing.presenter.AbstractButtonPresenter;
import com.cedarsoft.swing.presenter.JMenuBarPresenter;
import com.cedarsoft.swing.presenter.JMenuPresenter;
import com.cedarsoft.swing.presenter.demo.graph.NodePresenter;
import javax.annotation.Nonnull;
import y.view.Graph2D;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import java.awt.Color;
import java.awt.event.ActionEvent;

/**
 * Demonstrates the building of a demo
 */
public class MenuDemo {
  private AbstractAction addAction;

  public static void main( String[] args ) {
    new MenuDemo().run();
  }

  protected final Node rootNode;

  public MenuDemo() {
    rootNode = new DefaultNode( "menuNode" );
    final DefaultNode fileMenuNode = new DefaultNode( "file", Lookups.singletonLookup( Action.class, new FileAction() ) );
    rootNode.addChild( fileMenuNode );

    fileMenuNode.addChild( new DefaultNode( "open", Lookups.singletonLookup( Action.class, new OpenAction() ) ) );
    fileMenuNode.addChild( new DefaultNode( "close", Lookups.singletonLookup( Action.class, new CloseAction() ) ) );

    fileMenuNode.addChild( new DefaultNode( "increate counter", Lookups.singletonLookup( Action.class, new CounterAction() ) ) );


    DefaultNode recentlyOpenedFilesNode = new DefaultNode( "recentlyOpenedFiles", Lookups.singletonLookup( Action.class, new RecentFilesAction() ) );
    fileMenuNode.addChild( recentlyOpenedFilesNode );

    recentlyOpenedFilesNode.addChild( new DefaultNode( "file0", Lookups.singletonLookup( Action.class, new RecentFileAction( "file0" ) ) ) );
    recentlyOpenedFilesNode.addChild( new DefaultNode( "file1", Lookups.singletonLookup( Action.class, new RecentFileAction( "file1" ) ) ) );
    recentlyOpenedFilesNode.addChild( new DefaultNode( "file2", Lookups.singletonLookup( Action.class, new RecentFileAction( "file2" ) ) ) );
    recentlyOpenedFilesNode.addChild( new DefaultNode( "file3", Lookups.dynamicLookup( new RecentFileAction( "file3" ), new NodePresenter() {
      @Override
      @Nonnull
      protected y.base.Node createPresentation() {
        y.base.Node node = super.createPresentation();
        ( ( Graph2D ) node.getGraph() ).getRealizer( node ).setFillColor( Color.CYAN );
        return node;
      }
    } ) ) );

    fileMenuNode.addChild( new DefaultNode( "separator", Lookups.dynamicLookup( new JSeparator() ) ) );

    {
      final DynamicLookup lookup = new DynamicLookup();
      final AbstractAction[] actions = new AbstractAction[2];

      actions[0] = new AbstractAction( "action0" ) {
        @Override
        public void actionPerformed( ActionEvent e ) {
          lookup.addValue( actions[1] );
        }
      };

      actions[1] = new AbstractAction( "action1" ) {
        @Override
        public void actionPerformed( ActionEvent e ) {
          lookup.addValue( actions[0] );
        }
      };

      lookup.addValue( actions[0] );
      fileMenuNode.addChild( new DefaultNode( "toggleAction", lookup ) );
    }

    fileMenuNode.addChild( new DefaultNode( "separator1", Lookups.dynamicLookup( new JSeparator() ) ) );

    addAction = new AbstractAction( "Add Another Item" ) {
      @Override
      public void actionPerformed( ActionEvent e ) {
        fileMenuNode.addChild( new DefaultNode( String.valueOf( System.currentTimeMillis() ), Lookups.singletonLookup( Action.class, addAction ) ) );
      }
    };
    fileMenuNode.addChild( new DefaultNode( "addAction", Lookups.singletonLookup( Action.class, addAction ) ) );


    rootNode.addChild( new DefaultNode( "customEditMenu", Lookups.dynamicLookup( new EditFileAction(), new MySpecialEditMenuPresenter() ) ) );
  }

  protected void run() {
    JMenuBarPresenter presenter = new JMenuBarPresenter();
    JFrame frame = createFrame();
    frame.setJMenuBar( presenter.present( rootNode ) );
    frame.setVisible( true );
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

  static final class RecentFileAction extends AbstractAction {
    private RecentFileAction( String name ) {
      super( name );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
      System.out.println( "Clicked on recent file " + getValue( Action.NAME ) );
    }
  }

  static class MySpecialEditMenuPresenter extends AbstractButtonPresenter<JMenu> implements JMenuPresenter {
    @Override
    @Nonnull
    public JMenu createPresentation() {
      JMenu theMenu = new JMenu();
      theMenu.add( new JMenuItem( "manually added" ) );
      theMenu.add( new JMenuItem( "manually added 2" ) );
      return theMenu;
    }

    @Override
    protected boolean shallAddChildren() {
      return false;
    }
  }

  private static class FileAction extends AbstractAction {
    public FileAction() {
      super( "File" );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
      throw new UnsupportedOperationException();
    }
  }

  private static class OpenAction extends AbstractAction {
    public OpenAction() {
      super( "Open" );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
      System.out.println( "Opening file" );
    }
  }

  private static class CloseAction extends AbstractAction {
    public CloseAction() {
      super( "Close" );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
      System.out.println( "Closing file" );
    }
  }

  private static class CounterAction extends AbstractAction {
    private int counter;

    {
      updateName();
    }

    private void updateName() {
      this.putValue( Action.NAME, "Counter: " + counter );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
      counter++;
      updateName();
    }
  }

  private static class RecentFilesAction extends AbstractAction {
    public RecentFilesAction() {
      super( "Recent Files" );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
      throw new UnsupportedOperationException();
    }
  }

  private static class EditFileAction extends AbstractAction {
    public EditFileAction() {
      super( "Edit" );
    }

    @Override
    public void actionPerformed( ActionEvent e ) {
      throw new UnsupportedOperationException();
    }
  }
}
