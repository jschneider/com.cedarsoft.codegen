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

package com.cedarsoft.registry.cache;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.lang.Integer;
import java.lang.Object;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Stores the parent of children (and their indicies) in weak maps.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ParentCache {
  /**
   * The child is the key, the parent the value
   */
  @Nonnull
  private final Map<Object, WeakReference<Object>> parents = new WeakHashMap<Object, WeakReference<Object>>();
  /**
   * Contains the inidices of the child
   */
  @Nonnull
  private final Map<Object, Integer> indicies = new WeakHashMap<Object, Integer>();


  /**
   * Returns the parent of the given child
   *
   * @param child the child
   * @param <P>   a P object.
   * @param <C>   a C object.
   * @return the parent
   */
  @Nullable
  public <P, C> P findParent( @Nonnull C child ) {
    WeakReference<Object> reference = parents.get( child );
    if ( reference == null ) {
      return null;
    } else {
      return ( P ) reference.get();
    }
  }

  private void removeParent( @Nonnull Object child ) {
    parents.remove( child );
  }

  private void removeIndex( @Nonnull Object child ) {
    indicies.remove( child );
  }

  /**
   * <p>remove</p>
   *
   * @param child a {@link Object} object.
   */
  public void remove( @Nonnull Object child ) {
    removeParent( child );
    removeIndex( child );
  }

  /**
   * <p>removeAll</p>
   *
   * @param children a {@link List} object.
   */
  public void removeAll( @Nonnull List<? extends Object> children ) {
    for ( Object child : children ) {
      remove( child );
    }
  }

  private <P, C> void storeParent( @Nonnull C child, @Nonnull P parent ) {
    Object oldParent = findParent( child );
    if ( oldParent != null ) {
      if ( parent == oldParent ) {
        return;
      } else {
        throw new IllegalStateException( "Child <" + child + "> still has a parent: <" + oldParent + ">. New parent is: <" + parent + '>' );
      }
    }
    parents.put( child, new WeakReference<Object>( parent ) );
  }

  /**
   * Stores a parent
   *
   * @param child  the child
   * @param parent the parent
   * @param index  a int.
   * @param <P>    a P object.
   * @param <C>    a C object.
   */
  public <P, C> void store( @Nonnull C child, @Nonnull P parent, int index ) {
    storeParent( child, parent );
    storeIndex( child, index );
  }


  /**
   * <p>storeIndex</p>
   *
   * @param child a C object.
   * @param index a int.
   */
  public <C> void storeIndex( @Nonnull C child, int index ) {
    indicies.put( child, index );
  }

  /**
   * <p>findIndex</p>
   *
   * @param child a C object.
   * @return a {@link Integer} object.
   */
  @Nullable
  public <C> Integer findIndex( @Nonnull C child ) {
    return indicies.get( child );
  }

  /**
   * Returns a list containing all predecessors for the given child.
   * The first entry is the root - the last the parent of the child
   *
   * @param child the child
   * @return all predecessors for the given child
   */
  @Nonnull
  public List<? extends Object> getPredecessors( @Nonnull Object child ) {
    List<Object> list = new ArrayList<Object>();

    Object parent = findParent( child );
    while ( parent != null ) {
      list.add( parent );
      parent = findParent( parent );
    }

    Collections.reverse( list );
    return list;
  }

  /**
   * Returns the path (child an all its predecessors).
   * The first entry is the root - the last the child itself.
   *
   * @param child the child
   * @return a list containing the child and all its predecessors
   */
  @Nonnull
  public List<? extends Object> getPath( @Nonnull Object child ) {
    List<Object> path = new ArrayList<Object>( getPredecessors( child ) );
    path.add( child );
    return path;
  }

  /**
   * Stores several children
   *
   * @param parent   the parent
   * @param children the children
   */
  public void store( @Nonnull Object parent, @Nonnull List<? extends Object> children ) {
    for ( int index = 0; index < children.size(); index++ ) {
      Object child = children.get( index );
      store( child, parent, index );
    }
  }
}
