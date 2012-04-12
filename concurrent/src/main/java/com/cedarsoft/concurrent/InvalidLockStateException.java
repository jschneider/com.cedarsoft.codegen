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

package com.cedarsoft.concurrent;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>InvalidLockStateException class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class InvalidLockStateException extends RuntimeException {
  @Nonnull
  private final List<String> readLockingThreads = new ArrayList<String>();

  /**
   * <p>Constructor for InvalidLockStateException.</p>
   *
   * @param readLockingThreads a {@link List} object.
   */
  public InvalidLockStateException( @Nonnull List<? extends Thread> readLockingThreads ) {
    super( createMessage( readLockingThreads ) );
    for ( Thread readLockingThread : readLockingThreads ) {
      this.readLockingThreads.add( readLockingThread.getName() );
    }
  }

  /**
   * <p>Getter for the field <code>readLockingThreads</code>.</p>
   *
   * @return a {@link List} object.
   */
  @Nonnull
  public List<String> getReadLockingThreads() {
    return Collections.unmodifiableList( readLockingThreads );
  }

  private static String createMessage( @Nonnull List<? extends Thread> readLockingThreads ) {
    StringBuilder message = new StringBuilder().append( "Cannot get write lock because there are still read locks active in: " ).append( "\n" );
    for ( Thread thread : readLockingThreads ) {
      message.append( "\t" ).append( thread.getName() );
    }
    return message.toString();
  }
}
