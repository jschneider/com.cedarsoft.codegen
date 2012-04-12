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

package com.cedarsoft.license;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.Nullable;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Represents the license of the image
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 * @noinspection ClassReferencesSubclass
 */
public class License {
  /**
   * Constant <code>UNKNOWN</code>
   */
  @Nonnull
  public static final License UNKNOWN = new License( "UNKNOWN", "Unknown" );
  /**
   * Constant <code>ALL_RIGHTS_RESERVED</code>
   */
  @Nonnull
  public static final License ALL_RIGHTS_RESERVED = new License( "ALL_RIGHTS_RESERVED", "All rights reserved" );
  /**
   * Constant <code>PUBLIC_DOMAIN</code>
   */
  @Nonnull
  public static final License PUBLIC_DOMAIN = new License( "PUBLIC_DOMAIN", "Public Domain" );

  @Nonnull
  public static final License CDDL = new License( "CDDL", "Common Development and Distribution License", "http://www.opensource.org/licenses/cddl1.php" );
  @Nonnull
  public static final License AFFERO_GPL = new License( "AFFERO GPL", "Affero GNU Public License", "http://www.opensource.org/licenses/agpl-v3.html" );
  @Nonnull
  public static final License APACHE_20 = new License( "APACHE 2.0", "Apache License 2.0", "http://www.opensource.org/licenses/apache2.0.php" );
  @Nonnull
  public static final License BSD_NEW = new License( "BSD NEW", "New and Simplified BSD licenses", "http://www.opensource.org/licenses/bsd-license.php" );
  @Nonnull
  public static final License CPAL = new License( "CPAL", "Common Public Attribution License 1.0", "http://www.opensource.org/licenses/cpal_1.0" );
  @Nonnull
  public static final License EPL = new License( "EPL", "Eclipse Public License", "http://www.opensource.org/licenses/eclipse-1.0.php" );
  @Nonnull
  public static final License GPL_2 = new License( "GPLv2", "GNU General Public License 2.0", "http://www.opensource.org/licenses/gpl-2.0.php" );
  @Nonnull
  public static final License GPL_3 = new License( "GPLv3", "GNU General Public License 3.0", "http://www.opensource.org/licenses/gpl-3.0.html" );
  @Nonnull
  public static final License LGPL = new License( "LGPL", "GNU Library or \"Lesser\" General Public License", "http://www.opensource.org/licenses/lgpl-2.1.php" );
  @Nonnull
  public static final License LGPL_3 = new License( "LGPLv3", "GNU Library or \"Lesser\" General Public License version 3.0 (LGPLv3)", "http://www.opensource.org/licenses/lgpl-3.0.html" );
  @Nonnull
  public static final License MPL = new License( "MPL", "Mozilla Public License 1.1 (MPL)", "http://www.opensource.org/licenses/mozilla1.1.php" );
  /**
   * Creative Commons
   */
  @Nonnull
  public static final CreativeCommonsLicense CC_BY = new CreativeCommonsLicense( "CC-BY", "CC Attribution", false, CreativeCommonsLicense.ModificationsAllowed.YES, "http://creativecommons.org/licenses/by/3.0" );
  /**
   * Share-Alike
   */
  @Nonnull
  public static final CreativeCommonsLicense CC_BY_SA = new CreativeCommonsLicense( "CC-BY-SA", "CC Attribution Share Alike", false, CreativeCommonsLicense.ModificationsAllowed.SHARE_ALIKE, "http://creativecommons.org/licenses/by-sa/3.0" );
  /**
   * No-Derivative-Work
   */
  @Nonnull
  public static final CreativeCommonsLicense CC_BY_ND = new CreativeCommonsLicense( "CC-BY-ND", "CC Attribution No Derivates", false, CreativeCommonsLicense.ModificationsAllowed.NO, "http://creativecommons.org/licenses/by-nd/3.0" );
  /**
   * Non-Commercial
   */
  @Nonnull
  public static final CreativeCommonsLicense CC_BY_NC = new CreativeCommonsLicense( "CC-BY-NC", "CC Attribution Non-Commercial", true, CreativeCommonsLicense.ModificationsAllowed.YES, "http://creativecommons.org/licenses/by-nc/3.0" );
  /**
   * Non-Commercial, Share-Alike
   */
  @Nonnull
  public static final CreativeCommonsLicense CC_BY_NC_SA = new CreativeCommonsLicense( "CC-BY-NC-SA", "CC Attribution Non-Commercial Share Alike", true, CreativeCommonsLicense.ModificationsAllowed.SHARE_ALIKE, "http://creativecommons.org/licenses/by-nc-sa/3.0" );
  /**
   * Non-Commercial, No-Derivative-Work
   */
  @Nonnull
  public static final CreativeCommonsLicense CC_BY_NC_ND = new CreativeCommonsLicense( "CC-BY-NC-ND", "CC Attribution Non-Commercial No Derivates", true, CreativeCommonsLicense.ModificationsAllowed.NO, "http://creativecommons.org/licenses/by-nc-nd/3.0" );

