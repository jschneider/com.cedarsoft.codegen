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

import org.junit.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: 25.08.2006<br>
 * Time: 17:07:01<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class FileCopyTest {
  private File tmp;
  private File myFile;


  @Before
  public void setUp() throws Exception {
    tmp = new File( "/tmp/fileCopyTestTemp" );
    tmp.mkdir();
    myFile = new File( tmp, "test.txt" );

    FileOutputStream out = new FileOutputStream( myFile );
    out.write( 60 );
    out.close();
  }

  @Test
  public void testCopySimple() throws IOException {
    File dst = new File( "/tmp/fileCopyTestTempTarget" );
    FileCopyManager.copy( tmp, dst );
    assertTrue( dst.exists() );
    assertEquals( 1, dst.list().length );
    assertEquals( "test.txt", dst.list()[0] );
  }

  @After
  public void tearDown() throws Exception {
    myFile.delete();
    tmp.delete();


  }
}
