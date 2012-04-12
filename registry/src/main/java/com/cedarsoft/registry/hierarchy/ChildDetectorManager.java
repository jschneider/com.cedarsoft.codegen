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

package com.cedarsoft.registry.hierarchy;

import com.cedarsoft.registry.TypeRegistry;
import javax.annotation.Nonnull;

import java.lang.Class;
import java.lang.IllegalArgumentException;
import java.util.Map;

/**
 * Manages {@link ChildDetector}s
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ChildDetectorManager {
  @Nonnull
  private final TypeRegistry<ChildDetector<?, ?>> registry;

  /**
   * <p>Constructor for ChildDetectorManager.</p>
   */
  public ChildDetectorManager() {
    this( true );
  }

  /**
   * <p>Constructor for ChildDetectorManager.</p>
   *
   * @param registerSuperTypes a boolean.
   */
  public ChildDetectorManager( boolean registerSuperTypes ) {
    registry = new TypeRegistry<ChildDetector<?, ?>>( registerSuperTypes );
  }

  /**
   * <p>addChildDetector</p>
   *
   * @param parentType    a {@link Class} object.
   * @param childDetector a {@link ChildDetector} object.
   */
  public <P, C> void addChildDetector( @Nonnull Class<P> parentType, @Nonnull ChildDetector<P, C> childDetector ) {
    registry.addElement( parentType, childDetector );
  }

  /**
   * <p>setChildDetectors</p>
   *
   * @param childDetectors a {@link Map} object.
   */
  public void setChildDetectors( Map<Class<?>, ChildDetector<?, ?>> childDetectors ) {
    registry.setElements( childDetectors );
  }

  /**
   * <p>getChildDetector</p>
   *
   * @param parentType a {@link Class} object.
   * @return a {@link ChildDetector} object.
   *
   * @throws IllegalArgumentException
   *          if any.
   */
  @Nonnull
  public <P, C> ChildDetector<P, C> getChildDetector( @Nonnull Class<P> parentType ) throws IllegalArgumentException {
    return ( ChildDetector<P, C> ) registry.getElement( parentType );
  }
}
