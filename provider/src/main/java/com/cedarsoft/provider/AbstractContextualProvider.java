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

package com.cedarsoft.provider;

import org.jetbrains.annotations.NotNull;

/**
 * <p>Abstract AbstractContextualProvider class.</p>
 *
 * @param <T> the type that is provided
 * @param <C> the context
 * @param <E> the exception that is thrown
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public abstract class AbstractContextualProvider<T, C, E extends Throwable> implements ContextualProvider<T, C, E> {
  @NotNull
  private final ContextualProvider<T, C, E> contextualProvider;

  /**
   * <p>Constructor for AbstractContextualProvider.</p>
   *
   * @param contextualProvider a {@link com.cedarsoft.provider.ContextualProvider} object.
   */
  protected AbstractContextualProvider( @NotNull ContextualProvider<T, C, E> contextualProvider ) {
    this.contextualProvider = contextualProvider;
  }

  /**
   * <p>createProvider</p>
   *
   * @param context a C object.
   * @return a {@link com.cedarsoft.provider.Provider} object.
   */
  @NotNull
  public Provider<T, E> createProvider( @NotNull final C context ) {
    return new Provider<T, E>() {
      @NotNull
      @Override
      public T provide() throws E {
        return contextualProvider.provide( context );
      }

      @NotNull
      @Override
      public String getDescription() {
        return contextualProvider.getDescription( context );
      }
    };
  }
}
