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

package com.cedarsoft.execution;

import org.junit.*;

import java.io.File;
import java.util.List;

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: Jun 18, 2007<br>
 * Time: 1:49:47 PM<br>
 */
public class JavaCommandBuilderTest {
  private JavaCommandBuilder starter;

  @Before
  public void setUp() throws Exception {
    starter = new JavaCommandBuilder( "mainClass" );
  }

  @Test
  public void testClasspath() {
    String[] classPathElements = new String[]{"a", "b", "", "c"};
    starter.setClassPathElements( classPathElements );
    assertEquals( 3, starter.getClassPathElements().size() );

    assertEquals( "a" + File.pathSeparator + "b" + File.pathSeparator + "c", starter.getClassPath() );

    starter.addClassPathElement( "d" );
    assertEquals( "a" + File.pathSeparator + "b" + File.pathSeparator + "c" + File.pathSeparator + "d", starter.getClassPath() );
  }

  @Test
  public void testVmProperties() {
    starter.setVmProperties( "key=value", "", "key2=value2" );

    assertEquals( 2, starter.getVmProperties().size() );
    assertEquals( "key=value", starter.getVmProperties().get( 0 ) );
    assertEquals( "key2=value2", starter.getVmProperties().get( 1 ) );

    starter.addVmProperty( "d=e" );
    assertEquals( "d=e", starter.getVmProperties().get( 2 ) );
  }

  @Test
  public void testArguments() {
    starter.setArguments( "a", "b", "", "c" );
    assertEquals( 3, starter.getArguments().size() );
  }

  @Test
  public void testAll() {
    assertEquals( "java mainClass", starter.getCommandLine() );

    starter.setClassPathElements( "a", "b" );
    assertEquals( "java -cp a" + File.pathSeparator + "b mainClass", starter.getCommandLine() );

    starter.setArguments( "arg", "arg1" );
    assertEquals( "java -cp a" + File.pathSeparator + "b mainClass arg arg1", starter.getCommandLine() );

    starter.setVmProperties( "prop0", "prop1" );
    assertEquals( "java -Dprop0 -Dprop1 -cp a" + File.pathSeparator + "b mainClass arg arg1", starter.getCommandLine() );


    List<String> elements = starter.getCommandLineElements();
    assertEquals( 8, elements.size() );
    assertEquals( "java", elements.get( 0 ) );
    assertEquals( "-Dprop0", elements.get( 1 ) );
    assertEquals( "-Dprop1", elements.get( 2 ) );
    assertEquals( "-cp", elements.get( 3 ) );
    assertEquals( "a" + File.pathSeparator + "b", elements.get( 4 ) );
    assertEquals( "mainClass", elements.get( 5 ) );
    assertEquals( "arg", elements.get( 6 ) );
    assertEquals( "arg1", elements.get( 7 ) );
  }
}
