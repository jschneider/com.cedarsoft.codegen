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

package com.cedarsoft.codegen;

import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 */
public class GeneratorConfigurationTest {
  @Test
  public void testModes() {
    assertTrue( GeneratorConfiguration.CreationMode.ALL.isCreate() );
    assertTrue( GeneratorConfiguration.CreationMode.ALL.isCreateTests() );

    assertTrue( GeneratorConfiguration.CreationMode.NO_TESTS.isCreate() );
    assertFalse( GeneratorConfiguration.CreationMode.NO_TESTS.isCreateTests() );

    assertFalse( GeneratorConfiguration.CreationMode.TESTS_ONLY.isCreate() );
    assertTrue( GeneratorConfiguration.CreationMode.TESTS_ONLY.isCreateTests() );
  }

  @Test
  public void testValueOf() {
    assertEquals( GeneratorConfiguration.CreationMode.ALL, GeneratorConfiguration.CreationMode.get( true, true ) );
    assertEquals( GeneratorConfiguration.CreationMode.TESTS_ONLY, GeneratorConfiguration.CreationMode.get( false, true ) );
    assertEquals( GeneratorConfiguration.CreationMode.NO_TESTS, GeneratorConfiguration.CreationMode.get( true, false ) );
  }


}
