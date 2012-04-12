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

package com.cedarsoft.configuration.xstream;

import com.cedarsoft.configuration.DefaultConfigurationManager;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

/**
 *
 */
public class XStreamTest {
  private DefaultConfigurationManager manager;
  private XStreamPersister persister;

  @Before
  public void setUp() throws Exception {
    manager = new DefaultConfigurationManager();
    persister = new XStreamPersister();
  }

  @Test
  public void testEncoding() {
    assertEquals( "<?xml version=\"1.0\"?>", persister.createXmlHeader( null ) );
    assertEquals( "<?xml version=\"1.0\" encoding=\"ENCODING\"?>", persister.createXmlHeader( "ENCODING" ) );
  }

  @Test
  public void testWrite() throws IOException {
    manager.addConfiguration( new MyModuleConfiguration( "asdf" ) );

    final StringWriter out = new StringWriter();

    persister.persist( manager, out );
    assertEquals( "<?xml version=\"1.0\"?>\n" +
      "<configurations>\n" +
      "  <com.cedarsoft.configuration.xstream.XStreamTest_-MyModuleConfiguration>\n" +
      "    <name>asdf</name>\n" +
      "  </com.cedarsoft.configuration.xstream.XStreamTest_-MyModuleConfiguration>\n" +
      "</configurations>", out.toString() );
  }

  @Test
  public void testFile() throws IOException {
    manager.addConfiguration( new MyModuleConfiguration( "asdf" ) );

    File file = File.createTempFile( "devtoolsConfig", ".xml" );
    persister.persist( manager, file );

    assertEquals( "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
      "<configurations>\n" +
      "  <com.cedarsoft.configuration.xstream.XStreamTest_-MyModuleConfiguration>\n" +
      "    <name>asdf</name>\n" +
      "  </com.cedarsoft.configuration.xstream.XStreamTest_-MyModuleConfiguration>\n" +
      "</configurations>", FileUtils.readFileToString( file ) );


    DefaultConfigurationManager deserialized = new DefaultConfigurationManager( persister.load( file ) );
    assertEquals( 1, deserialized.getConfigurations().size() );
  }

  @Test
  public void testManager() {
    try {
      manager.getConfiguration( MyModuleConfiguration.class );
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
    }

    manager.addConfiguration( new MyModuleConfiguration( "asdf" ) );

    assertNotNull( manager.getConfiguration( MyModuleConfiguration.class ) );
    assertSame( manager.getConfiguration( MyModuleConfiguration.class ), manager.getConfiguration( MyModuleConfiguration.class ) );
  }

  @Test
  public void testIt() throws IOException {
    assertEquals( "<?xml version=\"1.0\"?>\n" +
      "<configurations/>", persister.persist( manager ) );
  }

  @Test
  public void testNotSoSimple() throws IOException {
    manager.addConfiguration( new MyModuleConfiguration( "asdf" ) );
    assertNotNull( manager.getConfiguration( MyModuleConfiguration.class ) );

    String serialized = persister.persist( manager );
    assertEquals( "<?xml version=\"1.0\"?>\n" +
      "<configurations>\n" +
      "  <com.cedarsoft.configuration.xstream.XStreamTest_-MyModuleConfiguration>\n" +
      "    <name>asdf</name>\n" +
      "  </com.cedarsoft.configuration.xstream.XStreamTest_-MyModuleConfiguration>\n" +
      "</configurations>", serialized );

    DefaultConfigurationManager deserialized = new DefaultConfigurationManager( persister.load( serialized ) );
    assertNotNull( deserialized );
    assertNotNull( deserialized.getConfiguration( MyModuleConfiguration.class ) );

  }

  @Test
  public void testOtherConfig() throws IOException {
    manager.addConfiguration( new MyModuleConfiguration( "theName" ) );

    String serialized = persister.persist( manager );
    assertEquals( "<?xml version=\"1.0\"?>\n" +
      "<configurations>\n" +
      "  <com.cedarsoft.configuration.xstream.XStreamTest_-MyModuleConfiguration>\n" +
      "    <name>theName</name>\n" +
      "  </com.cedarsoft.configuration.xstream.XStreamTest_-MyModuleConfiguration>\n" +
      "</configurations>", serialized );

    DefaultConfigurationManager deserialized = new DefaultConfigurationManager( persister.load( serialized ) );
    assertNotNull( deserialized );
    assertNotNull( deserialized.getConfiguration( MyModuleConfiguration.class ) );
    assertEquals( "theName", deserialized.getConfiguration( MyModuleConfiguration.class ).getName() );
  }

  private static class MyModuleConfiguration {
    private String name;

    private MyModuleConfiguration( String name ) {
      this.name = name;
    }

    public String getName() {
      return name;
    }
  }
}
