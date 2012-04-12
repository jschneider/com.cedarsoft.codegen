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

package com.cedarsoft.objectaccess;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Support for deletion processors.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class DeletionProcessorsSupport<T> {
  @Nonnull
  private final List<DeletionProcessor<T>> deletionProcessors = new ArrayList<DeletionProcessor<T>>();

  /**
   * Sets the deletion processors
   *
   * @param deletionProcessors the processors
   */
  public void setDeletionProcessors( @Nonnull List<? extends DeletionProcessor<T>> deletionProcessors ) {
    this.deletionProcessors.clear();
    this.deletionProcessors.addAll( deletionProcessors );
  }

  /**
   * <p>Getter for the field <code>deletionProcessors</code>.</p>
   *
   * @return a {@link List} object.
   */
  @Nonnull
  public List<? extends DeletionProcessor<T>> getDeletionProcessors() {
    return Collections.unmodifiableList( deletionProcessors );
  }

  /**
   * <p>addDeletionProcessor</p>
   *
   * @param processor a {@link DeletionProcessor} object.
   */
  public void addDeletionProcessor( @Nonnull DeletionProcessor<T> processor ) {
    this.deletionProcessors.add( processor );
  }

  /**
   * <p>removeDeletionProcessor</p>
   *
   * @param processor a {@link DeletionProcessor} object.
   */
  public void removeDeletionProcessor( @Nonnull DeletionProcessor<T> processor ) {
    this.deletionProcessors.remove( processor );
  }

  /**
   * Notifes all registered processors that the given object will be deleted
   *
   * @param object the object that will be deleted
   */
  public void notifyWillBeDeleted( @Nonnull T object ) {
    for ( DeletionProcessor<T> processor : deletionProcessors ) {
      processor.willBeDeleted( object );
    }
  }
}
