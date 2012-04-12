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
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

/**
 * <p>CreativeCommonsLicense class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class CreativeCommonsLicense extends License {

  @Nonnull
  private final ModificationsAllowed modificationsAllowed;

  private final boolean restrictedToNonCommercial;


  /**
   * <p>Constructor for CreativeCommonsLicense.</p>
   *
   * @param id                        a {@link String} object.
   * @param name                      a {@link String} object.
   * @param restrictedToNonCommercial a boolean.
   * @param modificationsAllowed      a {@link CreativeCommonsLicense.ModificationsAllowed} object.
   * @param url                       the url
   */
  CreativeCommonsLicense( @Nonnull String id, @Nonnull String name, boolean restrictedToNonCommercial, @Nonnull ModificationsAllowed modificationsAllowed, @Nonnull String url ) {
    super( id, name, url );
    this.restrictedToNonCommercial = restrictedToNonCommercial;
    this.modificationsAllowed = modificationsAllowed;
  }

  /**
   * <p>isRestrictedToNonCommercial</p>
   *
   * @return a boolean.
   */
  public boolean isRestrictedToNonCommercial() {
    return restrictedToNonCommercial;
  }

  /**
   * <p>Getter for the field <code>modificationsAllowed</code>.</p>
   *
   * @return a {@link CreativeCommonsLicense.ModificationsAllowed} object.
   */
  @Nonnull
  public ModificationsAllowed getModificationsAllowed() {
    return modificationsAllowed;
  }

  /**
   * <p>isDerivedWorkAllowed</p>
   *
   * @return a boolean.
   */
  public boolean isDerivedWorkAllowed() {
    return modificationsAllowed == ModificationsAllowed.YES;
  }

  /**
   * <p>isUsableCommercially</p>
   *
   * @return a boolean.
   */
  public boolean isUsableCommercially() {
    return !isRestrictedToNonCommercial();
  }

  /**
   * <p>isSharedAlikeDerivedWorkAllowed</p>
   *
   * @return a boolean.
   */
  public boolean isSharedAlikeDerivedWorkAllowed() {
    return modificationsAllowed == ModificationsAllowed.SHARE_ALIKE || modificationsAllowed == ModificationsAllowed.YES;
  }

  /**
   * Returns the translated URL
   *
   * @param locale the locale
   * @return the URL for the locale
   */
  @Nonnull
  public URL getUrl( @Nonnull Locale locale ) {
    URL urlBase = getUrl();
    assert urlBase != null;
    try {
      return new URL( urlBase.getProtocol(), urlBase.getHost(), urlBase.getPort(), urlBase.getFile() + "/" + locale.getLanguage() );
    } catch ( MalformedURLException e ) {
      throw new RuntimeException( e );
    }
  }

  @Nonnull
  public static License get( @Nonnull String id ) {
    for ( CreativeCommonsLicense license : CC_LICENSES ) {
      if ( license.getId().equals( id ) ) {
        return license;
      }
    }

    throw new IllegalArgumentException( "No license found for id <" + id + ">" );
  }

  public enum ModificationsAllowed {
    YES,
    SHARE_ALIKE,
    NO
  }
}
