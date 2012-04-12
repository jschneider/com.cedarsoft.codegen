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

import com.cedarsoft.test.utils.AssertUtils;
import org.apache.commons.configuration.HierarchicalConfiguration;
import org.apache.commons.configuration.XMLConfiguration;
import org.apache.commons.configuration.tree.ConfigurationNode;
import org.apache.commons.io.FileUtils;
import org.junit.*;
import org.junit.rules.*;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.*;

/**
 *
 */
public class XmlConfigurationManagerTest {
  @Rule
  public TemporaryFolder tmp = new TemporaryFolder();
  private XMLConfiguration configuration;
  private XmlConfigurationManager manager;
  private File file;

  @Before
  public void setUp() throws Exception {
    configuration = new XMLConfiguration();
    manager = new XmlConfigurationManager( configuration );
    file = tmp.newFile( "config.xml" );
    configuration.setFile( file );
  }

  @Test
  public void testOutput() throws IOException, SAXException {
    manager.getConfiguration().setProperty( "daKey", "daValue" );
    manager.getModuleConfiguration( String.class ).setProperty( "daStringKey", "daStringValue" );
    manager.save();

    AssertUtils.assertXMLEquals("<configuration>\n" +
                                  "  <daKey>daValue</daKey>\n" +
                                  "  <modules>\n" +
                                  "    <java>\n" +
                                  "      <lang>\n" +
                                  "        <String>\n" +
                                  "          <daStringKey>daStringValue</daStringKey>\n" +
                                  "        </String>\n" +
                                  "      </lang>\n" +
                                  "    </java>\n" +
                                  "  </modules>\n" +
                                  "</configuration>", FileUtils.readFileToString(file));
  }


  @Test
  public void testGetConfiguration() throws Exception {
    assertNotNull( manager.getConfiguration() );
    ConfigurationNode rootNode = manager.getConfiguration().getRootNode();
    assertEquals( 0, rootNode.getChildren().size() );

    assertNotNull( manager.getModuleConfiguration( Integer.class ) );
    assertEquals( 1, rootNode.getChildren().size() );

    HierarchicalConfiguration stringConfig = manager.getModuleConfiguration( String.class );
    stringConfig.setProperty( "daProp", 7 );
    assertEquals( 7, manager.getModuleConfiguration( String.class ).getProperty( "daProp" ) );
  }
}
