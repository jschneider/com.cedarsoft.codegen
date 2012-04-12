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

import java.util.List;

/**
 * <p>ChildrenSupport interface.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public interface ChildrenSupport {
  /**
   * Returns the children
   *
   * @return the children
   */
  @Nonnull
  List<? extends Node> getChildren();

  /**
   * Adds a child
   *
   * @param child the child that is added
   */
  void addChild( @Nonnull Node child );

  /**
   * Adds a child
   *
   * @param index the index
   * @param child the child that is added
   */
  void addChild( int index, @Nonnull Node child );

  /**
   * Detaches a child
   *
   * @param child the child that is detached
   */
  void detachChild( @Nonnull Node child );

  /**
   * Detaches a child
   *
   * @param index the index
   */
  void detachChild( int index );

  /**
   * Returns the parent node
   *
   * @return the parent node
   */
  @Nonnull
  Node getParentNode();

  /**
   * Sets the parent node.
   * This method must be called before the ChildrenSupport may be used.
   *
   * @param parentNode the parent node.
   */
  void setParentNode( @Nonnull Node parentNode );

  /**
   * Whether the given child is managed by this ChildrenSupport
   *
   * @param child the child
   * @return whether the given child is managed by this children support
   */
  boolean isChild( @Nonnull StructPart child );

  /**
   * Adds a structure listener
   *
   * @param structureListener the listener
   */
  void addStructureListener( @Nonnull StructureListener structureListener );

  /**
   * Adds a structure listener (wrapped within a {@link WeakStructureListener})
   *
   * @param structureListener the listener
   */
  void addStructureListenerWeak( @Nonnull StructureListener structureListener );

  /**
   * Removes a structure listener
   *
   * @param structureListener the listener
   */
  void removeStructureListener( @Nonnull StructureListener structureListener );

  /**
   * Finds the child with the given name
   *
   * @param childName the name of the child
   * @return the child
   *
   * @throws ChildNotFoundException
   *          if no child with that name is found
   */
  @Nonnull
  Node findChild( @Nonnull String childName ) throws ChildNotFoundException;

  /**
   * Sets the children
   *
   * @param children the children
   */
  void setChildren( @Nonnull List<? extends Node> children );

  /**
   * Detaches all children
   */
  void detachChildren();

  /**
   * <p>getStructureListeners</p>
   *
   * @return a {@link List} object.
   */
  @Nonnull
  List<? extends StructureListener> getStructureListeners();
}
