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
import org.apache.commons.io.FileUtils;
import org.fest.assertions.Assertions;
import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.io.IOException;

import static com.cedarsoft.test.utils.AssertUtils.assertFileByHash;
import static com.cedarsoft.test.utils.AssertUtils.assertFileByHashes;
import static org.junit.Assert.*;

/**
 *
 */
public class AssertUtilsFileCompareTest {
  @Rule
  public ExpectedException expectedException = ExpectedException.none();
  @Rule
  public final TemporaryFolder tmp = new TemporaryFolder();
  private File fileA;
  private File fileB;

  @Before
  public void setUp() throws Exception {
    fileA = tmp.newFile("a");
    FileUtils.writeStringToFile(fileA, "daContent");

    fileB = tmp.newFile("b");
    FileUtils.writeStringToFile(fileB, "daContentB");
  }

  @Test
  public void testCopyFile() {
    File dir = AssertUtils.FAILED_FILES_DIR;
    assertEquals(new File(dir, "daPath" + File.separator + "daName"), AssertUtils.createCopyFile("daPath", "daName"));
    assertEquals(new File(dir, "daPath2" + File.separator + "other" + File.separator + "daName2"), AssertUtils.createCopyFile("daPath2" + File.separator + "other", "daName2"));
  }

  @Test
  public void testBasic() throws IOException {
    assertFileByHash(Hash.fromHex(Algorithm.MD5, "913aa4a45cea16f9714f109e7324159f"), fileA);
  }

  @Test
  public void testInvalid() throws IOException {
    expectedException.expect(IllegalArgumentException.class);
    assertFileByHashes(fileA);
  }

  @Test
  public void testOneOf() throws IOException {
    assertFileByHashes(fileA,
                       Hash.fromHex(Algorithm.MD5, "913aa4a45cea16f9714f109e7324159f"),
                       Hash.fromHex(Algorithm.MD5, "AA"),
                       Hash.fromHex(Algorithm.MD5, "BB")
    );
  }

  @Test
  public void testOneOf2() throws IOException {
    assertFileByHashes(fileA,
                       Hash.fromHex(Algorithm.MD5, "AA"),
                       Hash.fromHex(Algorithm.MD5, "913aa4a45cea16f9714f109e7324159f"),
                       Hash.fromHex(Algorithm.MD5, "BB")
    );
  }

  @Test
  public void testOneOf4() throws IOException {
    assertFileByHashes(fileA, Algorithm.MD5,
                       "AA",
                       "913aa4a45cea16f9714f109e7324159f",
                       "BB"
    );
  }

  @Test
  public void testOneOfFail() throws Exception {
    try {
      assertFileByHashes(fileA,
                         Hash.fromHex(Algorithm.MD5, "AA"),
                         Hash.fromHex(Algorithm.MD5, "CC"),
                         Hash.fromHex(Algorithm.MD5, "BB")
      );
    } catch (AssertionError e) {
      Assertions.assertThat(e.getMessage()).startsWith("Stored questionable file under test at <");
      Assertions.assertThat(e.getMessage()).contains("Expected: is <[[MD5: aa], [MD5: cc], [MD5: bb]]>\n" +
                                                       "     got: <[[MD5: 913aa4a45cea16f9714f109e7324159f], [MD5: 913aa4a45cea16f9714f109e7324159f], [MD5: 913aa4a45cea16f9714f109e7324159f]]>\n");
    }
  }

  @Test
  public void testFail() throws Exception {
    try {
      assertFileByHash(Hash.fromHex(Algorithm.MD5, "AA"), fileA);
    } catch (AssertionError e) {
      Assertions.assertThat(e.getMessage()).startsWith("Stored questionable file under test at <");
      Assertions.assertThat(e.getMessage()).contains("Expected: is <[MD5: 913aa4a45cea16f9714f109e7324159f]>\n" +
                                                       "     got: <[MD5: aa]>\n");
    }
  }
}
