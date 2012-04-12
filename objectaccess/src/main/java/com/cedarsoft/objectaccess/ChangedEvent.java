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

import com.cedarsoft.commons.properties.PropertiesPath;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.lang.Object;
import java.lang.String;

/**
 * Event that represents the change of something
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ChangedEvent<T> {
  @Nonnull
  private final T changedObject;
  @Nonnull
  private final PropertiesPath propertiesPath;
  @Nullable
  private final Object context;

  /**
   * <p>Constructor for ChangedEvent.</p>
   *
   * @param changedObject  a T object.
   * @param context        a {@link Object} object.
   * @param propertiesPath a {@link String} object.
   */
  public ChangedEvent( @Nonnull T changedObject, @Nullable Object context, @Nonnull String... propertiesPath ) {
    if ( propertiesPath.length == 0 ) {
      throw new IllegalArgumentException( "Empty properties path" );
    }
    this.changedObject = changedObject;
    this.context = context;
    this.propertiesPath = new PropertiesPath( propertiesPath );
  }

  /**
   * <p>Getter for the field <code>propertiesPath</code>.</p>
   *
   * @return a {@link PropertiesPath} object.
   */
  @Nonnull
  public PropertiesPath getPropertiesPath() {
    return propertiesPath;
  }

  /**
   * The context object
   *
   * @return the context object
   */
  @Nullable
  public Object getContext() {
    return context;
  }

  /**
   * The changed object
   *
   * @return the changed object
   */
  @Nonnull
  public T getChangedObject() {
    return changedObject;
  }

  /**
   * <p>getRootProperty</p>
   *
   * @return a {@link String} object.
   */
  @Deprecated
  @Nonnull
  public String getRootProperty() {
    return propertiesPath.getRootProperty();
  }

  @Override
  public String toString() {
    return "ChangedEvent{" +
      "changedObject=" + changedObject +
      ", propertiesPath=" + propertiesPath +
      ", context=" + context +
      '}';
  }
}
