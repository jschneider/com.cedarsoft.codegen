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

package com.cedarsoft.app;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.annotation.Nonnull;
import javax.inject.Inject;
import java.io.File;
import java.io.IOException;

/**
 * Offers access to the application home.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class DefaultApplicationHomeAccess implements ApplicationHomeAccess {
  @Nonnull
  private static final Log LOG = LogFactory.getLog( DefaultApplicationHomeAccess.class );
  /**
   * Constant <code>SUFFIX_SANDBOX="-sandbox"</code>
   */
  @Nonnull
  public static final String SUFFIX_SANDBOX = "-sandbox";
  @Nonnull
  private final String applicationName;
  private final File applicationHome;

  /**
   * Create a new application home access
   *
   * @param applicationName the name of the application
   * @throws IOException if an io exception occures
   */
  @Inject
  public DefaultApplicationHomeAccess( @ApplicationName @Nonnull String applicationName ) throws IOException {
    this( new File( new File( System.getProperty( "user.home" ) ), '.' + applicationName ), applicationName );
  }

  /**
   * <p>Constructor for DefaultApplicationHomeAccess.</p>
   *
   * @param applicationName a {@link String} object.
   * @param sandbox         a boolean.
   * @throws IOException if any.
   */
  public DefaultApplicationHomeAccess( @Nonnull String applicationName, boolean sandbox ) throws IOException {
    this( new File( new File( System.getProperty( "user.home" ) ), '.' + getApplicationDirName( applicationName, sandbox ) ), applicationName );
  }

  /**
   * <p>getApplicationDirName</p>
   *
   * @param applicationName a {@link String} object.
   * @param sandbox         a boolean.
   * @return a {@link String} object.
   */
  @Nonnull
  public static String getApplicationDirName( @Nonnull String applicationName, boolean sandbox ) {
    if ( sandbox ) {
      return applicationName + SUFFIX_SANDBOX;
    } else {
      return applicationName;
    }
  }

  /**
   * Create a new application home access
   *
   * @param applicationHome the application home
   * @param applicationName the application name
   * @throws IOException if any.
   */
  public DefaultApplicationHomeAccess( @Nonnull File applicationHome, @Nonnull String applicationName ) throws IOException {
    if ( applicationName.length() < 3 ) {
      throw new IllegalArgumentException( "application name is too short: " + applicationName );
    }
    this.applicationHome = applicationHome;
    this.applicationName = applicationName;

    if ( !applicationHome.exists() || !applicationHome.isDirectory() ) {
      applicationHome.mkdirs();
      LOG.info( "Creating Application Directory: " + applicationHome.getAbsolutePath() );
    }
    if ( !applicationHome.canWrite() ) {
      throw new IOException( "Cannot write " + applicationHome.getAbsolutePath() );
    }
  }

  /**
   * {@inheritDoc}
   * <p/>
   * The application name
   */
  @Override
  @Nonnull
  public String getApplicationName() {
    return applicationName;
  }

  /**
   * {@inheritDoc}
   * <p/>
   * The application home dir
   */
  @Override
  @Nonnull
  public File getApplicationHome() {
    return applicationHome;
  }
}
