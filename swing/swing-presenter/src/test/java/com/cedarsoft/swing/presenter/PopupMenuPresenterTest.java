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

package com.cedarsoft.swing.presenter;

import com.cedarsoft.commons.struct.DefaultNode;
import com.cedarsoft.lookup.Lookups;
import javax.annotation.Nonnull;
import org.junit.*;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JFrame;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static org.junit.Assert.*;

/**
 *
 */
public class PopupMenuPresenterTest {
  DefaultNode root;

  @Before
  public void setUp() throws Exception {
    root = new DefaultNode( "root", Lookups.singletonLookup( Action.class, createAction( 99 ) ) );
    root.addChild( new DefaultNode( "1", Lookups.singletonLookup( Action.class, createAction( 0 ) ) ) );
    root.addChild( new DefaultNode( "2", Lookups.singletonLookup( Action.class, createAction( 1 ) ) ) );
    root.addChild( new DefaultNode( "3", Lookups.singletonLookup( Action.class, createAction( 2 ) ) ) );
    root.addChild( new DefaultNode( "4", Lookups.singletonLookup( Action.class, createAction( 3 ) ) ) );
  }

  @Nonnull
  private static Action createAction( int id ) {
    return new AbstractAction( String.valueOf( id ) ) {
      @Override
      public void actionPerformed( ActionEvent e ) {
      }
    };
  }

  @Test
  public void testWeak() {
    WeakReference<Object> reference = new WeakReference<Object>( new DefaultJPopupMenuPresenter().present( root ) );
    assertNotNull( reference.get() );
    System.gc();
    System.gc();
    System.gc();
    assertNull( reference.get() );
  }

  @Test
  public void testTexts() {
    JPopupMenu popupMenu = new DefaultJPopupMenuPresenter().present( root );

    MenuElement[] subElements = popupMenu.getSubElements();
    assertEquals( 4, subElements.length );
    for ( int i = 0; i < subElements.length; i++ ) {
      MenuElement menuElement = subElements[i];
      AbstractButton button = ( AbstractButton ) menuElement.getComponent();
      assertNotNull( button.getAction() );
      assertEquals( String.valueOf( i ), button.getText() );
    }
  }

  @Test
  public void testIt() {
    JPopupMenu popupMenu = new DefaultJPopupMenuPresenter().present( root );
    assertEquals( 4, popupMenu.getSubElements().length );
  }

  @Test
  public void testDynamic() {
    JPopupMenu popupMenu = new DefaultJPopupMenuPresenter().present( root );
    assertEquals( 4, popupMenu.getSubElements().length );

    root.addChild( new DefaultNode( "other", Lookups.dynamicLookup( createAction( 4 ) ) ) );
    assertEquals( 5, popupMenu.getSubElements().length );

    root.detachChild( 2 );
    assertEquals( 4, popupMenu.getSubElements().length );
    root.detachChild( 2 );
    assertEquals( 3, popupMenu.getSubElements().length );
  }

  public static void main( String[] args ) throws Exception {
    final Set<WeakReference<JPopupMenu>> menues = new HashSet<WeakReference<JPopupMenu>>();

    start( menues );

    new Thread( new Runnable() {
      @Override
      public void run() {
        while ( true ) {
          try {
            Thread.sleep( 1000 );
          } catch ( InterruptedException ignore ) {
          }
          synchronized ( menues ) {
            System.gc();
            System.gc();
            System.gc();
            System.gc();
            System.gc();
            for ( Iterator<WeakReference<JPopupMenu>> it = menues.iterator(); it.hasNext(); ) {
              WeakReference<JPopupMenu> reference = it.next();
              if ( reference.get() == null ) {
                it.remove();
              }
            }
            System.out.println( menues.size() );
          }
        }
      }
    } ).start();
  }

  private static void start( final Set<WeakReference<JPopupMenu>> menues ) throws Exception {
    final PopupMenuPresenterTest test = new PopupMenuPresenterTest();
    test.setUp();

    JFrame frame = new JFrame();

    frame.getContentPane().addMouseListener( new MouseAdapter() {
      @Override
      public void mouseClicked( MouseEvent e ) {
        JPopupMenu menu = new DefaultJPopupMenuPresenter().present( test.root );
        menu.show( ( Component ) e.getSource(), e.getX(), e.getY() );

        synchronized ( menues ) {
          menues.add( new WeakReference<JPopupMenu>( menu ) );
        }

        test.root.addChild( new DefaultNode( "" + System.currentTimeMillis(), Lookups.dynamicLookup( createAction( 8 ) ) ) );
      }
    } );

    frame.setSize( 800, 600 );
    frame.setLocationRelativeTo( null );
    frame.setVisible( true );
  }
}
