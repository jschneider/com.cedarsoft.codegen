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

package com.cedarsoft.test.utils;

import com.cedarsoft.crypt.Algorithm;
import com.cedarsoft.crypt.Hash;
import com.cedarsoft.crypt.HashCalculator;
import com.google.common.io.ByteStreams;
import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.io.IOException;
import org.xml.sax.SAXException;

import static org.junit.Assert.*;

/**
 *
 */
public class AssertUtilsTest {
  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  @Rule
  public TemporaryFolder tmp = new TemporaryFolder();
  @Rule
  public TestName testName = new TestName();

  @Test
  public void testJsonFormat() throws Exception {
    String separator = System.getProperty("line.separator");

    assertEquals("{" + separator +
                   "  \"id\" : \"asdfasdf\"," + separator +
                   "  \"unformated\" : true" + separator +
                   "}", JsonUtils.formatJson("{\"id\":\"asdfasdf\",   \"unformated\":true}"));
    assertEquals("{" + separator +
                   "  \"id\" : \"asdfasdf\"," + separator +
                   "  \"unformated\" : true" + separator +
                   "}", JsonUtils.formatJson("{\"id\":\"asdfasdf\", \"unformated\":true}"));
  }

  @Test
  public void testJsonEquals() throws Exception {
    JsonUtils.assertJsonEquals("{\"id\":\"asdfasdf\",   \"unformated\":true}", "{\"id\":\"asdfasdf\",   \"unformated\":true}");
    JsonUtils.assertJsonEquals("{\"id\":\"asdfasdf\",   \"unformated\":true}", "{\"id\":\"asdfasdf\",\"unformated\":true}");
  }

  @Test
  public void testJsonNotEquals() throws Exception {
    expectedException.expect(ComparisonFailure.class);
    JsonUtils.assertJsonEquals( "{\"id\":\"asdfasdf\",   \"unformated\":true}", "{\"id\":\"asdfasdf\",   \"unformated\":false}" );
  }

  @Test
  public void testJsonNotFormatable() throws Exception {
    try {
      JsonUtils.assertJsonEquals( ( String ) null, null );
      fail( "Where is the Exception" );
    } catch ( ComparisonFailure ignore ) {
    }
    try {
      JsonUtils.assertJsonEquals( "affase", "asdf" );
      fail( "Where is the Exception" );
    } catch ( ComparisonFailure ignore ) {
    }
  }

  @Test
  public void testXml() throws Exception {
    expectedException.expect( ComparisonFailure.class );
    AssertUtils.assertXMLEquals( "<xml2/>", "<xml/>" );
  }

  @Test
  public void testAssertWithResource() throws IOException {
    AssertUtils.assertEquals(getClass().getResource("equals.txt"), "the content of the file...\n" +
      "second line!\n" +
      "third line!");

    try {
      AssertUtils.assertEquals( getClass().getResource( "equals.txt" ), "other!" );
      fail( "Where is the Exception" );
    } catch ( AssertionError ignore ) {
    }
  }

  @Test
  public void testXml2() throws Exception {
    expectedException.expect(ComparisonFailure.class);
    AssertUtils.assertXMLEquals( getClass().getResource( "AssertUtilsTest.1.xml" ), "<xml/>" );
  }

  @Test
  public void testXml2WhiteSpaces() throws Exception {
    AssertUtils.assertXMLEquals( getClass().getResource( "AssertUtilsTest.2.xml" ), "<xml/>", true );
  }

  @Test
  public void testXml2WComments() throws Exception {
    try {
      AssertUtils.assertXMLEquals("err", new String(ByteStreams.toByteArray(getClass().getResourceAsStream("AssertUtilsTest.2.xml"))), "<xml><!--comment2--></xml>", true, false);
      fail("Where is the Exception");
    } catch (ComparisonFailure e) {
      Assertions.assertThat(e.getMessage()).startsWith("XML comparison failed expected");
    }

    AssertUtils.assertXMLEquals( "err", new String(ByteStreams.toByteArray(getClass().getResourceAsStream( "AssertUtilsTest.2.xml" ))), "<xml><!--comment2--></xml>", true, true );
  }

