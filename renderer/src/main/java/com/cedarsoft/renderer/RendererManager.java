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

package com.cedarsoft.renderer;

import com.cedarsoft.registry.TypeRegistry;
import javax.annotation.Nonnull;

import java.lang.Class;
import java.lang.IllegalArgumentException;
import java.util.Map;

/**
 * Manages {@link Renderer}
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class RendererManager {
  @Nonnull
  private final TypeRegistry<Renderer<?, Object>> registry;

  /**
   * <p>Constructor for RendererManager.</p>
   */
  public RendererManager() {
    this( false );
  }

  /**
   * <p>Constructor for RendererManager.</p>
   *
   * @param registerSuperTypes a boolean.
   */
  public RendererManager( boolean registerSuperTypes ) {
    registry = new TypeRegistry<Renderer<?, Object>>( registerSuperTypes );
  }

  /**
   * <p>setRenderer</p>
   *
   * @param renderer a {@link Map} object.
   */
  @Deprecated
  public void setRenderer( @Nonnull Map<Class<?>, Renderer<?, Object>> renderer ) {
    registry.setElements( renderer );
  }

  /**
   * <p>addRenderer</p>
   *
   * @param type     a {@link Class} object.
   * @param renderer a {@link Renderer} object.
   */
  public <T> void addRenderer( @Nonnull Class<T> type, @Nonnull Renderer<? super T, Object> renderer ) {
    registry.addElement( type, renderer );
  }

  /**
   * <p>getRenderer</p>
   *
   * @param type a {@link Class} object.
   * @return a {@link Renderer} object.
   *
   * @throws IllegalArgumentException
   *          if any.
   */
  @Nonnull
  public <T> Renderer<? super T, Object> getRenderer( @Nonnull Class<T> type ) throws IllegalArgumentException {
    return ( Renderer<T, Object> ) registry.getElement( type );
  }
}
