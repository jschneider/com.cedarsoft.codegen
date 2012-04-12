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

package com.cedarsoft.lookup.binding;

import com.cedarsoft.lookup.LookupChangeEvent;
import com.cedarsoft.lookup.TypedLookupChangeListener;

import javax.annotation.Nonnull;

import java.lang.Class;
import java.lang.Object;
import java.lang.String;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * A special LookupChangeListener that changes the property of a given object (using reflection).
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class PropertyCallback<T> implements TypedLookupChangeListener<T> {
  private final Class<T> propertyType;
  private final Object object;
  private final Method setter;

  /**
   * <p>Constructor for PropertyCallback.</p>
   *
   * @param object       a {@link Object} object.
   * @param propertyName a {@link String} object.
   * @param propertyType a {@link Class} object.
   */
  public PropertyCallback( @Nonnull Object object, @Nonnull String propertyName, @Nonnull Class<T> propertyType ) {
    this.propertyType = propertyType;
    this.object = object;
    try {
      setter = object.getClass().getMethod( "set" + propertyName.substring( 0, 1 ).toUpperCase() + propertyName.substring( 1 ), propertyType );
    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Class<T> getType() {
    return propertyType;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void lookupChanged( @Nonnull LookupChangeEvent<? extends T> event ) {
    try {
      setter.invoke( object, event.getNewValue() );
    } catch ( IllegalAccessException e ) {
      throw new RuntimeException( e );
    } catch ( InvocationTargetException e ) {
      throw new RuntimeException( e );
    }
  }
}
