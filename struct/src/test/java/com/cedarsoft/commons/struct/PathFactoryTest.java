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

import javax.annotation.Nonnull;
import org.junit.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import static org.junit.Assert.*;

/**
 *
 */
public class PathFactoryTest {

  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testCreateParent() {
    assertEquals( "/a/b", Path.createParentPath( "/a", Path.createPath( "b" ) ).toString() );
    assertEquals( "a/b", Path.createParentPath( "a", Path.createPath( "b" ) ).toString() );

    try {
      Path.createParentPath( "a", Path.createPath( "/b" ) );
      fail( "Where is the Exception" );
    } catch ( IllegalArgumentException ignore ) {
    }
  }

  @Test
  public void testLevel() {
    DefaultNode root = new DefaultNode( "root" );
    DefaultNode child = new DefaultNode( "child" );
    root.addChild( child );
    DefaultNode childChild = new DefaultNode( "childChild" );
    child.addChild( childChild );

    assertEquals( 0, Path.calculateLevel( root, root ) );
    assertEquals( 0, Path.calculateLevel( child, child ) );
    assertEquals( 1, Path.calculateLevel( root, child ) );
    assertEquals( 2, Path.calculateLevel( root, childChild ) );

    try {
      Path.calculateLevel( root, new DefaultNode( "otherChild" ) );
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
    }

    try {
      Path.calculateLevel( childChild, root );
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
    }
  }

  @Test
  public void testAbsolute() {
    assertTrue( Path.createPath( "/a" ).isAbsolute() );
    assertFalse( Path.createPath( "a" ).isAbsolute() );
  }

  @Test
  public void testEmpty() {
    try {
      Path.createPath( "a//" );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }
  }

  @Test
  public void testValidator() {
    Node root = new DefaultNode( "0" );
    Node child = new DefaultNode( "1" );
    Node childChild = new DefaultNode( "2" );

    root.addChild( child );
    child.addChild( childChild );

    final Queue<Node> expected = new LinkedList<Node>( Arrays.asList( childChild, child, root ) );

    assertEquals( "0/1/2", Path.buildPath( childChild ).toString() );
    assertEquals( "0/1/2", Path.buildPath( childChild, new PathValidator() {
      @Override
      public void validate( @Nonnull Path path ) throws ValidationFailedException {
        assertFalse( path.isAbsolute() );
      }
    }
    ).toString() );
  }
}
