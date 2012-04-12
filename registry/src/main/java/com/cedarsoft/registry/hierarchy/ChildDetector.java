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

package com.cedarsoft.registry.hierarchy;

import javax.annotation.Nonnull;

import java.util.List;

/**
 * Implementations detect all children of an element that shall be added to the recursion
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 * @param <C> the type of the children
 * @param <P> the type of the parent
 */
public interface ChildDetector<P, C> {
  /**
   * Finds the children for the given parent
   *
   * @param parent the parent
   * @return the children
   */
  @Nonnull
  List<? extends C> findChildren( @Nonnull P parent );

  /**
   * Registers a change listener that is notified when the child detector changes its children
   *
   * @param changeListener the listener
   */
  void addChangeListener( @Nonnull ChildChangeListener<P> changeListener );

  /**
   * Removes the change listener
   *
   * @param changeListener the change listener that is removed
   */
  void removeChangeListener( @Nonnull ChildChangeListener<P> changeListener );
}
