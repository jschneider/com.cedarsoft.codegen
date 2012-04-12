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

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.junit.*;
import org.junit.rules.*;

import static org.junit.Assert.*;

/**
 *
 */
public class PasswordUtilsTest {
  @Test
  public void testIt() throws DecoderException {
    assertEquals( "1a1dc91c907325c69271ddf0c944bc72", new String( Hex.encodeHex( PasswordUtils.calculateMD5Hash( "pass" ) ) ) );
    assertEquals( "ea847988ba59727dbf4e34ee75726dc3", new String( Hex.encodeHex( PasswordUtils.calculateMD5Hash( "topsecret" ) ) ) );

    assertTrue( PasswordUtils.hasExpectedHash( "pass", Hex.decodeHex( "1a1dc91c907325c69271ddf0c944bc72".toCharArray() ) ) );
    assertTrue( PasswordUtils.hasExpectedHash( "topsecret", Hex.decodeHex( "ea847988ba59727dbf4e34ee75726dc3".toCharArray() ) ) );

    assertFalse( PasswordUtils.hasExpectedHash( "topsecret", null ) );
    assertFalse( PasswordUtils.hasExpectedHash( "topsecret", Hex.decodeHex( "1234".toCharArray() ) ) );
  }

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void testEx() throws InvalidPasswordException {
    PasswordUtils.validatePasswordHash( "a".getBytes(), "a".getBytes() );

    expectedException.expect( InvalidPasswordException.class );
    PasswordUtils.validatePasswordHash( "a".getBytes(), "b".getBytes() );
  }

  @Test
  public void testEx2() throws InvalidPasswordException {
    expectedException.expect( InvalidPasswordException.class );
    PasswordUtils.validatePasswordHash( null, null );
  }

  @Test
  public void testEx3() throws InvalidPasswordException {
    expectedException.expect( InvalidPasswordException.class );
    PasswordUtils.validatePasswordHash( "a".getBytes(), "ab".getBytes() );
  }

  @Test
  public void testEx4() throws InvalidPasswordException {
    expectedException.expect( InvalidPasswordException.class );
    PasswordUtils.validatePasswordHash( "a".getBytes(), null );
  }
}
