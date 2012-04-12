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
import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents a path of several nodes.
 * A path is a string representation.
 * <p/>
 * If you want to get a "list" of nodes try {@link Route} instead.
 * <p/>
 * A path is *immutable*
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class Path implements Serializable {
  private static final long serialVersionUID = -201844652320294645L;
  /**
   * Constant <code>PATH_SEPARATOR_CHAR='/'</code>
   */
  public static final char PATH_SEPARATOR_CHAR = '/';
  /**
   * Constant <code>PATH_SEPARATOR="String.valueOf( PATH_SEPARATOR_CHAR )"</code>
   */
  @Nonnull
  public static final String PATH_SEPARATOR = String.valueOf( PATH_SEPARATOR_CHAR );
  /**
   * Constant <code>EMPTY</code>
   */
  @Nonnull
  public static final Path EMPTY = new Path();

  private final boolean absolute;
  @Nonnull
  private final List<String> elements = new ArrayList<String>();

  /**
   * Creates a new path
   *
   * @param elements the elements
   */
  public Path( @Nonnull String... elements ) {
    this( Arrays.asList( elements ) );
  }

  /**
   * <p>Constructor for Path.</p>
   *
   * @param relative a boolean.
   * @param elements a {@link String} object.
   */
  @Deprecated
  public Path( boolean relative, @Nonnull String... elements ) {
    this( Arrays.asList( elements ), relative );
  }

  /**
   * Creates a new *relative* path
   *
   * @param elements the elements
   */
  public Path( @Nonnull Iterable<? extends String> elements ) {
    this( elements, false );
  }

  /**
   * <p>Constructor for Path.</p>
   *
   * @param elements a {@link Iterable} object.
   * @param absolute a boolean.
   */
  public Path( @Nonnull Iterable<? extends String> elements, boolean absolute ) {
    this.absolute = absolute;

    for ( String element : elements ) {
      if ( element.length() == 0 ) {
        continue;
      }

      if ( element.contains( PATH_SEPARATOR ) ) {
        throw new IllegalArgumentException( "Invalid character (" + PATH_SEPARATOR + ") in <" + element + '>' );
      }

      this.elements.add( element );
    }
  }

  /**
   * Return a list containing all path elements
   *
   * @return a list containing all path elements
   */
  @Nonnull
  public List<? extends String> getElements() {
    return Collections.unmodifiableList( elements );
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  public String toString() {
    StringBuilder buffer = new StringBuilder();
    if ( isAbsolute() ) {
      buffer.append( PATH_SEPARATOR_CHAR );
    }

    for ( String element : elements ) {
      buffer.append( element );
      buffer.append( PATH_SEPARATOR );
    }

    if ( buffer.length() > 1 ) {
      buffer.deleteCharAt( buffer.length() - 1 );
    }

    return buffer.toString();
  }

  /**
   * <p>size</p>
   *
   * @return a int.
   */
  public int size() {
    return elements.size();
  }

  /**
   * <p>isAbsolute</p>
   *
   * @return a boolean.
   */
  public boolean isAbsolute() {
    return absolute;
  }

  /**
   * <p>absolute</p>
   *
   * @return a {@link Path} object.
   */
  @Nonnull
  public Path absolute() {
    return absolute( true );
  }

  /**
   * <p>relative</p>
   *
   * @return a {@link Path} object.
   */
  @Nonnull
  public Path relative() {
    return absolute( false );
  }

  /**
   * <p>absolute</p>
   *
   * @param newAbsolute a boolean.
   * @return a {@link Path} object.
   */
  @Nonnull
  public Path absolute( boolean newAbsolute ) {
    if ( absolute == newAbsolute ) {
      return this;
    }
    return new Path( elements, newAbsolute );
  }

  /**
   * <p>subPath</p>
   *
   * @param index0 a int.
   * @param index1 a int.
   * @return a {@link Path} object.
   */
  @Nonnull
  public Path subPath( int index0, int index1 ) {
    return new Path( elements.subList( index0, index1 ) );
  }

  /**
   * Creates a new path where the first element has been removed
   *
   * @return a new path without the first element
   */
  @Nonnull
  public Path popped() {
    if ( elements.isEmpty() ) {
      throw new ArrayIndexOutOfBoundsException( "No elements left to pop" );
    }
    return subPath( 1, elements.size() );
  }

  /**
   * <p>withParent</p>
   *
   * @param parentPathRepresentation a {@link String} object.
   * @return a {@link Path} object.
   */
  @Nonnull
  public Path withParent( @Nonnull String parentPathRepresentation ) {
    return createParentPath( parentPathRepresentation, this );
  }

  /**
   * Creates a new path that represents the child with the given name
   *
   * @param childName the name of the child
   * @return the path of the child
   */
  public Path withChild( @Nonnull String childName ) {
    List<String> childElements = new ArrayList<String>( elements.size() + 1 );
    childElements.addAll( elements );
    childElements.add( childName );
    return new Path( childElements );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;

    Path path = ( Path ) o;

    if ( elements != null ? !elements.equals( path.elements ) : path.elements != null ) return false;

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    return elements != null ? elements.hashCode() : 0;
  }

  /**
   * Create a path object for a given path representation
   *
   * @param pathRepresentation the path representation
   * @return the path
   */
  @Nonnull
  public static Path createPath( @Nonnull String pathRepresentation ) {
    boolean isAbsolute = false;
    if ( pathRepresentation.startsWith( PATH_SEPARATOR ) ) {
      //noinspection AssignmentToMethodParameter
      pathRepresentation = pathRepresentation.substring( 1 );
      isAbsolute = true;
    }

    List<String> parts = new ArrayList<String>();

    StringBuilder actualName = new StringBuilder();
    for ( char actualChar : pathRepresentation.toCharArray() ) {
      if ( actualChar == PATH_SEPARATOR_CHAR ) {
        if ( actualName.length() == 0 ) {
          throw new IllegalArgumentException( "Name must not be empty: " + pathRepresentation );
        }

        parts.add( actualName.toString() );
        actualName = new StringBuilder();
      } else {
        actualName.append( actualChar );
      }
    }

    parts.add( actualName.toString() );

    return new Path( parts, isAbsolute );
  }

  /**
   * Creates a path for a node
   *
   * @param node the node
   * @return the path for the given node
   */
  @Nonnull
  public static Path buildPath( @Nonnull StructPart node ) {
    return buildPath( node, null );
  }

  /**
   * Creates a path for a node
   *
   * @param node      the node
   * @param validator the validator
   * @return the path for the given node
   */
  @Nonnull
  public static Path buildPath( @Nonnull StructPart node, @Nullable PathValidator validator ) {
    List<String> elements = new ArrayList<String>();

    //add parent node
    elements.add( 0, node.getName() );

    StructPart actualNode = node;
    while ( ( actualNode = actualNode.getParent() ) != null ) {
      elements.add( 0, actualNode.getName() );
    }
    Path path = new Path( elements );
    if ( validator != null ) {
      validator.validate( path );
    }
    return path;
  }

  /**
   * Creates a parent path
   *
   * @param parentPathRepresentation the string representation of the path of the parent
   * @param child                    the child path
   * @return the path for the parent string representation and the child
   */
  @Nonnull
  public static Path createParentPath( @Nonnull String parentPathRepresentation, @Nonnull Path child ) {
    if ( child.isAbsolute() ) {
      throw new IllegalArgumentException( "Cannot create parent path for absolute child path" );
    }
    Path parentPath = createPath( parentPathRepresentation );

    List<String> elements = new ArrayList<String>( parentPath.getElements() );
    elements.addAll( child.getElements() );

    return new Path( elements, parentPath.isAbsolute() );
  }

  /**
   * Calculates the level of the given node relative to the root.
   * If the node is a child of root, the returned level is "1".
   *
   * @param root the root
   * @param node the node
   * @return the level
   */
  public static int calculateLevel( @Nonnull StructPart root, @Nonnull StructPart node ) {
    if ( root.equals( node ) ) {
      return 0;
    }

    int level = 1;

    StructPart currentParent = node.getParent();
    while ( currentParent != null ) {
      if ( currentParent.equals( root ) ) {
        return level;
      }
      currentParent = currentParent.getParent();
      level++;
    }

    throw new IllegalArgumentException( "Root is not parent of node: " + root + " - " + node );
  }
}
