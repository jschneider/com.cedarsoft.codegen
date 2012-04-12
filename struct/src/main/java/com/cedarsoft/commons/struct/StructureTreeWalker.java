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

/**
 * Traverses a structure.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public interface StructureTreeWalker {
  /**
   * Traverses the structure with the given StructPart as root
   *
   * @param root           the root
   * @param walkerCallBack the callback that is notified whenever a node is reached
   */
  void walk( @Nonnull StructPart root, @Nonnull WalkerCallBack walkerCallBack );

  /**
   * A callback that can be used to traverse a structure using {@link StructureTreeWalker}.
   */
  interface WalkerCallBack {
    /**
     * Is called for each node that is reached
     *
     * @param node  the node that is reached
     * @param level the level of the node (0 for the root node, 1 for the direct children of the root)
     * @throws CanceledException if the current node and its children should be skipped
     */
    void nodeReached( @Nonnull StructPart node, int level ) throws CanceledException;
  }
}
