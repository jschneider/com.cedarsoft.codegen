/**
 * Copyright (C) 2010 cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce.txt
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

package com.cedarsoft.configuration.xml;

import com.jgoodies.binding.value.ValueModel;
import org.jetbrains.annotations.NotNull;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

/**
 * You don't have to use this class directly. Instead use
 * {@link ConfigurationBinding#bind(ConfigurationAccess,ValueModel)
 * <p/>
 * Connects a value model to a configuration access.
 * For each property of a bean one ConfigurationConnector is needed.
 * <p/>
 * T: The type of the property
 */
public class ConfigurationConnector<T> {
  @NotNull
  private final ValueModel subject;
  @NotNull
  private final ConfigurationAccess<T> configurationAccess;

  /**
   * Creates a new ConfigurationConnector
   *
   * @param subject             the value model of the bean
   * @param configurationAccess the configuration access
   */
  public ConfigurationConnector( @NotNull ValueModel subject, @NotNull ConfigurationAccess<T> configurationAccess ) {
    this.subject = subject;
    this.configurationAccess = configurationAccess;
    this.subject.addValueChangeListener( new SubjectChangeListener() );
  }

  /**
   * Returns the value model
   *
   * @return the value model
   */
  @NotNull
  public ValueModel getSubject() {
    return subject;
  }

  /**
   * Returns the configuration access
   *
   * @return the configuration access
   */
  @NotNull
  public ConfigurationAccess<T> getConfigurationAccess() {
    return configurationAccess;
  }

  /**
   * Reads from the configuration
   */
  public void readFromConfiguration() {
    subject.setValue( configurationAccess.resolve() );
  }

  private class SubjectChangeListener implements PropertyChangeListener {
    @Override
    public void propertyChange( PropertyChangeEvent evt ) {
      T value = configurationAccess.getType().cast( evt.getNewValue() );
      configurationAccess.store( value );
    }
  }
}
