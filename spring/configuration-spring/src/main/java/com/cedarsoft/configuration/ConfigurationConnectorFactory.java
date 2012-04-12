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
import com.cedarsoft.configuration.xml.ConfigurationConnector;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import org.apache.commons.configuration.Configuration;
import javax.annotation.Nonnull;
import org.springframework.beans.factory.FactoryBean;

import java.lang.Class;
import java.lang.String;

/**
 * <p/>
 * Date: Jul 5, 2007<br>
 * Time: 1:30:53 PM<br>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
@Deprecated
public class ConfigurationConnectorFactory<T> implements FactoryBean {
  @Nonnull
  private final ConfigurationAccess<T> configurationAccess;
  private ValueModel valueModel;
  private final ConfigurationConnector<T> connector;

  /**
   * <p>Constructor for ConfigurationConnectorFactory.</p>
   *
   * @param configuration        a {@link Configuration} object.
   * @param type                 a {@link Class} object.
   * @param propertyName         a {@link String} object.
   * @param defaultValueProvider a {@link DefaultValueProvider} object.
   * @param beanAdapter          a {@link BeanAdapter} object.
   */
  public ConfigurationConnectorFactory( @Nonnull Configuration configuration, @Nonnull Class<T> type, @Nonnull String propertyName, @Nonnull DefaultValueProvider defaultValueProvider, @Nonnull BeanAdapter<?> beanAdapter ) {
    valueModel = beanAdapter.getValueModel( propertyName );
    configurationAccess = new ConfigurationAccess<T>( configuration, type, propertyName, defaultValueProvider );
    connector = new ConfigurationConnector<T>( valueModel, configurationAccess );
    connector.readFromConfiguration();
  }

  /**
   * <p>Constructor for ConfigurationConnectorFactory.</p>
   *
   * @param configuration a {@link Configuration} object.
   * @param type          a {@link Class} object.
   * @param propertyName  a {@link String} object.
   * @param defaultValue  a T object.
   * @param beanAdapter   a {@link BeanAdapter} object.
   */
  public ConfigurationConnectorFactory( @Nonnull Configuration configuration, @Nonnull Class<T> type, @Nonnull String propertyName, @Nonnull T defaultValue, @Nonnull BeanAdapter<?> beanAdapter ) {
    valueModel = beanAdapter.getValueModel( propertyName );
    configurationAccess = new ConfigurationAccess<T>( configuration, type, propertyName, defaultValue );
    connector = new ConfigurationConnector<T>( valueModel, configurationAccess );
    connector.readFromConfiguration();
  }

  /**
   * <p>Constructor for ConfigurationConnectorFactory.</p>
   *
   * @param configurationAccess a {@link ConfigurationAccess} object.
   * @param beanAdapter         a {@link BeanAdapter} object.
   */
  public ConfigurationConnectorFactory( @Nonnull ConfigurationAccess<T> configurationAccess, @Nonnull BeanAdapter<?> beanAdapter ) {
    valueModel = beanAdapter.getValueModel( configurationAccess.getKey() );
    this.configurationAccess = configurationAccess;
    connector = new ConfigurationConnector<T>( valueModel, this.configurationAccess );
    connector.readFromConfiguration();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object getObject() throws Exception {
    return connector;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Class<ConfigurationConnector> getObjectType() {
    return ConfigurationConnector.class;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isSingleton() {
    return true;
  }
}
