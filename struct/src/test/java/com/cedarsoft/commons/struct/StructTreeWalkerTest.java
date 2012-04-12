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

import com.cedarsoft.exceptions.CanceledException;
import javax.annotation.Nonnull;

import org.junit.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class StructTreeWalkerTest {
  private Node root;

  @Before
  public void setUp() throws Exception {
    root = new DefaultNode( "0" );
    root.addChild( new DefaultNode( "00" ) );
    DefaultNode child = new DefaultNode( "01" );
    root.addChild( child );
    child.addChild( new DefaultNode( "010" ) );
    child.addChild( new DefaultNode( "011" ) );
    child.addChild( new DefaultNode( "012" ) );
    child.addChild( new DefaultNode( "013" ) );
    root.addChild( new DefaultNode( "02" ) );
  }

  @Test
  public void testSkipDepthFirst() {
    final List<String> expected = new ArrayList<String>( Arrays.asList( "0", "00", "02" ) );

    StructureTreeWalker walker = new DepthFirstStructureTreeWalker();

    walker.walk( root, new StructureTreeWalker.WalkerCallBack() {
      @Override
      public void nodeReached( @Nonnull StructPart node, int level ) {
        if ( node.getName().equals( "01" ) ) {
          throw new CanceledException();
        }

        assertSame( expected.remove( 0 ), node.getName() );
        assertEquals( node.getName().length() - 1, level );
      }
    } );

    assertTrue( expected.isEmpty() );
  }

  @Test
  public void testBreadthFirstSkip() {
    final List<String> expected = new ArrayList<String>( Arrays.asList( "0", "00", "02" ) );

    StructureTreeWalker walker = new BreadthFirstStructureTreeWalker();
    walker.walk( root, new StructureTreeWalker.WalkerCallBack() {
      @Override
      public void nodeReached( @Nonnull StructPart node, int level ) {
        if ( node.getName().equals( "01" ) ) {
          throw new CanceledException();
        }

        assertSame( expected.remove( 0 ), node.getName() );
        assertEquals( node.getName().length() - 1, level );
      }
    } );
    assertTrue( expected.isEmpty() );
  }


  @Test
  public void testDeepFirst() {
    final List<String> expected = new ArrayList<String>( Arrays.asList( "0", "00", "01", "010", "011", "012", "013", "02" ) );

    StructureTreeWalker walker = new DepthFirstStructureTreeWalker();
    walker.walk( root, new StructureTreeWalker.WalkerCallBack() {
      @Override
      public void nodeReached( @Nonnull StructPart node, int level ) {
        assertSame( expected.remove( 0 ), node.getName() );
        assertEquals( node.getName().length() - 1, level );
      }
    } );
    assertTrue( expected.isEmpty() );
  }

  @Test
  public void testBreadthFirst() {
    final List<String> expected = new ArrayList<String>( Arrays.asList( "0", "00", "01", "02", "010", "011", "012", "013" ) );

    StructureTreeWalker walker = new BreadthFirstStructureTreeWalker();
    walker.walk( root, new StructureTreeWalker.WalkerCallBack() {
      @Override
      public void nodeReached( @Nonnull StructPart node, int level ) {
        assertSame( expected.remove( 0 ), node.getName() );
        assertEquals( node.getName().length() - 1, level );
      }
    } );
    assertTrue( expected.isEmpty() );
  }
}
