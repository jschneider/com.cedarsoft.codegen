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
import javax.annotation.Nullable;

import java.util.List;

/**
 * A struct part is a read only view. For adding/removing children a {@link Node} is needed.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public interface StructPart {
  /**
   * Returns the name of the node
   *
   * @return the name
   */
  @Nonnull
  String getName();

  /**
   * Returns the lookup associated with the node
   *
   * @return the lookup
   */
  @Nonnull
  Lookup getLookup();

  /**
   * Returns a list containing all children
   *
   * @return a list containing all children
   */
  @Nonnull
  List<? extends StructPart> getChildren();

  /**
   * Returns the parent (if it has one)
   *
   * @return the parent
   */
  @Nullable
  StructPart getParent();

  /**
   * Adds a child listener. Be careful - most of the time the listener should
   * only be added weak using {@link #addStructureListenerWeak(StructureListener)}
   *
   * @param structureListener the chid listener
   */
  void addStructureListener( @Nonnull StructureListener structureListener );

  /**
   * Adds a child listener wrapped within a weak listener
   *
   * @param structureListener the chid listener
   */
  void addStructureListenerWeak( @Nonnull StructureListener structureListener );

  /**
   * Removes the child listener
   *
   * @param structureListener the child listener
   */
  void removeStructureListener( @Nonnull StructureListener structureListener );

  /**
   * Returns the path of this node
   *
   * @return the path
   */
  @Nonnull
  Path getPath();

  /**
   * Returns whether the given node is a child of this or not
   *
   * @param child the possible child
   * @return true if the given node is a child, false otherwise
   */
  boolean isChild( @Nonnull StructPart child );

  /**
   * Returns true if the node has a parent
   *
   * @return true if the node has a parent, false otherwise
   */
  boolean hasParent();

  /**
   * Returns the child with the given childName
   *
   * @param childName the childName of the children
   * @return the child with the given childName
   *
   * @throws ChildNotFoundException
   *          if any.
   */
  @Nonnull
  StructPart findChild( @Nonnull String childName ) throws ChildNotFoundException;
}
