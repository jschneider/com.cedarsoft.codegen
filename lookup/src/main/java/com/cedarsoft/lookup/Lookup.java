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
import java.lang.IllegalArgumentException;
import java.lang.UnsupportedOperationException;
import java.util.Map;

/**
 * A lookup offers a simple way to get "extensions" of the current object.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public interface Lookup {
  /**
   * Lookup a given object
   *
   * @param type the type
   * @param <T>  a T object.
   * @return the lookup object or null if nothing has been found
   */
  @Nullable
  <T> T lookup( @Nonnull Class<T> type );

  /**
   * Lookks up a given object
   *
   * @param type the type
   * @param <T>  a T object.
   * @return the looked up object
   *
   * @throws IllegalArgumentException
   *          if no object has been found
   */
  @Nonnull
  <T> T lookupNonNull( @Nonnull Class<T> type ) throws IllegalArgumentException;

  /**
   * Returns a  map containing the available lookup objects.
   * This method can throw an {@link UnsupportedOperationException} if the map is not available.
   * If no exception is thrown the map must contain every possible lookup object.
   *
   * @return a  map containing the available lookup objects.
   *
   * @throws UnsupportedOperationException
   *          if this method is not supported
   */
  @Nonnull
  Map<Class<?>, Object> lookups();

  /**
   * Binds the given lookup change listener. Adds the given listener and calls
   * {@link LookupChangeListener#lookupChanged(LookupChangeEvent)} for the first time.
   *
   * @param lookupChangeListener the listener that is added
   * @param type                 a {@link Class} object.
   * @param <T>                  a T object.
   */
  <T> void bind( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener );

  /**
   * Binds the given lookup change listener with the key retrieved from {@link TypedLookupChangeListener#getType()}.
   * Adds the given listener and calls
   * {@link LookupChangeListener#lookupChanged(LookupChangeEvent)} for the first time.
   *
   * @param lookupChangeListener the listener that is added
   * @param <T>                  a T object.
   */
  <T> void bind( @Nonnull TypedLookupChangeListener<T> lookupChangeListener );

  /**
   * Binds the given lookup change listener that is wrapped within a {@link WeakLookupChangeListener}.
   * Adds the given listener and calls
   * {@link LookupChangeListener#lookupChanged(LookupChangeEvent)} for the first time.
   *
   * @param lookupChangeListener the listener that is added
   * @param type                 a {@link Class} object.
   * @param <T>                  a T object.
   */
  <T> void bindWeak( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener );

  /**
   * Binds the given lookup change listener (that is wrapped within a {@link WeakLookupChangeListener})
   * with the key retrieved from {@link TypedLookupChangeListener#getType()}.
   * Adds the given listener and calls
   * {@link LookupChangeListener#lookupChanged(LookupChangeEvent)} for the first time.
   *
   * @param lookupChangeListener the listener that is added
   * @param <T>                  a T object.
   */
  <T> void bindWeak( @Nonnull TypedLookupChangeListener<T> lookupChangeListener );

  /**
   * Adds a lookup change listener
   *
   * @param lookupChangeListener the lookup change listener
   */
  void addChangeListener( @Nonnull LookupChangeListener<?> lookupChangeListener );

  /**
   * Add a lookup change listener for a given type
   *
   * @param type                 the type
   * @param lookupChangeListener the listener
   * @param <T>                  a T object.
   */
  <T> void addChangeListener( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener );

  /**
   * Adds a lookup change listener that is wrapped within a {@link WeakLookupChangeListener}
   *
   * @param lookupChangeListener the lookup change listener
   */
  void addChangeListenerWeak( @Nonnull LookupChangeListener<?> lookupChangeListener );

  /**
   * Add a lookup change listener that is wrapped within a {@link WeakLookupChangeListener} for a given type
   *
   * @param type                 the type
   * @param lookupChangeListener the listener
   * @param <T>                  a T object.
   */
  <T> void addChangeListenerWeak( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener );

  /**
   * Remove a lookup change listener
   *
   * @param lookupChangeListener the listener that is removed from *all* types
   */
  void removeChangeListener( @Nonnull LookupChangeListener<?> lookupChangeListener );

  /**
   * Remove a lookup change listener from the given type
   *
   * @param type                 the type of the listener
   * @param lookupChangeListener the listener
   * @param <T>                  a T object.
   */
  <T> void removeChangeListener( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener );
}
