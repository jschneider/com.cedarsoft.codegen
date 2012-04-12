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

import com.cedarsoft.configuration.xml.ConfigurationAccess;
import org.apache.commons.configuration.Configuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.beans.factory.FactoryBean;

import java.lang.Class;
import java.lang.String;

/**
 * This factory reads a value from the configuration manager
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
@Deprecated
public class ConfigurationPropertyFactory<T> implements FactoryBean {
  @Nonnull
  private final ConfigurationAccess<T> configurationAccess;

  /**
   * <p>Constructor for ConfigurationPropertyFactory.</p>
   *
   * @param configuration        a {@link Configuration} object.
   * @param type                 a {@link Class} object.
   * @param key                  a {@link String} object.
   * @param defaultValueProvider a {@link DefaultValueProvider} object.
   */
  public ConfigurationPropertyFactory( @Nonnull Configuration configuration, @Nonnull Class<T> type, @Nonnull String key, @Nonnull DefaultValueProvider defaultValueProvider ) {
    this.configurationAccess = new ConfigurationAccess<T>( configuration, type, key, defaultValueProvider );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nullable
  public Object getObject() throws Exception {
    return configurationAccess.resolve();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Class<?> getObjectType() {
    return configurationAccess.getType();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSingleton() {
    return true;
  }
}
