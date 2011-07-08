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

package com.cedarsoft.history;

import com.cedarsoft.test.utils.EasyMockTemplate;
import org.easymock.classextension.EasyMock;
import javax.annotation.Nonnull;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class ElementsCollectionTest {
  @Test
  public void testMultiRemove() {
    ElementsCollection<Integer> collection = new ElementsCollection<Integer>( 0, 1, 2, 3, 4, 5, 6, 7 );
    assertEquals( 8, collection.size() );

    List<? extends Integer> removed = collection.removeElements( new ElementVisitor<Integer>( "" ) {
      @Override
      public boolean fits( @Nonnull Integer element ) {
        return true;
      }
    } );

    assertEquals( 8, removed.size() );
  }

  @Test
  public void testMultiRemoveListener() throws Exception {
    final ElementsCollection<Integer> collection = new ElementsCollection<Integer>( 0, 1, 2, 3, 4, 5, 6, 7 );
    assertEquals( 8, collection.size() );

    final SingleElementsListener<Integer> listener = EasyMock.createMock( SingleElementsListener.class, SingleElementsListener.class.getMethod( "elementDeleted", ObservableCollection.class, Object.class, Integer.TYPE ) );

    new EasyMockTemplate( listener ) {
      @Override
      protected void expectations() {
        listener.elementDeleted( collection, 0, 0 );
        listener.elementDeleted( collection, 1, 0 );
        listener.elementDeleted( collection, 2, 0 );
        listener.elementDeleted( collection, 3, 0 );
        listener.elementDeleted( collection, 4, 0 );
        listener.elementDeleted( collection, 5, 0 );
        listener.elementDeleted( collection, 6, 0 );
        listener.elementDeleted( collection, 7, 0 );
      }

      @Override
      protected void codeToTest() {
        collection.addElementListener( listener );

        assertEquals( 8, collection.removeElements( new ElementVisitor<Integer>( "" ) {
          @Override
          public boolean fits( @Nonnull Integer element ) {
            return true;
          }
        } ).size() );
      }
    }.run();
  }
}