  @Test
  public void testFormat() throws Exception {
    expectedException.expect( ComparisonFailure.class );
    AssertUtils.assertXMLEquals( "<xml2/>", "This is no xml!" );
  }

  @Test
  public void testAssertOne() {
    AssertUtils.assertOne( "a", "a", "b", "c" );
    AssertUtils.assertOne( "a", "b", "c", "a" );

    expectedException.expect( AssertionError.class );
    expectedException.expectMessage(
      "Expected: (is \"b\" or is \"c\")\n" +
        "     got: \"a\"" );
    AssertUtils.assertOne( "a", "b", "c" );
  }

  @Test
  public void testTestName() {
    assertEquals( "testTestName", testName.getMethodName() );
  }

  @Test
  public void testFileByHash() throws Exception {
    File file = tmp.newFile( "daFile" );
    FileUtils.writeStringToFile( file, "daContent" );

    Hash expected = HashCalculator.calculate( Algorithm.MD5, file );
    assertEquals( "913aa4a45cea16f9714f109e7324159f", expected.getValueAsHex() );

    AssertUtils.assertFileByHash( "path", expected, file );
    AssertUtils.assertFileByHash( "path", HashCalculator.calculate( Algorithm.MD5, file ), file );
    AssertUtils.assertFileByHash( "path", HashCalculator.calculate( Algorithm.SHA256, file ), file );
    AssertUtils.assertFileByHash( "path", HashCalculator.calculate( Algorithm.SHA1, file ), file );
    AssertUtils.assertFileByHash( "path", HashCalculator.calculate( Algorithm.SHA512, file ), file );

    try {
      AssertUtils.assertFileByHash( AssertUtilsTest.class, testName.getMethodName(), Hash.fromHex( Algorithm.MD5, "aa" ), file );
      fail( "Where is the Exception" );
    } catch ( AssertionError e ) {
      assertTrue(e.getMessage().trim(),
                 e.getMessage().contains("Stored questionable file under test at <")
                   &&
                   e.getMessage().contains("com.cedarsoft.test.utils.AssertUtilsTest" + File.separator + "testFileByHash" + File.separator + "daFile")
                   &&
                   e.getMessage().contains(
                     "Expected: is <[MD5: 913aa4a45cea16f9714f109e7324159f]>\n" +
                       "     got: <[MD5: aa]>")
      );

      File copiedFile = AssertUtils.createCopyFile(AssertUtils.createPath(AssertUtilsTest.class, "testFileByHash"), "daFile");
      assertTrue(e.getMessage().contains(copiedFile.getAbsolutePath()));

      assertTrue(copiedFile.getParentFile().exists());
      assertTrue(copiedFile.exists());
      assertEquals("daContent", FileUtils.readFileToString(copiedFile));
    }

    try {
      AssertUtils.assertFileByHash( Hash.fromHex( Algorithm.MD5, "aa" ), file );
      fail( "Where is the Exception" );
    } catch ( AssertionError e ) {
      assertTrue( e.getMessage().trim(),
                  e.getMessage().contains( "Stored questionable file under test at <" )
                    &&
                    e.getMessage().contains( "com.cedarsoft.test.utils.AssertUtilsTest" + File.separator + "testFileByHash" + File.separator +
                      "daFile" )
                    &&
                    e.getMessage().contains(
                      "Expected: is <[MD5: 913aa4a45cea16f9714f109e7324159f]>\n" +
                        "     got: <[MD5: aa]>" )
      );
    }
  }

  @Test
  public void testGuessPath() throws Exception {
    assertEquals( AssertUtilsTest.class.getName() + File.separator + "testGuessPath", guess() );
    assertEquals( "com.cedarsoft.test.utils.AssertUtilsTest" + File.separator + "testGuessPath", guess() );
  }

  @Test
  public void testGuessPath2() throws Exception {
    assertEquals( AssertUtilsTest.class.getName() + File.separator + "testGuessPath2", guess() );
  }

  //this method is necessary to simulate the call to AssertUtils.assertFileByHash

  private String guess() {
    return AssertUtils.guessPathFromStackTrace();
  }
}
