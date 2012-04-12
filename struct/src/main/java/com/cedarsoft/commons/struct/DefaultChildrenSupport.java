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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Default implementation of {@link ChildrenSupport}
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class DefaultChildrenSupport implements ChildrenSupport {
  private Node parentNode;

  private List<Node> nodes = new ArrayList<Node>();
  private List<Node> nodesUm = Collections.unmodifiableList( nodes );

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<? extends Node> getChildren() {
    //noinspection ReturnOfCollectionOrArrayField
    return nodesUm;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addChild( int index, @Nonnull Node child ) {
    validateName( child.getName() );

    nodes.add( index, child );
    child.setParent( getParentNode() );
    notifyChildAdded( child, index );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addChild( @Nonnull Node child ) {
    addChild( nodes.size(), child );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setChildren( @Nonnull List<? extends Node> children ) {
    detachChildren();
    for ( Node child : children ) {
      addChild( child );
    }
  }

  /**
   * {@inheritDoc}
   * <p/>
   * Detaches all children
   */
  @Override
  public void detachChildren() {
    if ( !nodes.isEmpty() ) {
      for ( Node node : new ArrayList<Node>( nodes ) ) {
        detachChild( node );
      }
    }
  }

  private void validateName( @Nonnull String name ) {
    for ( Node node : nodes ) {
      if ( node.getName().equals( name ) ) {
        throw new IllegalArgumentException( "There still exists a node with the name " + name );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void detachChild( @Nonnull Node child ) {
    int oldIndex = nodes.indexOf( child );
    if ( oldIndex < 0 ) {
      throw new IllegalArgumentException( "Invalid child. " + child );
    }
    detachChild( oldIndex, child );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void detachChild( int index ) {
    detachChild( index, nodes.get( index ) );
  }

  private void detachChild( int index, @Nonnull Node childToDetach ) {
    nodes.remove( index );
    childToDetach.setParent( null );
    notifyChildDetached( childToDetach, index );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Node getParentNode() {
    return parentNode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setParentNode( @Nonnull Node parentNode ) {
    this.parentNode = parentNode;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isChild( @Nonnull StructPart child ) {
    //noinspection SuspiciousMethodCalls
    return nodes.contains( child );
  }

  private final List<StructureListener> structureListeners = new ArrayList<StructureListener>();

  /**
   * {@inheritDoc}
   */
  @Override
  public void addStructureListener( @Nonnull StructureListener structureListener ) {
    structureListeners.add( structureListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addStructureListenerWeak( @Nonnull StructureListener structureListener ) {
    structureListeners.add( new WeakStructureListener( structureListener ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeStructureListener( @Nonnull StructureListener structureListener ) {
    for ( Iterator<StructureListener> it = structureListeners.iterator(); it.hasNext(); ) {
      StructureListener listener = it.next();
      if ( listener == structureListener || listener instanceof WeakStructureListener && ( ( WeakStructureListener ) listener ).getWrappedListener() == structureListener ) {
        it.remove();
      }
    }
    structureListeners.remove( structureListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<? extends StructureListener> getStructureListeners() {
    return Collections.unmodifiableList( structureListeners );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Node findChild( @Nonnull String childName ) throws ChildNotFoundException {
    for ( Node node : nodes ) {
      if ( node.getName().equals( childName ) ) {
        return node;
      }
    }
    throw new ChildNotFoundException( new Path( childName ) );
  }

  private void notifyChildAdded( @Nonnull Node child, int index ) {
    StructureChangedEvent event = new StructureChangedEvent( getParentNode(), StructureChangedEvent.Type.Add, child, index );
    for ( StructureListener listener : new ArrayList<StructureListener>( structureListeners ) ) {
      listener.childAdded( event );
    }
  }

  private void notifyChildDetached( @Nonnull Node child, int index ) {
    StructureChangedEvent event = new StructureChangedEvent( getParentNode(), StructureChangedEvent.Type.Remove, child, index );
    for ( StructureListener listener : new ArrayList<StructureListener>( structureListeners ) ) {
      listener.childDetached( event );
    }
  }
}
