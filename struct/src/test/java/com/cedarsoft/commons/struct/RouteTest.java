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

import com.cedarsoft.lookup.Lookup;

import javax.annotation.Nonnull;
import org.junit.*;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 *
 */
public class RouteTest {
  private DefaultNode root;
  private DefaultNode nodeA;
  private DefaultNode nodeB;
  private DefaultNode nodeC;

  @Before
  public void setUp() throws Exception {
    root = new DefaultNode( "asdf" );
    nodeA = new DefaultNode( "a" );
    root.addChild( nodeA );
    nodeB = new DefaultNode( "b" );
    nodeA.addChild( nodeB );
    nodeC = new DefaultNode( "c" );
  }

  @Test
  public void testEmptyRoute() {
    Route route = new Route( Collections.<Node>emptyList() );
    assertEquals( 0, route.size() );
  }

  @Test
  public void testEmptyRoute2() {
    assertEquals( 1, Route.buildRoute( root, Path.EMPTY ).size() );

    try {
      Route.buildRoute( root, Path.EMPTY.absolute() );
      fail( "Where is the Exception" );
    } catch ( IllegalArgumentException ignore ) {
    }
  }

  @Test
  public void testInvalidRoot() {
    try {
      Route.buildRoute( new DefaultNode( "invalid" ), Path.createPath( "/asdf/a/b" ) );
      fail( "Where is the Exception" );
    } catch ( IllegalArgumentException ignore ) {
    }
  }

  @Test
  public void testCreate() {
    assertEquals( 1, root.getChildren().size() );

    Path path = Path.createPath( "z" );
    try {
      Route.buildRoute( root, path );
      fail( "Where is the Exception" );
    } catch ( ChildNotFoundException ignore ) {
    }

    Route route = Route.buildRoute( root, path, new NodeFactory() {
      @Override
      @Nonnull
      public Node createNode( @Nonnull String name, @Nonnull Lookup context ) {
        return new DefaultNode( name );
      }
    } );

    assertEquals( 2, route.size() );
    assertEquals( route.getNodes().get( 0 ), root );
    assertEquals( "z", route.getNodes().get( 1 ).getName() );
    assertEquals( 2, root.getChildren().size() );
  }

  @Test
  public void testRouteWithRelative() throws Exception {
    Route route = Route.buildRoute( root, Path.createPath( "a/b" ) );
    assertEquals( 3, route.size() );

    assertEquals( "b", route.getLastNode().getName() );
    assertEquals( "asdf", route.getNodes().get( 0 ).getName() );
    assertEquals( "a", route.getNodes().get( 1 ).getName() );
    assertEquals( "b", route.getNodes().get( 2 ).getName() );
  }

  @Test
  public void testCreateChildPath() throws ChildNotFoundException {
    Route route = Route.buildRoute( root, Path.createPath( "/asdf/a/b" ) );
    assertNotNull( route );

    assertEquals( 3, route.getNodes().size() );
    assertSame( root, route.getNodes().get( 0 ) );
    assertSame( nodeA, route.getNodes().get( 1 ) );
    assertSame( nodeB, route.getNodes().get( 2 ) );

    assertEquals( "asdf/a/b", route.getPath().toString() );
  }

  @Test
  public void testEquals() throws ChildNotFoundException {
    assertEquals( Route.buildRoute( root, Path.createPath( "/asdf" ) ), Route.buildRoute( root, Path.createPath( "/asdf" ) ) );
    assertEquals( Route.buildRoute( root, Path.createPath( "/asdf/a" ) ), Route.buildRoute( root, Path.createPath( "a" ) ) );
    assertEquals( Route.buildRoute( root, Path.createPath( "/asdf/a/b" ) ), Route.buildRoute( root, Path.createPath( "a/b" ) ) );

    assertEquals( Route.buildRoute( root, Path.createPath( "/asdf/a/b" ) ), Route.buildRoute( root, Path.createPath( "a/b" ) ) );
    assertEquals( Route.buildRoute( root, Path.createPath( "/asdf/a/b/" ) ), Route.buildRoute( root, Path.createPath( "a/b" ) ) );
  }
}
