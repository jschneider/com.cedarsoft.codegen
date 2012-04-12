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

package com.cedarsoft.lookup;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.lang.Class;

/**
 * This event is thrown whenever a lookup has been changed.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class LookupChangeEvent<T> {
  @Nonnull
  private final Lookup source;
  @Nonnull
  private final Class<? super T> type;
  @Nullable
  private final T oldValue;
  @Nullable
  private final T newValue;

  /**
   * <p>Constructor for LookupChangeEvent.</p>
   *
   * @param source   a {@link Lookup} object.
   * @param type     a {@link Class} object.
   * @param oldValue a T object.
   * @param newValue a T object.
   */
  public LookupChangeEvent( @Nonnull Lookup source, @Nonnull Class<? super T> type, @Nullable T oldValue, @Nullable T newValue ) {
    this.source = source;
    this.type = type;
    this.oldValue = oldValue;
    this.newValue = newValue;
  }

  /**
   * <p>Getter for the field <code>type</code>.</p>
   *
   * @return a {@link Class} object.
   */
  @Nonnull
  public Class<? super T> getType() {
    return type;
  }

  /**
   * <p>Getter for the field <code>oldValue</code>.</p>
   *
   * @return a T object.
   */
  @Nullable
  public T getOldValue() {
    return oldValue;
  }

  /**
   * <p>Getter for the field <code>newValue</code>.</p>
   *
   * @return a T object.
   */
  @Nullable
  public T getNewValue() {
    return newValue;
  }

  /**
   * <p>Getter for the field <code>source</code>.</p>
   *
   * @return a {@link Lookup} object.
   */
  @Nonnull
  public Lookup getSource() {
    return source;
  }
}
