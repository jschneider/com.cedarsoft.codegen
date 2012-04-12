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
import com.cedarsoft.lookup.LookupStore;
import com.cedarsoft.lookup.Lookups;

import javax.annotation.Nonnull;
import org.junit.*;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;
import java.awt.event.ActionEvent;
import java.lang.ref.WeakReference;

import static org.junit.Assert.*;


/**
 *
 */
public class JComboBoxPresenterTest {
  DefaultNode root;
  private Action rootAction;
  private JComboBoxPresenter presenter;
  private ListCellRenderer renderer;

  @Before
  public void setUp() throws Exception {
    rootAction = new AbstractAction() {
      @Override
      public void actionPerformed( ActionEvent e ) {
      }
    };

    renderer = new DefaultListCellRenderer();

    root = new DefaultNode( "comboButton", Lookups.dynamicLookup( rootAction, renderer ) );
    addChild( "child0" );
    addChild( "child1" );
    addChild( "child2" );
    presenter = new JComboBoxPresenter();
  }

  @Test
  public void testAction() {
    root = new DefaultNode( "root", Lookups.dynamicLookup() );

    JComboBox box = presenter.present( root );
    assertNull( box.getAction() );

    AbstractAction newAction = new AbstractAction( "theAction" ) {
      @Override
      public void actionPerformed( ActionEvent e ) {
      }
    };
    ( ( LookupStore ) root.getLookup() ).store( Action.class, newAction );
    assertSame( newAction, box.getAction() );
  }

  @Test
  public void testRenderer() {
    root = new DefaultNode( "theRoot", Lookups.dynamicLookup() );
    JComboBox box = presenter.present( root );
    assertNotNull( box.getRenderer() );
    assertEquals( JComboBoxPresenter.StructDefaultCellRenderer.class, box.getRenderer().getClass() );

    DefaultListCellRenderer newRenderer = new DefaultListCellRenderer();
    ( ( LookupStore ) root.getLookup() ).store( ListCellRenderer.class, newRenderer );

    assertEquals( newRenderer, box.getRenderer() );
  }

  private void addChild( @Nonnull String name ) {
    root.addChild( new DefaultNode( name, Lookups.createLookup( Action.class, new AbstractAction( name ) {
      @Override
      public void actionPerformed( ActionEvent e ) {
      }
    } ) ) );
  }

  public static void main( String[] args ) throws Exception {
    JComboBoxPresenterTest test = new JComboBoxPresenterTest();
    test.setUp();
    test.testWeak();
  }

  @Test
  public void testWeak() throws InterruptedException {
    Thread.sleep( 500 );
    WeakReference<Object> reference = new WeakReference<Object>( new JComboBoxPresenter().present( root ) );
    assertNotNull( reference.get() );
    System.gc();
    System.gc();
    System.gc();
    System.gc();
    System.gc();
    Thread.sleep( 500 );
    System.gc();
    System.gc();
    System.gc();
    System.gc();
    assertNull( reference.get() );
  }

  @Test
  public void testSelectionIndex() {
    JComboBox box = presenter.present( root );
    assertSame( rootAction, box.getAction() );
    assertSame( renderer, box.getRenderer() );

    assertEquals( 3, box.getItemCount() );
    assertEquals( -1, box.getSelectedIndex() );

    box.setSelectedIndex( 2 );
    assertEquals( 2, box.getSelectedIndex() );
    assertEquals( root.getChildren().get( 2 ), box.getSelectedItem() );
  }

  @Test
  public void testComboBoxPlain() {
    JComboBox box = new JComboBox();
    box.setAction( new AbstractAction( "the value" ) {
      @Override
      public void actionPerformed( ActionEvent e ) {
        System.out.println( "event... " + e );
      }
    } );

    box.setModel( new DefaultComboBoxModel( new Object[]{"a", "b"} ) );
    assertEquals( "a", box.getSelectedItem() );

    box.setSelectedIndex( 1 );
    assertEquals( "b", box.getSelectedItem() );
  }

}
