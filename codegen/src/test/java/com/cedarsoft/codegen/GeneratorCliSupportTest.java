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

package com.cedarsoft.codegen;

import javax.annotation.Nonnull;
import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;

import static org.junit.Assert.*;

/**
 *
 */
public class GeneratorCliSupportTest {
  private StringWriter out;
  private GeneratorCliSupport support;

  @Rule
  public TemporaryFolder tmp = new TemporaryFolder();

  @Before
  public void setUp() throws Exception {
    out = new StringWriter();
    support = new GeneratorCliSupport( new AbstractGenerator() {
      @Nonnull
      @Override
      protected String getRunnerClassName() {
        return "com.cedarsoft.codegen.GeneratorCliSupportTest$MyRunner";
      }
    }, "daCommand", new PrintWriter( out ) );
  }

  @Test
  public void testIt() throws Exception {
    support.run( new String[0] );
    assertEquals( "Missing required options: d, t\n" +
      "usage: daCommand -d <serializer dest dir> -t <test dest dir> path-to-class\n" +
      "-d,--destination <arg>               the output directory for the created\n" +
      "                                     classes\n" +
      "-h,--help                            display this use message\n" +
      "-r,--resources-destination <arg>     the output directory for the created\n" +
      "                                     resources\n" +
      "-s,--test-resources-destination <arg>the output directory for the created\n" +
      "                                     test resources\n" +
      "-t,--test-destination <arg>          the output directory for the created\n" +
      "                                     tests", out.toString().trim() );
  }

  @Test
  public void testHelp() throws Exception {
    support.run( new String[]{"-h", "-d", "asdf", "-t", "asfff"} );
    assertEquals(
        "usage: daCommand -d <serializer dest dir> -t <test dest dir> path-to-class\n" +
        "-d,--destination <arg>               the output directory for the created\n" +
        "                                     classes\n" +
        "-h,--help                            display this use message\n" +
        "-r,--resources-destination <arg>     the output directory for the created\n" +
        "                                     resources\n" +
        "-s,--test-resources-destination <arg>the output directory for the created\n" +
        "                                     test resources\n" +
        "-t,--test-destination <arg>          the output directory for the created\n" +
        "                                     tests", out.toString().trim() );
  }

  @Test
  public void testResDest() throws Exception {
    URL resource = getClass().getResource( "/com/cedarsoft/codegen/model/test/Window.java" );
    assertNotNull( resource );
    File javaFile = new File( resource.toURI() );
    assertTrue( javaFile.exists() );

    support.run( new String[]{
      "-d", tmp.newFolder( "dest" ).getAbsolutePath(),
      "-r", tmp.newFolder( "dest-resources" ).getAbsolutePath(),
      "-t", tmp.newFolder( "test-dest" ).getAbsolutePath(),
      "-s", tmp.newFolder( "test-dest-resources" ).getAbsolutePath(),
      javaFile.getAbsolutePath()} );
    assertEquals( "called...", out.toString().trim() );
  }

  @Test
  public void testDefault() throws Exception {
    URL resource = getClass().getResource( "/com/cedarsoft/codegen/model/test/Window.java" );
    assertNotNull( resource );
    File javaFile = new File( resource.toURI() );
    assertTrue( javaFile.exists() );

    support.run( new String[]{"-d", tmp.newFolder( "dest" ).getAbsolutePath(), "-t", tmp.newFolder( "test-dest" ).getAbsolutePath(), javaFile.getAbsolutePath()} );
    assertEquals( "called...", out.toString().trim() );
  }

  public static class MyRunner implements AbstractGenerator.Runner {
    @Override
    public void generate( @Nonnull GeneratorConfiguration configuration ) throws Exception {
      configuration.getLogOut().write( "called..." );
      assertNotNull( configuration.getResourcesDestination() );
      assertNotNull( configuration.getTestResourcesDestination() );
    }
  }
}
