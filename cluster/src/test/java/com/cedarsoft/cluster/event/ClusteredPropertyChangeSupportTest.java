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

package com.cedarsoft.cluster.event;

import javax.annotation.Nonnull;
import org.junit.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class ClusteredPropertyChangeSupportTest {
  private ClusteredPropertyChangeSupport pcs;

  @Before
  public void setUp() throws Exception {
    pcs = new ClusteredPropertyChangeSupport( "asdf" );
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testIt() {
    Assert.assertEquals( 0, pcs.getTransientSupport().getPropertyChangeListeners().length );
    Assert.assertEquals( 0, pcs.getNonTransientListeners().size() );

    pcs.addPropertyChangeListener( new PropertyChangeListener() {
      @Override
      public void propertyChange( PropertyChangeEvent evt ) {
      }
    }, true );

    Assert.assertEquals( 1, pcs.getTransientSupport().getPropertyChangeListeners().length );
    Assert.assertEquals( 0, pcs.getNonTransientListeners().size() );

    pcs.addPropertyChangeListener( new PropertyChangeListener() {
      @Override
      public void propertyChange( PropertyChangeEvent evt ) {
      }
    }, false );

    Assert.assertEquals( 1, pcs.getTransientSupport().getPropertyChangeListeners().length );
    Assert.assertEquals( 1, pcs.getNonTransientListeners().size() );
  }

  @Test
  public void testWIthPropName() {
    @Nonnull
    final List<PropertyChangeEvent> events = new ArrayList<PropertyChangeEvent>();

    PropertyChangeListener listener = new PropertyChangeListener() {
      @Override
      public void propertyChange( PropertyChangeEvent evt ) {
        events.add( evt );
      }
    };
    pcs.addPropertyChangeListener( "asdf", listener, false );

    pcs.firePropertyChange( "asdf", "old", "new" );

    assertEquals( 1, events.size() );
    assertEquals( "new", events.get( 0 ).getNewValue() );

    events.clear();

    pcs.removePropertyChangeListener( "asdf", listener );
    pcs.firePropertyChange( "asdf", "old", "new2" );

    assertEquals( 0, events.size() );
  }
}
