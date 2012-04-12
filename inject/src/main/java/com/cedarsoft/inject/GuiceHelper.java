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

package com.cedarsoft.inject;

import com.google.inject.Binder;
import com.google.inject.Key;
import com.google.inject.util.Types;
import javax.annotation.Nonnull;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

/**
 * <p>GuiceHelper class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class GuiceHelper {
  private GuiceHelper() {
  }

  /**
   * <p>superCollectionOf</p>
   *
   * @param type a {@link Type} object.
   * @return a {@link ParameterizedType} object.
   */
  @Nonnull
  public static ParameterizedType superCollectionOf( @Nonnull Type type ) {
    return Types.newParameterizedType( Collection.class, Types.subtypeOf( type ) );
  }

  /**
   * <p>superListOf</p>
   *
   * @param type a {@link Type} object.
   * @return a {@link ParameterizedType} object.
   */
  @Nonnull
  public static ParameterizedType superListOf( @Nonnull Type type ) {
    return Types.newParameterizedType( List.class, Types.subtypeOf( type ) );
  }

  /**
   * Binds a wildcard collection to the set of that type
   *
   * @param binder the binder
   * @param type   the type
   * @param <T>    a T object.
   */
  public static <T> void bindWildcardCollectionForSet( @Nonnull Binder binder, @Nonnull Type type ) {
    binder.bind( ( Key<Collection<? extends T>> ) Key.get( GuiceHelper.superCollectionOf( type ) ) ).to( ( Key<? extends Collection<? extends T>> ) Key.get( Types.setOf( type ) ) );
  }
}
