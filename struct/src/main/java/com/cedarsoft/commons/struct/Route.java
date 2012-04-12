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
import com.cedarsoft.lookup.Lookups;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.lang.IllegalStateException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Represents route of several nodes.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class Route {
  @Nonnull
  private static final Route EMPTY = new Route( Collections.<Node>emptyList() );
  @Nonnull
  private final List<Node> nodes = new ArrayList<Node>();

  /**
   * Creates a new route
   *
   * @param nodes the nodes
   */
  public Route( @Nonnull List<? extends Node> nodes ) {
    this.nodes.addAll( nodes );
  }

  /**
   * Builds a path for several nodes
   *
   * @param nodes the nodes
   * @return the path
   */
  @Nonnull
  public static Path buildRoute( @Nonnull List<? extends Node> nodes ) {
    List<String> elements = new ArrayList<String>();
    for ( Node node : nodes ) {
      elements.add( node.getName() );
    }
    return new Path( elements );
  }

  /**
   * Returns the last node of the route
   *
   * @return the last node
   *
   * @throws IllegalStateException
   *          if the route is empty
   */
  @Nonnull
  public Node getLastNode() throws IllegalStateException {
    if ( nodes.isEmpty() ) {
      throw new IllegalStateException( "Path has no nodes" );
    }
    return nodes.get( nodes.size() - 1 );
  }

  /**
   * Returns the nodes
   *
   * @return the nodes for this route
   */
  @Nonnull
  public List<? extends Node> getNodes() {
    return Collections.unmodifiableList( nodes );
  }

  /**
   * <p>size</p>
   *
   * @return a int.
   */
  public int size() {
    return nodes.size();
  }

  /**
   * Returns the path
   *
   * @return the path
   */
  @Nonnull
  public Path getPath() {
    List<String> elements = new ArrayList<String>();
    for ( Node node : nodes ) {
      elements.add( node.getName() );
    }
    return new Path( elements );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;

    Route route = ( Route ) o;

    if ( !nodes.equals( route.nodes ) ) return false;

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return nodes.hashCode();
  }

  /**
   * Build a route
   *
   * @param rootNode the root node
   * @param path     the path
   * @return the route
   *
   * @throws ChildNotFoundException
   *          if any.
   */
  @Nonnull
  public static Route buildRoute( @Nonnull Node rootNode, @Nonnull Path path ) throws ChildNotFoundException {
    return buildRouteInternal( rootNode, path, null );
  }

  /**
   * Builds the route. Creates and add all necessary nodes
   *
   * @param rootNode    the root
   * @param path        the path
   * @param nodeFactory the node factory that creates the nodes. The context will contain the parent node
   * @return the route
   */
  @Nonnull
  public static Route buildRoute( @Nonnull Node rootNode, @Nonnull Path path, @Nonnull NodeFactory nodeFactory ) {
    return buildRouteInternal( rootNode, path, nodeFactory );
  }

  /**
   * <p>buildRouteInternal</p>
   *
   * @param rootNode    a {@link Node} object.
   * @param path        a {@link Path} object.
   * @param nodeFactory a {@link NodeFactory} object.
   * @return a {@link Route} object.
   *
   * @throws ChildNotFoundException
   *          if any.
   */
  @Nonnull
  public static Route buildRouteInternal( @Nonnull Node rootNode, @Nonnull Path path, @Nullable NodeFactory nodeFactory ) throws ChildNotFoundException {
    Iterator<? extends String> iterator = path.getElements().iterator();
    //If the path is absolute, we have to verify the root node
    if ( path.isAbsolute() ) {
      if ( !iterator.hasNext() ) {
        throw new IllegalArgumentException( "Invalid path. Is absolute but does not have any element." );
      }

      String firstElement = iterator.next();
      if ( !rootNode.getName().equals( firstElement ) ) {
        throw new IllegalArgumentException( "Invalid root node. Expected \"" + path.getElements().get( 0 ) + "\" but was \"" + rootNode.getName() + "\"" );
      }
    }

    List<Node> nodes = new ArrayList<Node>();
    nodes.add( rootNode );
    while ( iterator.hasNext() ) {
      String element = iterator.next();
      Node lastNode = nodes.get( nodes.size() - 1 );
      try {
        nodes.add( lastNode.findChild( element ) );
      } catch ( ChildNotFoundException e ) {
        //Throw the exception if no node factory is available
        if ( nodeFactory == null ) {
          throw e;
        } else {
          try {
            Node created = nodeFactory.createNode( element, Lookups.singletonLookup( Node.class, lastNode ) );
            lastNode.addChild( created );
            nodes.add( created );
          } catch ( CanceledException ignore ) { //if the creation has been canceled
            throw e;
          }
        }
      }
    }

    return new Route( nodes );
  }
}
