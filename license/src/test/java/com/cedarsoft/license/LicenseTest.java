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

import org.fest.assertions.Assertions;
import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 */
public class LicenseTest {
  @Test
  public void testEquals() {
    assertEquals( License.ALL_RIGHTS_RESERVED, License.ALL_RIGHTS_RESERVED );
    assertFalse( License.PUBLIC_DOMAIN.equals( License.ALL_RIGHTS_RESERVED ) );
    assertFalse( License.PUBLIC_DOMAIN.equals( null ) );
    assertFalse( License.PUBLIC_DOMAIN.equals( "asdf" ) );
  }

  @Test
  public void testUrl() {
    assertNull( License.ALL_RIGHTS_RESERVED.getUrl() );
    assertNull( License.PUBLIC_DOMAIN.getUrl() );
    assertNull( License.UNKNOWN.getUrl() );

    assertNotNull( License.CDDL.getUrl() );
  }

  @Test
  public void testResolve() throws Exception {
    assertEquals( 20, License.LICENSES.size() );
    assertSame( License.GPL_3, License.get( License.GPL_3.getId() ) );
    assertSame( License.CC_BY_NC_ND, License.get( License.CC_BY_NC_ND.getId() ) );
  }

  @Test
  public void testToString() throws Exception {
    Assertions.assertThat( License.APACHE_20.toString() ).isEqualTo( "Apache License 2.0 (APACHE 2.0)" );
  }
}
