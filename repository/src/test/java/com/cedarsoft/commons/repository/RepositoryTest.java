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

package com.cedarsoft.commons.repository;

import com.cedarsoft.commons.struct.ChildNotFoundException;
import com.cedarsoft.commons.struct.DefaultNode;
import com.cedarsoft.commons.struct.Node;
import com.cedarsoft.commons.struct.Path;
import org.junit.*;

import static org.junit.Assert.*;

/**
 */
public class RepositoryTest {
  private Repository repository;

  @Before
  public void setUp() throws Exception {
    repository = new Repository();
  }

  @Test
  public void testPathNodeAbsolute() {
    Node parent = new DefaultNode( "a" );
    Node child0 = new DefaultNode( "child0" );
    parent.addChild( child0 );

    repository.getRootNode().addChild( parent );
    assertEquals( "/a/child0", repository.getPath( child0 ).toString() );
  }

  @Test
  public void testGetRepository() {
    assertTrue( repository.isRoot( repository.getRootNode() ) );
    assertFalse( repository.getRootNode().hasParent() );
  }

  @Test
  public void testRoot() {
    Node root = repository.getRootNode();
    assertTrue( repository.isRoot( root ) );
    assertFalse( repository.isRoot( new DefaultNode( "asdf" ) ) );
  }

  @Test
  public void testRootNode() {
    Node root = repository.getRootNode();
    assertSame( root, repository.getNode( Path.createPath( "/" ) ) );

    assertNotNull( root );
    assertEquals( "", root.getName() );
    assertEquals( "/", repository.getPath( root ).toString() );
  }

  @Test
  public void testAbsolute() {
    Node root = repository.getRootNode();
    assertTrue( repository.getPath( root ).isAbsolute() );

    DefaultNode child = new DefaultNode( "child" );
    root.addChild( child );
    assertTrue( repository.getPath( child ).isAbsolute() );
  }

  @Test
  public void testAddChildrenRoot() {
    DefaultNode child = new DefaultNode( "daChild" );
    repository.getRootNode().addChild( child );
    assertEquals( "/daChild", repository.getPath( child ).toString() );
  }

  @Test
  public void testFindNode() throws ChildNotFoundException {
    repository.getRootNode().addChild( new DefaultNode( "asdf" ) );
    Node node = repository.findNode( Path.createPath( "/asdf" ) );
    assertNotNull( node );
    assertEquals( "asdf", node.getName() );
  }

  @Test
  public void testSilentCreation() {
    Path path = Path.createPath( "/home/schneide" );
    try {
      repository.findNode( path );
      fail( "Where is the Exception" );
    } catch ( ChildNotFoundException ignore ) {
    }

    Node node = repository.getNode( path );
    assertNotNull( node );
    assertEquals( "/home/schneide", repository.getPath( node ).toString() );

    assertEquals( path, node.getPath() );
  }
}
