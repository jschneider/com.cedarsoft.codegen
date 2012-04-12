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

package com.cedarsoft.commons.struct;

import com.cedarsoft.lookup.Lookups;
import javax.annotation.Nonnull;
import org.junit.*;
import org.junit.rules.*;

import static org.junit.Assert.*;

/**
 *
 */
public class LeafNodeTest {
  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  private Node node;

  @Before
  public void setUp() throws Exception {
    node = new LeafNode( "name", Lookups.emtyLookup() );
  }

  @Test
  public void testBasic() {
    assertNotNull( node.getLookup() );
    assertEquals( 0, node.getChildren().size() );
    assertNull( node.getParent() );
    assertEquals( "name", node.getName() );
    assertFalse( node.isChild( new DefaultNode( "asdf" ) ) );
    assertFalse( node.hasParent() );
  }

  @Test
  public void testParent() {
    assertFalse( node.hasParent() );
    node.setParent( new DefaultNode( "parent" ) );
    assertTrue( node.hasParent() );
  }

  @Test
  public void testAddChild() {
    expectedException.expect( UnsupportedOperationException.class );
    node.addChild( new DefaultNode( "asdf" ) );
  }

  @Test
  public void testAddChild1() {
    expectedException.expect( UnsupportedOperationException.class );
    node.addChild( 0, new DefaultNode( "asdf" ) );
  }

  @Test
  public void testDet() {
    expectedException.expect( UnsupportedOperationException.class );
    node.detachChild( 0 );
  }

  @Test
  public void testFind() {
    expectedException.expect( UnsupportedOperationException.class );
    node.findChild( "asdf" );
  }

  @Test
  public void testDetach2() {
    expectedException.expect( UnsupportedOperationException.class );
    node.detachChild( new DefaultNode( "asdf" ) );
  }

  @Test
  public void testListeners() {
    StructureListener listener = new StructureListener() {
      @Override
      public void childAdded( @Nonnull StructureChangedEvent event ) {
      }

      @Override
      public void childDetached( @Nonnull StructureChangedEvent event ) {
      }
    };
    node.removeStructureListener( listener );

    node.addStructureListener( listener );
    node.addStructureListener( listener );

    node.removeStructureListener( listener );
    node.removeStructureListener( listener );

  }
}
