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

import com.cedarsoft.test.utils.MockitoTemplate;
import org.junit.*;
import org.mockito.Mock;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

/**
 *
 */
public class DeletionProcessorsSupportTest {
  @Test
  public void testIt() throws Exception {
    final DeletionProcessorsSupport<String> support = new DeletionProcessorsSupport<String>();

    new MockitoTemplate() {
      @Mock
      private DeletionProcessor<String> processor;

      @Override
      protected void stub() throws Exception {
      }

      @Override
      protected void execute() throws Exception {
        support.addDeletionProcessor( processor );
        assertEquals( 1, support.getDeletionProcessors().size() );
        support.notifyWillBeDeleted( "daObject" );
        support.removeDeletionProcessor( processor );
        support.notifyWillBeDeleted( "daObject2" );
        support.addDeletionProcessor( processor );
        support.notifyWillBeDeleted( "daObject3" );
      }

      @Override
      protected void verifyMocks() throws Exception {
        verify( processor ).willBeDeleted( "daObject" );
        verify( processor ).willBeDeleted( "daObject3" );
      }
    }.run();
  }

}
