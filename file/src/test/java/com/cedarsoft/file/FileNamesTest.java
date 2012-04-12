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

package com.cedarsoft.file;

import com.cedarsoft.test.utils.TestUtils;
import org.apache.commons.io.FileUtils;
import org.junit.*;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 *
 */
public class FileNamesTest {
  @Test
  public void testIt2() {
    FileNames fileNames = new FileNames();

    assertEquals( 0, fileNames.getFileNames().size() );
    fileNames.add( new FileName( "asdf", "txt" ) );
    assertEquals( 1, fileNames.getFileNames().size() );
    fileNames.add( new FileName( "asdf", "jpg" ) );
    assertEquals( 2, fileNames.getFileNames().size() );

    try {
      fileNames.add( new FileName( "asdf", "jpg" ) );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }
    assertEquals( 2, fileNames.getFileNames().size() );
  }

  @Test
  public void testIt() {
    FileNames fileNames = new FileNames();

    fileNames.add( new FileName( "img_0001", ".", "jpg" ) );
    fileNames.add( new FileName( "img_0001", ".", "cr2" ) );
    fileNames.add( new FileName( "img_0002", ".", "jpg" ) );
    fileNames.add( new FileName( "img_0002", ".", "cr2" ) );
    fileNames.add( new FileName( "img_0003", ".", "jpg" ) );

    assertEquals( 5, fileNames.getFileNames().size() );
  }

  @Test
  public void testParse() throws IOException {
    File dir = TestUtils.createEmptyTmpDir();

    try {
      FileUtils.touch( new File( dir, "1.jpg" ) );
      FileUtils.touch( new File( dir, "2.jpg" ) );
      FileUtils.touch( new File( dir, "3.jpg" ) );
      FileUtils.touch( new File( dir, "4.jpg" ) );
      FileUtils.touch( new File( dir, "5.jpg" ) );
      FileUtils.touch( new File( dir, "5.cr2" ) );

      FileNames fileNames = new FileNamesFactory( new FileTypeRegistry() ).create( dir );

      assertEquals( 6, fileNames.getFileNames().size() );
    } finally {
      FileUtils.deleteDirectory( dir );
    }
  }
}
