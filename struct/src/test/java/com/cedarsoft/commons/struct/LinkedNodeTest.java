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

import org.junit.*;

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: May 25, 2007<br>
 * Time: 4:10:09 PM<br>
 */
public class LinkedNodeTest {
  private Node childChild;
  private Node childChildchild;
  private Node node;
  private Node child;

  @Before
  public void setUp() throws Exception {
    node = new DefaultNode( "asdf" );
    child = new DefaultNode( "child" );
    node.addChild( child );

    childChild = new DefaultNode( "childChild" );
    child.addChild( childChild );

    childChildchild = new DefaultNode( "childChildChild" );
    childChild.addChild( childChildchild );
  }

  @Test
  public void testSetup() {
    assertEquals( "asdf/child/childChild", childChild.getPath().toString() );
    assertEquals( "asdf/child/childChild/childChildChild", childChildchild.getPath().toString() );
  }

  @Test
  public void testClean() {
    LinkedNode linkedNode = new LinkedNode( childChild );
    node.addChild( linkedNode );

    assertEquals( 2, node.getChildren().size() );
    assertEquals( "asdf/child", node.getChildren().get( 0 ).getPath().toString() );
    Node actNode = node.getChildren().get( 1 );
    assertTrue( LinkedNode.isLinkedNode( actNode ) );
    assertEquals( "asdf/childChild", actNode.getPath().toString() );
    assertEquals( 1, actNode.getChildren().size() );
    assertEquals( "asdf/child/childChild/childChildChild", actNode.getChildren().get( 0 ).getPath().toString() );
  }

  @Test
  public void testNo() {
    assertFalse( LinkedNode.isLinkedNode( new DefaultNode( "asdf" ) ) );
  }

  @Test
  public void testParent() {
    LinkedNode linkedNode = new LinkedNode( childChild );
    assertFalse( linkedNode.hasParent() );
    linkedNode.setParent( new DefaultNode( "parent" ) );
    assertTrue( linkedNode.hasParent() );
    linkedNode.setParent( null );
    assertFalse( linkedNode.hasParent() );
  }

  @Test
  public void testAddChild() {
    LinkedNode linkedNode = new LinkedNode( childChildchild );
    node.addChild( linkedNode );

    assertEquals( 0, linkedNode.getChildren().size() );
    assertEquals( 0, childChildchild.getChildren().size() );

    linkedNode.addChild( new DefaultNode( "anotherChild" ) );
    assertEquals( 1, linkedNode.getChildren().size() );
    assertEquals( 1, childChildchild.getChildren().size() );

    childChildchild.addChild( new DefaultNode( "anotherchild2") );
    assertEquals( 2, linkedNode.getChildren().size() );
    assertEquals( 2, childChildchild.getChildren().size() );

    linkedNode.detachChild( 1 );
    assertEquals( 1, linkedNode.getChildren().size() );
    assertEquals( 1, childChildchild.getChildren().size() );
    childChildchild.detachChild( 0 );
    assertEquals( 0, linkedNode.getChildren().size() );
    assertEquals( 0, childChildchild.getChildren().size() );
  }
}
