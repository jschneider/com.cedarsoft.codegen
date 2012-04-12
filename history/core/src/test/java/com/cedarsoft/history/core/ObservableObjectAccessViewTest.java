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

package com.cedarsoft.history.core;

import javax.annotation.Nonnull;
import org.junit.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class ObservableObjectAccessViewTest {
  private ClusteredElementsCollection<Number> collection;
  private ObservableObjectAccessView<Integer> view;

  @Before
  public void setUp() throws Exception {
    collection = new ClusteredElementsCollection<Number>();
    view = new ObservableObjectAccessView<Integer>( collection, new ObservableObjectAccessView.TypeBridge<Integer>( Integer.class ) );
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testSinglMof() {
    try {
      Collections.singletonList( "a" ).add( "b" );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }
  }

  @Test
  public void testInitial() {
    collection.add( 1 );
    collection.add( 2 );
    collection.add( 3 );

    view = new ObservableObjectAccessView<Integer>( collection, new ObservableObjectAccessView.TypeBridge<Integer>( Integer.class ) );
    assertEquals( 3, view.getElements().size() );
  }

  @Test
  public void testListener2() {
    final ClusteredElementsCollection<Integer> core = new ClusteredElementsCollection<Integer>();
    DelegatingClusteredObservableObjectAccess<Integer> access = new DelegatingClusteredObservableObjectAccess<Integer>() {
      @Override
      @Nonnull
      public ClusteredObservableObjectAccess<Integer> getDelegate() {
        return core;
      }
    };

    ObservableObjectAccessView<Integer> view = new ObservableObjectAccessView<Integer>( access, new ObservableObjectAccessView.TypeBridge<Integer>( Integer.class ) );

    final List<Integer> deleted = new ArrayList<Integer>();
    final List<Integer> added = new ArrayList<Integer>();
    final List<Integer> changed = new ArrayList<Integer>();

    view.addElementListener( new SingleElementsListener<Integer>() {
      @Override
      public void elementDeleted( @Nonnull ObservableCollection<? extends Integer> source, @Nonnull Integer element, int index ) {
        deleted.add( element );
      }

      @Override
      public void elementAdded( @Nonnull ObservableCollection<? extends Integer> source, @Nonnull Integer element, int index ) {
        added.add( element );
      }

      @Override
      public void elementChanged( @Nonnull ObservableCollection<? extends Integer> source, @Nonnull Integer element, int index ) {
        changed.add( element );
      }
    } );


    assertEquals( 0, deleted.size() );
    assertEquals( 0, added.size() );
    assertEquals( 0, changed.size() );

    core.add( 4 );

    assertEquals( 1, added.size() );
  }

  @Test
  public void testListeners() {
    final List<Integer> deleted = new ArrayList<Integer>();
    final List<Integer> added = new ArrayList<Integer>();
    final List<Integer> changed = new ArrayList<Integer>();

    view.addElementListener( new SingleElementsListener<Integer>() {
      @Override
      public void elementDeleted( @Nonnull ObservableCollection<? extends Integer> source, @Nonnull Integer element, int index ) {
        deleted.add( element );
      }

      @Override
      public void elementAdded( @Nonnull ObservableCollection<? extends Integer> source, @Nonnull Integer element, int index ) {
        added.add( element );
      }

      @Override
      public void elementChanged( @Nonnull ObservableCollection<? extends Integer> source, @Nonnull Integer element, int index ) {
        changed.add( element );
      }
    } );

    assertEquals( 0, deleted.size() );
    assertEquals( 0, added.size() );
    assertEquals( 0, changed.size() );

    collection.add( 4L );
    collection.commit( 4L );
    collection.remove( 4L );

    assertEquals( 0, deleted.size() );
    assertEquals( 0, added.size() );
    assertEquals( 0, changed.size() );

    collection.add( 4 );
    assertEquals( 0, deleted.size() );
    assertEquals( 1, added.size() );
    assertEquals( 0, changed.size() );
    collection.commit( 4 );
    assertEquals( 0, deleted.size() );
    assertEquals( 1, added.size() );
    assertEquals( 1, changed.size() );
    collection.remove( 4 );
    assertEquals( 1, deleted.size() );
    assertEquals( 1, added.size() );
    assertEquals( 1, changed.size() );
  }

  @Test
  public void testIt() {
    collection.add( 5 );
    assertEquals( 1, collection.size() );
    assertEquals( 1, view.getElements().size() );

    collection.add( 4L );
    assertEquals( 2, collection.size() );
    assertEquals( 1, view.getElements().size() );

    collection.remove( 5 );
    assertEquals( 1, collection.size() );
    assertEquals( 0, view.getElements().size() );
  }
}
