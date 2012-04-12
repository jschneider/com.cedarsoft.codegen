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

package com.cedarsoft.configuration.xml;

import javax.inject.Inject;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;

import javax.annotation.Nonnull;

/**
 * Configures configurations
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class XmlConfigurationManager {
  @Nonnull
  private static final String MODULES_KEY = "modules";

  @Nonnull
  private final XMLConfiguration configuration;

  /**
   * <p>Constructor for XmlConfigurationManager.</p>
   *
   * @param configuration a {@link XMLConfiguration} object.
   */
  @Inject
  public XmlConfigurationManager( @Nonnull XMLConfiguration configuration ) {
    this.configuration = configuration;

    if ( this.configuration.getFileName() != null ) {
      Runtime.getRuntime().addShutdownHook( new Thread( new Runnable() {
        @Override
        public void run() {
          save();
        }
      } ) );
    }
  }

  protected void save() {
    try {
      this.configuration.save();
    } catch ( ConfigurationException e ) {
      throw new RuntimeException( e );
    }
  }

  /**
   * Returns the configuration for the given moduleType
   *
   * @param moduleType the moduleType
   * @return the configuration for the given  moduleType
   */
  @Nonnull
  public HierarchicalConfiguration getModuleConfiguration( @Nonnull Class<?> moduleType ) {
    String moduleTypeName = moduleType.getName().replaceAll( "\\$", "." );
    HierarchicalConfiguration modulesConfiguration = getModulesConfiguration();
    try {
      return modulesConfiguration.configurationAt( moduleTypeName );
    } catch ( IllegalArgumentException ignore ) {
      modulesConfiguration.addProperty( moduleTypeName, "" );
      return modulesConfiguration.configurationAt( moduleTypeName );
    }
  }

  @Nonnull
  HierarchicalConfiguration getModulesConfiguration() {
    try {
      return configuration.configurationAt( MODULES_KEY );
    } catch ( IllegalArgumentException ignore ) {
      configuration.addProperty( MODULES_KEY, "" );
      return configuration.configurationAt( MODULES_KEY );
    }
  }

  /**
   * <p>Getter for the field <code>configuration</code>.</p>
   *
   * @return a {@link HierarchicalConfiguration} object.
   */
  @Nonnull
  public HierarchicalConfiguration getConfiguration() {
    return configuration;
  }
}