  @Nonnull
  public static final List<? extends CreativeCommonsLicense> CC_LICENSES = Collections.unmodifiableList( Arrays.asList(
    CC_BY,
    CC_BY_SA,
    CC_BY_ND,
    CC_BY_NC,
    CC_BY_NC_SA,
    CC_BY_NC_ND
  ) );

  @Nonnull
  public static final List<? extends License> LICENSES;

  static {
    List<License> list = new ArrayList<License>( Arrays.asList(
      UNKNOWN,
      ALL_RIGHTS_RESERVED,
      PUBLIC_DOMAIN,
      CDDL,
      AFFERO_GPL,
      APACHE_20,
      BSD_NEW,
      CPAL,
      EPL,
      GPL_2,
      GPL_3,
      LGPL,
      LGPL_3,
      MPL
    ) );
    list.addAll( CC_LICENSES );
    LICENSES = Collections.unmodifiableList( list );
  }

  @Nonnull
  private final String id;
  @Nonnull
  private final String name;

  @Nullable
  private final URL url;

  /**
   * <p>Constructor for License.</p>
   *
   * @param id   a {@link String} object.
   * @param name a {@link String} object.
   */
  public License( @Nonnull String id, @Nonnull String name ) {
    this( id, name, ( URL ) null );
  }

  public License( @Nonnull String id, @Nonnull String name, @Nonnull String url ) {
    this( id, name, getUrl( url ) );
  }

  public License( @Nonnull String id, @Nonnull String name, @Nullable URL url ) {
    this.id = id;
    this.name = name;
    this.url = url;
  }

  /**
   * <p>Getter for the field <code>name</code>.</p>
   *
   * @return a {@link String} object.
   */
  @Nonnull
  public String getName() {
    return name;
  }

  /**
   * <p>Getter for the field <code>id</code>.</p>
   *
   * @return a {@link String} object.
   */
  @Nonnull
  public String getId() {
    return id;
  }

  @Nullable
  public URL getUrl() {
    return url;
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof License ) ) return false;

    License license = ( License ) o;

    if ( !id.equals( license.id ) ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    return id.hashCode();
  }

  @Override
  public String toString() {
    return name + " (" + id + ")";
  }

  @Nonnull
  static URL getUrl( @Nonnull String url ) {
    try {
      return new URL( url );
    } catch ( MalformedURLException e ) {
      throw new RuntimeException( e );
    }
  }

  @Nonnull
  public static License get( @Nonnull String id ) throws IllegalArgumentException{
    for ( License license : LICENSES ) {
      if ( license.getId().equals( id ) ) {
        return license;
      }
    }

    throw new IllegalArgumentException( "No license found for id <" + id + ">" );
  }
}
