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

import org.apache.commons.io.FilenameUtils;
import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 */
public class FileNameTest {
  @Test
  public void testDel() {
    assertEquals( "asdf", new FileName( "asdf", ".", "jpg" ).getBaseName().getName() );
    assertEquals( "jpg", new FileName( "asdf", ".", "jpg" ).getExtension().getExtension() );
    assertEquals( ".", new FileName( "asdf", ".", "jpg" ).getExtension().getDelimiter() );

    assertEquals( "asdf.jpg", new FileName( "asdf", ".", "jpg" ).getName() );
    assertEquals( "asdf.jpg", new FileName( "asdf", ".", "jpg" ).toString() );
  }

  @Test
  public void testCase() {
    assertEquals( "asdf", new FileName( "asdf", ".", "jpg" ).getBaseName().getName() );
    assertEquals( "aSdf", new FileName( "aSdf", ".", "JPG" ).getBaseName().getName() );

    assertEquals( "jpg", new FileName( "asdf", ".", "jpg" ).getExtension().getExtension() );
    assertEquals( "JPG", new FileName( "asdf", ".", "JPG" ).getExtension().getExtension() );

    assertEquals( ".", new FileName( "asdf", ".", "jpg" ).getExtension().getDelimiter() );
  }

  @Test
  public void testFileNameUtils() {
    assertEquals( "", FilenameUtils.getExtension( "asdf" ) );
    assertEquals( "", FilenameUtils.getExtension( "asdf." ) );

    assertEquals( "asdf", FilenameUtils.getBaseName( "asdf." ) );
  }
}
