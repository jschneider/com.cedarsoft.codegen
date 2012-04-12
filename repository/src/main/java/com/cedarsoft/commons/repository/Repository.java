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

import javax.annotation.Nonnull;

/**
 * A repository is a treelike structure where informations may be saved within nodes.
 * <p/>
 * The repository stores Nodes within an absolute structure.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class Repository {
  private final Node rootNode;

  /**
   * Creates a new repository
   */
  public Repository() {
    //noinspection RefusedBequest
    this.rootNode = new DefaultNode( "" );
  }

  /**
   * Returns the root node of the repository
   *
   * @return the root node
   */
  @Nonnull
  public Node getRootNode() {
    return rootNode;
  }

  /**
   * Returns the node for the given absolute path
   *
   * @param path the path (must be absolute)
   * @return the node
   *
   * @throws ChildNotFoundException
   *          if the path could not be resolved
   */
  @Nonnull
  public Node findNode( @Nonnull Path path ) throws ChildNotFoundException {
    if ( !path.isAbsolute() ) {
      throw new IllegalArgumentException( "Path must be absolute to be resolved within the repository" );
    }

    Node current = getRootNode();
    for ( String element : path.getElements() ) {
      current = findChild( current, element );
    }
    return current;
  }

  /**
   * Finds a child for the given name
   *
   * @param parent the parent node
   * @param name   the name
   * @return the found node
   *
   * @throws ChildNotFoundException
   *          if the child could not be found
   */
  @Nonnull
  protected Node findChild( @Nonnull Node parent, @Nonnull String name ) throws ChildNotFoundException {
    for ( Node child : parent.getChildren() ) {
      if ( child.getName().equals( name ) ) {
        return child;
      }
    }
    throw new ChildNotFoundException( parent.getPath().withChild( name ) );
  }

  /**
   * Get or create the node
   *
   * @param path the path
   * @return the node
   */
  @Nonnull
  public Node getNode( @Nonnull Path path ) {
    Node current = getRootNode();
    for ( String element : path.getElements() ) {
      try {
        current = findChild( current, element );
      } catch ( ChildNotFoundException ignore ) {
        DefaultNode created = new DefaultNode( element );
        current.addChild( created );
        current = created;
      }
    }
    return current;
  }

  /**
   * Returns true if the given node is the root node of the repository
   *
   * @param node the node
   * @return whether the given node is the root node of the the repository
   */
  public boolean isRoot( @Nonnull Node node ) {
    //noinspection ObjectEquality
    return rootNode == node;
  }

  /**
   * Returns the path for the given node
   *
   * @param node the node
   * @return the path for the node
   */
  @Nonnull
  public Path getPath( @Nonnull Node node ) {
    return Path.buildPath( node ).absolute();
  }
}
