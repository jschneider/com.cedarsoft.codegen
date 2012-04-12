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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: 12.10.2006<br>
 * Time: 16:25:26<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class PathTest {
  @Test
  public void testSubPath() {
    Path path = new Path( "a", "b", "c", "d" );
    Path subPath = path.subPath( 0, 2 );
    assertEquals( new Path( "a", "b" ), subPath );
  }

  @Test
  public void testSerializeation() throws IOException, ClassNotFoundException {
    Path path = new Path( "a", "b" );
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    new ObjectOutputStream( out ).writeObject( path );

    Object read = new ObjectInputStream( new ByteArrayInputStream( out.toByteArray() ) ).readObject();
    assertEquals( path, read );
  }

  @Test
  public void testEmptyPath() {
    assertEquals( "", Path.EMPTY.toString() );
    assertEquals( "/", Path.EMPTY.absolute().toString() );
    assertFalse( Path.EMPTY.isAbsolute() );

    assertEquals( "", Path.createPath( "" ).toString() );
    assertEquals( Path.EMPTY, Path.createPath( "" ) );
    assertEquals( "/", Path.createPath( "/" ).toString() );
    assertTrue( Path.createPath( "/" ).isAbsolute() );
    assertEquals( 0, Path.createPath( "/" ).size() );
  }

  @Test
  public void testPop() {
    Path path = new Path( "a", "b" );
    assertEquals( "a/b", path.toString() );
    assertEquals( "b", path.popped().toString() );
    assertEquals( "a/b", path.toString() );
  }

  @Test
  public void testPopAbs() {
    Path path = new Path( Arrays.asList( "a", "b", "c" ), true );

    assertEquals( "/a/b/c", path.toString() );
    assertEquals( "b/c", path.popped().toString() );
    assertEquals( "/a/b/c", path.toString() );
  }

  @Test
  public void testAbsolute() {
    Path path = new Path( "a", "b" );
    assertEquals( "a/b", path.toString() );

    assertEquals( "/a/b", path.absolute().toString() );
  }

  @Test
  public void testCreateParent() {
    assertEquals( "ab/asdf/a/b", Path.createPath( "asdf/a/b" ).withParent( "ab" ).toString() );

    //cannot create path for root
    try {
      Path child = Path.createPath( "/asdf/a/b" );
      assertTrue( child.isAbsolute() );
      child.withParent( "ab" );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }

    assertEquals( "/ab/asdf/a/b", Path.createPath( "asdf/a/b" ).withParent( "/ab" ).toString() );
  }

  @Test
  public void testCreateChildPath() {
    Path path = Path.createPath( "/asdf/a/b" );
    Path child = path.withChild( "c" );
    assertEquals( "asdf/a/b/c", child.toString() );
  }

  @Test
  public void testElements() {
    List<Node> nodes = new ArrayList<Node>();
    nodes.add( new DefaultNode( "a" ) );
    nodes.add( new DefaultNode( "b" ) );
    nodes.add( new DefaultNode( "c" ) );

    assertEquals( "a/b/c", Route.buildRoute( nodes ).toString() );
  }

  @Test
  public void testPathElements() {
    Path path = Path.createPath( "/asdf/a/b" );
    List<? extends String> elements = path.getElements();
    assertEquals( 3, elements.size() );
    assertEquals( "asdf", elements.get( 0 ) );
    assertEquals( "a", elements.get( 1 ) );
    assertEquals( "b", elements.get( 2 ) );
  }

  @Test
  public void testEquals() {
    assertEquals( Path.createPath( "/" ), Path.createPath( "/" ) );
    assertEquals( Path.createPath( "/asdf" ), Path.createPath( "/asdf" ) );
    assertEquals( Path.createPath( "/a/b" ), Path.createPath( "/a/b" ) );
    assertEquals( Path.createPath( "/a/" ), Path.createPath( "/a" ) );
  }

  @Test
  public void testStringSplit() {
    assertEquals( 0, "/".split( "/" ).length );
    assertEquals( 0, "/".split( "/" ).length );
  }

  @Test
  public void testPath() {
    Path path = Path.createPath( "/" );
    assertTrue( path.getElements().isEmpty() );
  }

  @Test
  public void testBuildPaths() {
    Node parent = new DefaultNode( "a" );
    Node child0 = new DefaultNode( "child0" );
    parent.addChild( child0 );

    Node child1 = new DefaultNode( "child1" );
    child0.addChild( child1 );

    Path path = Path.buildPath( child1 );
    assertEquals( "a/child0/child1", path.toString() );
  }

  @Test
  public void testPathToString() {
    Path path = new Path( "a", "b", "c" );
    assertEquals( "a/b/c", path.toString() );
  }

  @Test
  public void testEquals2() {
    assertEquals( new Path( "a", "b", "c" ), new Path( "a", "b", "c" ) );
    assertEquals( new Path( "a", "b", "c" ), new Path( "a", "b", "c" ) );
    assertFalse( new Path( "a", "b", "c" ).equals( new Path( "a", "b" ) ) );
  }

  @Test
  public void testCreatePath() {
    Path path = Path.createPath( "/a/b/c" );
    assertTrue( path.isAbsolute() );
    assertNotNull( path );
    assertEquals( 3, path.getElements().size() );
    assertEquals( "/a/b/c", path.toString() );
  }

  @Test
  public void testCreatePath2() {
    assertEquals( "/a/b/c", Path.createPath( "/a/b/c" ).toString() );
    assertEquals( "a/b/c", Path.createPath( "a/b/c" ).toString() );
    assertEquals( "a", Path.createPath( "a" ).toString() );

    assertEquals( "/a", Path.createPath( "/a/" ).toString() );
    assertEquals( "/a/b/c", Path.createPath( "/a/b/c/" ).toString() );

    assertEquals( "", Path.createPath( "" ).toString() );

    try {
      Path.createPath( "/a///" );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }
  }

  @Test
  public void testToStringAbsolute() {
    assertEquals( "/a/b", Path.createPath( "/a/b" ).toString() );
    assertTrue( Path.createPath( "/a/b" ).isAbsolute() );
    assertEquals( "a/b", Path.createPath( "a/b" ).toString() );
    assertFalse( Path.createPath( "a/b" ).isAbsolute() );
  }
}
