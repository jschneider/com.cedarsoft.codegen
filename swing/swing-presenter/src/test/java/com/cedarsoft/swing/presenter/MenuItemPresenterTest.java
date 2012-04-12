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
import com.cedarsoft.commons.struct.Node;
import com.cedarsoft.lookup.MappedLookup;
import org.junit.*;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JMenuItem;
import javax.swing.text.DefaultEditorKit;
import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: Jun 1, 2007<br>
 * Time: 3:55:27 PM<br>
 */
public class MenuItemPresenterTest {
  public MenuItemPresenterTest() {
    super();
  }

  public void testWeak2() {
    DefaultJMenuItemPresenter presenter = new DefaultJMenuItemPresenter();

    MappedLookup lookup = new MappedLookup();
    Node node = new DefaultNode( "menu", lookup );
    AbstractAction action = new AbstractAction( "The action" ) {
      @Override
      public void actionPerformed( ActionEvent e ) {
      }
    };
    lookup.store( Action.class, action );

    WeakReference<Object> reference = new WeakReference<Object>( presenter.present( node ) );
    assertNotNull( reference.get() );
    System.gc();
    System.gc();
    System.gc();
    assertNull( reference.get() );
  }

  @Test
  public void testIt() {
    DefaultJMenuItemPresenter presenter = new DefaultJMenuItemPresenter();

    MappedLookup lookup = new MappedLookup();
    Node node = new DefaultNode( "menu", lookup );
    AbstractAction action = new AbstractAction( "The action" ) {
      @Override
      public void actionPerformed( ActionEvent e ) {
      }
    };
    lookup.store( Action.class, action );

    JMenuItem menuItem = presenter.present( node );
    assertSame( action, menuItem.getAction() );
    assertEquals( "The action", menuItem.getText() );

    DefaultEditorKit.CopyAction action2 = new DefaultEditorKit.CopyAction();
    lookup.store( Action.class, action2 );

    assertSame( action2, menuItem.getAction() );
  }

  public void testInitialText() {
    DefaultJMenuItemPresenter presenter = new DefaultJMenuItemPresenter();

    MappedLookup lookup = new MappedLookup();
    AbstractAction action = new AbstractAction( "The action" ) {
      @Override
      public void actionPerformed( ActionEvent e ) {
      }
    };
    lookup.store( Action.class, action );

    Node node = new DefaultNode( "menu", lookup );
    JMenuItem menuItem = presenter.present( node );
    assertSame( action, menuItem.getAction() );
    assertEquals( "The action", menuItem.getText() );
  }
}
