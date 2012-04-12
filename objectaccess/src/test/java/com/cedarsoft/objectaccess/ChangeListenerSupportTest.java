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

package com.cedarsoft.objectaccess;

import javax.annotation.Nonnull;

import com.cedarsoft.test.utils.MockitoTemplate;
import org.junit.*;
import org.mockito.Mock;

import java.beans.PropertyChangeSupport;

import static org.mockito.Mockito.*;

/**
 *
 */
public class ChangeListenerSupportTest {
  @Test
  public void testIt() throws Exception {
    final ChangeListenerSupport<String> changeListenerSupport = new ChangeListenerSupport<String>( "asdf" );

    new MockitoTemplate() {
      @Mock
      private ChangeListener<String> listener;

      @Override
      protected void stub() throws Exception {
      }

      @Override
      protected void execute() throws Exception {
        changeListenerSupport.addChangeListener( listener );
        changeListenerSupport.changed( "context", "the", "property", "path" );
      }

      @Override
      protected void verifyMocks() throws Exception {
        verify( listener ).entryChanged( refEq( new ChangedEvent<String>( "asdf", "context", "the", "property", "path" ) ) );
        verifyNoMoreInteractions( listener );
      }
    }.run();
  }

  @Test
  public void testPropChange() throws Exception {
    @Nonnull
    final PropertyChangeSupport pcs = new PropertyChangeSupport( this );
    final ChangeListenerSupport<String> changeListenerSupport = new ChangeListenerSupport<String>( "asdf" );

    new MockitoTemplate() {
      @Mock
      private ChangeListener<String> listener;

      @Override
      protected void stub() throws Exception {
      }

      @Override
      protected void execute() throws Exception {
        changeListenerSupport.addChangeListener( listener );
        pcs.addPropertyChangeListener( changeListenerSupport.createPropertyListenerDelegate( "a", "b" ) );
        pcs.firePropertyChange( "daProp", "old", "new" );
      }

      @Override
      protected void verifyMocks() throws Exception {
        verify( listener ).entryChanged( refEq( new ChangedEvent<String>( "asdf", null, "a", "b", "daProp" ), "context" ) );
        verifyNoMoreInteractions( listener );
      }
    }.run();
  }
}
