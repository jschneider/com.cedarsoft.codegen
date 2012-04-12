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

package com.cedarsoft.provider;

import org.apache.commons.io.FileUtils;
import javax.annotation.Nonnull;
import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.Assert.*;

/**
 *
 */
public class ProviderTest {
  @Test
  public void testException() {
    Provider<String, IOException> provider = new Provider<String, IOException>() {
      @Override
      @Nonnull
      public String provide() throws IOException {
        throw new IOException( "Uuups" );
      }

      @Override
      @Nonnull
      public String getDescription() {
        return "asdf";
      }
    };

    try {
      provider.provide();
      fail( "Where is the Exception" );
    } catch ( IOException ignore ) {
    }
  }

  @Rule
  public TemporaryFolder tmp = new TemporaryFolder();

  @Test
  public void testINputStream() throws IOException {
    File file = tmp.newFile( "asdf" );
    FileUtils.writeStringToFile( file, "a" );

    InputStreamFromFileProvider provider = new InputStreamFromFileProvider( file );
    assertTrue( provider.getDescription().endsWith( "asdf" ) );

    {
      InputStream stream = provider.provide();
      assertEquals( 'a', stream.read() );
      assertEquals( -1, stream.read() );
    }

    {
      InputStream stream = provider.provide();
      assertEquals( 'a', stream.read() );
      assertEquals( -1, stream.read() );
    }
  }

  @Test
  public void testContext() throws IOException {
    AbstractContextualProvider<String, Integer, IOException> provider = new AbstractContextualProvider<String, Integer, IOException>() {
      @Nonnull
      @Override
      public String provide( @Nonnull Integer context ) throws IOException {
        return String.valueOf( context );
      }

      @Nonnull
      @Override
      public String getDescription( @Nonnull Integer context ) {
        return "descr";
      }
    };

    assertEquals( "7", provider.createProvider( 7 ).provide() );
    assertEquals( "8", provider.createProvider( 8 ).provide() );
  }
}
