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

package com.cedarsoft.crypt;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.*;
import org.junit.rules.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 * <p/>
 * Date: Jul 20, 2007<br>
 * Time: 11:28:04 PM<br>
 */
public class HashTest {
  @Test
  public void testDigestTest() {
    assertNotSame( Algorithm.SHA256.getMessageDigest(), Algorithm.SHA256.getMessageDigest() );
  }

  @Test
  public void testToString() {
    assertThat( Hash.fromHex( Algorithm.MD5, "aabb" ).toString(), is( "[MD5: aabb]" ) );
    assertThat( Hash.fromHex( Algorithm.SHA256, "aabb1111111111" ).toString(), is( "[SHA256: aabb1111111111]" ) );
  }

  @Test
  public void testString() throws Exception {
    assertEquals( "f0e4c2f76c58916ec258f246851bea091d14d4247a2fc3e18694461b1816e13b", HashCalculator.calculate( Algorithm.SHA256, "asdf" ).getValueAsHex() );
    assertEquals( "f0e4c2f76c58916ec258f246851bea091d14d4247a2fc3e18694461b1816e13b", HashCalculator.calculate( Algorithm.SHA256.getMessageDigest(), "asdf" ).getValueAsHex() );
  }

  @Test
  public void testSerialization() throws IOException, ClassNotFoundException {
    Hash hash = Hash.fromHex( Algorithm.SHA256, "1234" );

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    new ObjectOutputStream( out ).writeObject( hash );

    Hash deserialized = ( Hash ) new ObjectInputStream( new ByteArrayInputStream( out.toByteArray() ) ).readObject();
    assertEquals( deserialized, hash );
  }

  @Rule
  public TemporaryFolder tmp = new TemporaryFolder();

  @Test
  public void testIt() throws NoSuchAlgorithmException, IOException {
    URL paris = getClass().getResource( "/paris.jpg" );
    assertNotNull( paris );

    assertEquals( "fbd5f9b6c0fd2035c490e46be0bc3ec3", HashCalculator.calculate( Algorithm.MD5, paris ).getValueAsHex() );//value read using md5sum cmd line tool
    assertEquals( "aa5371938c4190543bddcfc1193a247717feba06", HashCalculator.calculate( Algorithm.SHA1, paris ).getValueAsHex() );//value read using sha1sum cmd line tool

    assertEquals( "aa5371938c4190543bddcfc1193a247717feba06", HashCalculator.calculate( Algorithm.SHA1, IOUtils.toByteArray( paris.openStream() ) ).getValueAsHex() );
    assertEquals( "aa5371938c4190543bddcfc1193a247717feba06", HashCalculator.calculate( Algorithm.SHA1, paris.openStream() ).getValueAsHex() );

    File file = tmp.newFile( "paris.jpg" );
    FileUtils.writeByteArrayToFile( file, IOUtils.toByteArray( paris.openStream() ) );
    assertEquals( "aa5371938c4190543bddcfc1193a247717feba06", HashCalculator.calculate( Algorithm.SHA1, file ).getValueAsHex() );
  }

  @Test
  public void testRound() {
    Hash hash = new Hash( Algorithm.SHA256, "asdf".getBytes() );

    assertEquals( "61736466", hash.getValueAsHex() );
    assertEquals( Hash.fromHex( hash.getAlgorithm(), hash.getValueAsHex() ), hash );
  }

  @Test
  public void testAlgos() throws IOException {
    URL paris = getClass().getResource( "/paris.jpg" );
    assertNotNull( paris );

    for ( Algorithm algorithm : Algorithm.values() ) {
      String value = HashCalculator.calculate( algorithm, paris ).getValueAsHex();
      assertNotNull( value );
      assertTrue( value.length() > 10 );
    }
  }
}
