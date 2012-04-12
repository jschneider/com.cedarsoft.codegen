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

package com.cedarsoft.configuration.xml;

import com.cedarsoft.configuration.DefaultValueProvider;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

import javax.annotation.Nonnull;
import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: Jun 28, 2007<br>
 * Time: 4:04:13 PM<br>
 */
public class ConfigurationAccessTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private Configuration configuration;

  @Before
  public void setUp() throws Exception {
    configuration = new BaseConfiguration();
  }

  @Test
  public void testMap() {
    Map<String, String> defaults = new HashMap<String, String>();
    defaults.put( "a", "ab" );
    defaults.put( "b", "bb" );
    ConfigurationAccess<Map> configurationAccess = new ConfigurationAccess<Map>( configuration, Map.class, "key", defaults );

    //Default Value
    assertEquals( 2, configurationAccess.resolve().size() );

    Map<String, String> newMap = new HashMap<String, String>();
    newMap.put( "c", "cc" );
    newMap.put( "d", "dd" );
    newMap.put( "e", "ee" );
    configurationAccess.store( newMap );

    assertEquals( 3, newMap.size() );
    assertEquals( "cc", configurationAccess.resolve().get( "c" ) );
    assertEquals( "dd", configurationAccess.resolve().get( "d" ) );
    assertEquals( "ee", configurationAccess.resolve().get( "e" ) );
  }

  @Test
  public void testWrite() {
    ConfigurationAccess<String> configurationAccess = new ConfigurationAccess<String>( configuration, String.class, "key", "defValue" );
    assertEquals( "defValue", configurationAccess.resolve() );
    assertEquals( "defValue", configuration.getString( "key" ) );

    configurationAccess.store( "newValue" );

    assertEquals( "newValue", configurationAccess.resolve() );
    assertEquals( "newValue", configuration.getString( "key" ) );
  }

  @Test
  public void testList() {
    assertEquals( 0, ( ( List<String> ) configuration.getList( "asdf" ) ).size() );

    configuration.addProperty( "asdf", Arrays.asList( "a", "b", "c" ) );
    assertEquals( 3, ( ( List<String> ) configuration.getList( "asdf" ) ).size() );

    ConfigurationAccess<List> configurationAccess = new ConfigurationAccess<List>( configuration, List.class, "asdf", new DefaultValueProvider() {
      @Override
      @Nonnull
      public <T> T getDefaultValue( @Nonnull String key, @Nonnull Class<T> type ) {
        throw new UnsupportedOperationException();
      }
    } );

    List<String> result = configurationAccess.resolve();
    assertEquals( 3, result.size() );
  }

  @Test
  public void testCallback() {
    DefaultValueProvider defaultValueProvider = new DefaultValueProvider() {
      @Override
      @Nonnull
      public <T> T getDefaultValue( @Nonnull String key, @Nonnull Class<T> type ) {
        return type.cast( "1234" );
      }
    };
    ConfigurationAccess<String> configurationAccess = new ConfigurationAccess<String>( configuration, String.class, "key", defaultValueProvider );
    assertEquals( "1234", configurationAccess.resolve() );

    assertEquals( "1234", configuration.getString( "key" ) );
  }

  @Test
  public void testStringResolver() {
    ConfigurationAccess<String> configurationAccess = new ConfigurationAccess<String>( configuration, String.class, "key", "defValue" );

    assertEquals( "defValue", configurationAccess.resolve() );
    configuration.setProperty( "key", "2" );
    assertEquals( "2", configurationAccess.resolve() );
    configuration.setProperty( "key", 2 );

    thrown.expect( Exception.class );
    configurationAccess.resolve();
  }

  @Test
  public void testIntResolver() {
    ConfigurationAccess<Integer> configurationAccess = new ConfigurationAccess<Integer>( configuration, Integer.class, "key", 5 );
    assertEquals( new Integer( 5 ), configurationAccess.resolve() );
    configuration.setProperty( "key", 2 );
    assertEquals( new Integer( 2 ), configurationAccess.resolve() );
  }

  @Test
  public void testDoubleResolver() {
    ConfigurationAccess<Double> configurationAccess = new ConfigurationAccess<Double>( configuration, Double.class, "key", 5.0 );
    assertEquals( new Double( 5 ), configurationAccess.resolve() );
    configuration.setProperty( "key", 2.0 );
    assertEquals( new Double( 2 ), configurationAccess.resolve() );
  }

  @Rule
  public TemporaryFolder tmp = new TemporaryFolder();

  @Test
  public void testFileResolver() throws IOException {
    File file = tmp.newFile( "daFile" );

    ConfigurationAccess<File> configurationAccess = new ConfigurationAccess<File>( configuration, File.class, "key", file );
    assertEquals( file, configurationAccess.resolve() );
    configuration.setProperty( "key", file.getAbsolutePath() );
    assertEquals( file, configurationAccess.resolve() );
  }
}
