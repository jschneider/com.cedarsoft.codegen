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
import org.junit.*;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;

import static org.junit.Assert.*;


/**
 * <p/>
 * Date: May 25, 2007<br>
 * Time: 3:27:15 PM<br>
 */
public class ButtonPresenterTest {
  private JButtonPresenter buttonPresenter;
  private Node node;
  private AbstractAction action;
  private MockLookup lookup;

  @Before
  public void setUp() throws Exception {
    buttonPresenter = new JButtonPresenter();
    lookup = new MockLookup();
    action = new AbstractAction( "The Action" ) {
      @Override
      public void actionPerformed( ActionEvent e ) {
      }
    };
    action.putValue( Action.SELECTED_KEY, Boolean.FALSE );
    lookup.store( Action.class, action );

    node = new DefaultNode( "theButtonNode", lookup );
  }

  @Test
  public void testWeak2() {
    WeakReference<Object> reference = new WeakReference<Object>( buttonPresenter.present( node ) );
    assertNotNull( reference.get() );
    System.gc();
    System.gc();
    System.gc();
    assertNull( reference.get() );
  }

  @Test
  public void testWeakCHeckbox() {
    WeakReference<Object> reference = new WeakReference<Object>( new JCheckboxPresenter().present( node ) );
    assertNotNull( reference.get() );
    System.gc();
    System.gc();
    System.gc();
    assertNull( reference.get() );
  }

  @Test
  public void testWeak() {
    buttonPresenter = new JButtonPresenter();
    WeakReference<JButton> reference = new WeakReference<JButton>( buttonPresenter.present( node ) );
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );
    assertNotNull( reference.get() );
    System.gc();
    System.gc();
    System.gc();
    assertNull( reference.get() );
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );

    lookup.store( Action.class, new AbstractAction( "otherAction" ) {
      @Override
      public void actionPerformed( ActionEvent e ) {
      }
    } );
    assertEquals( 0, lookup.getLookupChangeSupport().getListeners().size() );
    assertNull( reference.get() );
  }

  @Test
  public void testIt() {
    JCheckBox checkBox = new JCheckBox();
    AbstractAction daAction = new AbstractAction( "asf" ) {
      @Override
      public void actionPerformed( ActionEvent e ) {
      }
    };

    checkBox.setAction( daAction );

    assertFalse( checkBox.isSelected() );
    daAction.putValue( Action.SELECTED_KEY, Boolean.TRUE );

    assertTrue( checkBox.isSelected() );
    checkBox.doClick();
    assertFalse( checkBox.isSelected() );
    assertFalse( ( Boolean ) daAction.getValue( Action.SELECTED_KEY ) );
  }

  @Test
  public void testCheckBox() {
    JCheckboxPresenter presenter = new JCheckboxPresenter();
    JCheckBox checkBox = presenter.present( node );

    assertSame( action, checkBox.getAction() );

    assertFalse( checkBox.isSelected() );
    assertFalse( Boolean.TRUE.equals( action.getValue( Action.SELECTED_KEY ) ) );

    checkBox.doClick();

    assertTrue( checkBox.isSelected() );
    assertTrue( Boolean.TRUE.equals( action.getValue( Action.SELECTED_KEY ) ) );

    action.putValue( Action.SELECTED_KEY, Boolean.FALSE );
    assertFalse( checkBox.isSelected() );

    assertEquals( "The Action", checkBox.getText() );
  }

  @Test
  public void testNoAction() {
    try {
      buttonPresenter.present( new DefaultNode( "a" ) );
      fail( "Where is the Exception" );
    } catch ( IllegalStateException ignore ) {
    }
  }

  @Test
  public void testCreateButton() {
    JButton button = buttonPresenter.present( node );
    assertNotNull( button );
    assertSame( action, button.getAction() );

    AbstractAction otherAction = new AbstractAction( "other Action" ) {
      @Override
      public void actionPerformed( ActionEvent e ) {
      }
    };
    lookup.store( Action.class, otherAction );
    assertSame( otherAction, button.getAction() );
  }
}
