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

import com.cedarsoft.exceptions.NotFoundException;
import org.fest.assertions.Assertions;
import org.junit.*;

import java.util.Collections;
import java.util.Comparator;

import static org.junit.Assert.*;

/**
 *
 */
public class FileTypeRegistryTest {
  @Test
  public void testIds() throws Exception {
    FileTypeRegistry registry = new FileTypeRegistry( true );
    for ( FileType fileType : registry.getFileTypes() ) {
      Assertions.assertThat( fileType.getId() ).doesNotContain( " " );
    }
  }

  @Test
  public void testParse() {
    FileTypeRegistry fileTypeRegistry = new FileTypeRegistry();

    assertEquals( fileTypeRegistry.parseFileName( "asdf.jpg" ), new FileName( "asdf", ".", "jpg" ) );
  }

  @Test
  public void testParseCase() throws Exception {
    FileTypeRegistry fileTypeRegistry = new FileTypeRegistry();

    assertEquals( fileTypeRegistry.parseFileName( "asdf.jpg" ), new FileName( "asdf", ".", "jpg" ) );
    assertEquals( fileTypeRegistry.parseFileName( "asdf.JPG" ), new FileName( "asdf", ".", "JPG" ) );
    assertEquals( fileTypeRegistry.parseFileName( "ASDF.JPG" ), new FileName( "ASDF", ".", "JPG" ) );
  }

  @Test
  public void testParseEnd() throws Exception {
    FileTypeRegistry fileTypeRegistry = new FileTypeRegistry();

    try {
      fileTypeRegistry.parseFileName( "ASDF.JPGSS" );
      fail( "Where is the Exception" );
    } catch ( NotFoundException e ) {
      assertEquals( "No FileType found for file <ASDF.JPGSS>", e.getMessage() );
    }
  }

  @Test
  public void testIt() {
    FileTypeRegistry registry = new FileTypeRegistry( false );

    registry.store( new FileType( "Canon Raw", "image/cr2", false, new Extension( ".", "cr2" ) ) );
    registry.store( new FileType( "Photoshop", "image/psd", false, new Extension( ".", "psd" ) ) );

    assertEquals( 2, registry.getStoredObjects().size() );
    assertEquals( 2, registry.getFileTypes().size() );
  }

  @Test
  public void testDefaults() {
    FileTypeRegistry registry = new FileTypeRegistry( Collections.<FileType>emptyList(), new Comparator<FileType>() {
      @Override
      public int compare( FileType o1, FileType o2 ) {
        return o1.getId().compareTo( o2.getId() );
      }
    } );

    assertEquals( 0, registry.getStoredObjects().size() );
    registry.ensureDefaultTypesRegistered();
    assertEquals( 6, registry.getStoredObjects().size() );
  }
}
