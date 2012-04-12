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

package com.cedarsoft.configuration;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Default implementation for configuration manager
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class DefaultConfigurationManager implements ConfigurationManager {
  @Nonnull
  private final List<Object> configurations = new ArrayList<Object>();

  /**
   * <p>Constructor for DefaultConfigurationManager.</p>
   */
  public DefaultConfigurationManager() {
  }

  /**
   * <p>Constructor for DefaultConfigurationManager.</p>
   *
   * @param initialConfigurations a {@link List} object.
   */
  public DefaultConfigurationManager( @Nonnull List<?> initialConfigurations ) {
    configurations.addAll( initialConfigurations );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addConfiguration( @Nonnull Object configuration ) {
    configurations.add( configuration );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<?> getConfigurations() {
    return Collections.unmodifiableList( configurations );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public <T> T getConfiguration( @Nonnull Class<T> configurationType ) throws IllegalArgumentException {
    for ( Object configuration : configurations ) {
      if ( configuration.getClass().equals( configurationType ) ) {
        return configurationType.cast( configuration );
      }
    }

    throw new IllegalArgumentException( "No configuration found of type " + configurationType.getName() );
  }
}

