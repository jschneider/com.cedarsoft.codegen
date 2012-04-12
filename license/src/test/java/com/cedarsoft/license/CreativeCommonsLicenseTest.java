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

import org.junit.*;

import java.net.URL;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import static org.junit.Assert.*;

/**
 *
 */
public class CreativeCommonsLicenseTest {
  @Test
  public void testUrls() throws Exception {
    assertNotNull( License.CC_BY.getUrl() );
    assertNotNull( License.CC_BY_NC.getUrl() );
    assertNotNull( License.CC_BY_NC_ND.getUrl() );
    assertNotNull( License.CC_BY_NC_SA.getUrl() );
    assertNotNull( License.CC_BY_ND.getUrl() );
    assertNotNull( License.CC_BY_SA.getUrl() );

    //Ensure different urls
    Set<URL> urls = new HashSet<URL>();
    for ( CreativeCommonsLicense license : License.CC_LICENSES ) {
      assertNotNull( license );
      urls.add( license.getUrl() );
    }

    assertEquals( 6, urls.size() );
  }

  @Test
  public void testLicenseUrlTranslated() throws Exception {
    assertNotNull( License.CC_BY.getUrl( Locale.GERMAN ) );
    assertEquals( "http://creativecommons.org/licenses/by/3.0/de", License.CC_BY.getUrl( Locale.GERMAN ).toString() );
    assertEquals( "http://creativecommons.org/licenses/by-nc-nd/3.0/de", License.CC_BY_NC_ND.getUrl( Locale.GERMAN ).toString() );
  }

  @Test
  public void testAll() throws Exception {
    assertEquals( 6, License.CC_LICENSES.size() );
  }

  @Test
  public void testCCBy() {
    assertFalse( License.CC_BY.isRestrictedToNonCommercial() );
    assertTrue( License.CC_BY.isDerivedWorkAllowed() );
    assertTrue( License.CC_BY.isSharedAlikeDerivedWorkAllowed() );
    assertTrue( License.CC_BY.isUsableCommercially() );
    assertEquals( CreativeCommonsLicense.ModificationsAllowed.YES, License.CC_BY.getModificationsAllowed() );
  }

  @Test
  public void testCCSA() {
    assertFalse( License.CC_BY_SA.isRestrictedToNonCommercial() );
    assertFalse( License.CC_BY_SA.isDerivedWorkAllowed() );
    assertTrue( License.CC_BY_SA.isSharedAlikeDerivedWorkAllowed() );

    assertTrue( License.CC_BY_SA.isUsableCommercially() );
    assertEquals( CreativeCommonsLicense.ModificationsAllowed.SHARE_ALIKE, License.CC_BY_SA.getModificationsAllowed() );
  }

  @Test
  public void testCCND() {
    assertFalse( License.CC_BY_ND.isRestrictedToNonCommercial() );
    assertFalse( License.CC_BY_ND.isDerivedWorkAllowed() );
    assertFalse( License.CC_BY_ND.isSharedAlikeDerivedWorkAllowed() );

    assertTrue( License.CC_BY_ND.isUsableCommercially() );
    assertEquals( CreativeCommonsLicense.ModificationsAllowed.NO, License.CC_BY_ND.getModificationsAllowed() );
  }

  @Test
  public void testCCNC() {
    assertTrue( License.CC_BY_NC.isRestrictedToNonCommercial() );
    assertTrue( License.CC_BY_NC.isDerivedWorkAllowed() );
    assertTrue( License.CC_BY_NC.isSharedAlikeDerivedWorkAllowed() );

    assertFalse( License.CC_BY_NC.isUsableCommercially() );
    assertEquals( CreativeCommonsLicense.ModificationsAllowed.YES, License.CC_BY_NC.getModificationsAllowed() );
  }

  @Test
  public void testCCNCSA() {
    assertTrue( License.CC_BY_NC_SA.isRestrictedToNonCommercial() );
    assertFalse( License.CC_BY_NC_SA.isDerivedWorkAllowed() );
    assertTrue( License.CC_BY_NC_SA.isSharedAlikeDerivedWorkAllowed() );

    assertFalse( License.CC_BY_NC_SA.isUsableCommercially() );
    assertEquals( CreativeCommonsLicense.ModificationsAllowed.SHARE_ALIKE, License.CC_BY_NC_SA.getModificationsAllowed() );
  }

  @Test
  public void testCCNCND() {
    assertTrue( License.CC_BY_NC_ND.isRestrictedToNonCommercial() );
    assertFalse( License.CC_BY_NC_ND.isDerivedWorkAllowed() );
    assertFalse( License.CC_BY_NC_ND.isSharedAlikeDerivedWorkAllowed() );
    assertFalse( License.CC_BY_NC_ND.isUsableCommercially() );

    assertEquals( CreativeCommonsLicense.ModificationsAllowed.NO, License.CC_BY_NC_ND.getModificationsAllowed() );
  }

  @Test
  public void testResolve() throws Exception {
    assertEquals( 20, CreativeCommonsLicense.LICENSES.size() );
    assertEquals( 6, License.CC_LICENSES.size() );
    assertSame( License.GPL_3, License.get( License.GPL_3.getId() ) );
    assertSame( License.CC_BY_NC_ND, CreativeCommonsLicense.get( License.CC_BY_NC_ND.getId() ) );
  }
}
