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

package com.cedarsoft.cluster;

import javax.annotation.Nonnull;

import com.cedarsoft.objectaccess.ChangeListener;
import com.cedarsoft.objectaccess.ChangedEvent;
import com.cedarsoft.test.utils.MockitoTemplate;
import org.junit.*;
import org.mockito.Mock;

import java.beans.PropertyChangeSupport;

import static org.mockito.Mockito.*;

/**
 *
 */
public class ClusteredChangeListenerSupportTest {
  @Test
  public void testIt() throws Exception {
    final ClusteredChangeListenerSupport<String> support = new ClusteredChangeListenerSupport<String>( "observed" );
    new MockitoTemplate() {
      @Mock
      private ChangeListener<String> listener;

      @Override
      protected void stub() throws Exception {
      }

      @Override
      protected void execute() throws Exception {
        support.addChangeListener( listener, false );
        support.changed( "da", "path" );
        support.removeChangeListener( listener );
        support.changed( "da", "path", "2" );
        support.addChangeListener( listener, true );
        support.changed( "da", "path", "3" );
      }

      @Override
      protected void verifyMocks() throws Exception {
        verify( listener ).entryChanged( refEq( new ChangedEvent<String>( "observed", null, "da", "path" ) ) );
        verify( listener ).entryChanged( refEq( new ChangedEvent<String>( "observed", null, "da", "path", "3" ) ) );
        verifyNoMoreInteractions( listener );
      }
    }.run();
  }

  @Test
  public void testPropChange() throws Exception {
    @Nonnull
    final PropertyChangeSupport pcs = new PropertyChangeSupport( this );
    final ClusteredChangeListenerSupport<String> changeListenerSupport = new ClusteredChangeListenerSupport<String>( "asdf" );

    new MockitoTemplate() {
      @Mock
      private ChangeListener<String> listener;

      @Override
      protected void stub() throws Exception {
      }

      @Override
      protected void execute() throws Exception {
        changeListenerSupport.addChangeListener( listener, false );
        pcs.addPropertyChangeListener( changeListenerSupport.createPropertyListenerDelegate( "a", "b" ) );
        pcs.firePropertyChange( "daProp", "old", "new" );
        changeListenerSupport.removeChangeListener( listener );
        pcs.firePropertyChange( "daProp2", "new", "newer" );
        changeListenerSupport.addChangeListener( listener, true );
        pcs.firePropertyChange( "daProp3", "new", "newest" );
      }

      @Override
      protected void verifyMocks() throws Exception {
        verify( listener ).entryChanged( refEq( new ChangedEvent<String>( "asdf", null, "a", "b", "daProp" ), "context" ) );
        verify( listener ).entryChanged( refEq( new ChangedEvent<String>( "asdf", null, "a", "b", "daProp3" ), "context" ) );
        verifyNoMoreInteractions( listener );
      }
    }.run();
  }
  
}
