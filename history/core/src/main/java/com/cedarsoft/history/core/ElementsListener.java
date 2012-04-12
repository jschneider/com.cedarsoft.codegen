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

package com.cedarsoft.history.core;

import javax.annotation.Nonnull;

/**
 * Can be registered to be notified about updates.
 * There exist two types of "added", "changed" and "deleted" methods.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 * @param <E> the type of the elements
 */
public interface ElementsListener<E> {
  /**
   * Is notified when the elements have been deleted
   *
   * @param event a {@link ElementsChangedEvent} object.
   */
  void elementsDeleted( @Nonnull ElementsChangedEvent<? extends E> event );

  /**
   * The elements are added
   *
   * @param event a {@link ElementsChangedEvent} object.
   */
  void elementsAdded( @Nonnull ElementsChangedEvent<? extends E> event );

  /**
   * Elements have been changed
   *
   * @param event a {@link ElementsChangedEvent} object.
   */
  void elementsChanged( @Nonnull ElementsChangedEvent<? extends E> event );
}
