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

package com.cedarsoft.codegen.parser;

import com.cedarsoft.codegen.mock.AnnotationProcessorEnvironmentMock;
import com.sun.mirror.declaration.ClassDeclaration;

import javax.annotation.Nonnull;
import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 *
 */
public class ParserTest {
  private File javaFile0;
  private File javaFile1;
  private File javaFile2;
  private File javaFile3;
  private File javaFile4;

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Before
  public void setUp() throws Exception {
    javaFile0 = getResourceAsFile( "/com/cedarsoft/codegen/model/test/FieldTypesInit.java" );
    javaFile1 = getResourceAsFile( "/com/cedarsoft/codegen/model/test/Door.java" );
    javaFile2 = getResourceAsFile( "/com/cedarsoft/codegen/model/test/Room.java" );

    javaFile3 = getResourceAsFile( "/com/cedarsoft/codegen/model/test/SubClass.java" );
    javaFile4 = getResourceAsFile( "/com/cedarsoft/codegen/model/test/Window.java" );
  }

  @Test
  public void testSingle() {
    Result parsed = Parser.parse( null, javaFile0 );

    assertNotNull( parsed );
    assertEquals( 1, parsed.getClassDeclarations().size() );
    ClassDeclaration classDeclaration = parsed.getClassDeclaration( "com.cedarsoft.codegen.model.test.FieldTypesInit" );

    assertNotNull( classDeclaration );
    assertThat( classDeclaration.getSimpleName(), is( "FieldTypesInit" ) );
    assertThat( classDeclaration.getQualifiedName(), is( "com.cedarsoft.codegen.model.test.FieldTypesInit" ) );

    assertSame( classDeclaration, parsed.getClassDeclaration() );
  }

  @Test
  public void testSubClass() throws Exception {
    Result parsed = Parser.parse( null, javaFile3, javaFile4 );
    assertEquals( 2, parsed.getClassDeclarations().size() );

    assertNotNull( parsed.getClassDeclaration( "com.cedarsoft.codegen.model.test.Window" ) );
  }

  @Test
  public void testMulti() {
    Result parsed = Parser.parse( null, javaFile0, javaFile1, javaFile2 );
    assertNotNull( parsed );

    assertThat( parsed.getClassDeclarations().size(), is( 3 ) );

    expectedException.expect( IllegalStateException.class );
    parsed.getClassDeclaration();
  }

  @Test
  public void testFindShortes() {
    Result parsed = Parser.parse( null, javaFile0, javaFile1, javaFile2 );
    assertNotNull( parsed );

    assertEquals( "com.cedarsoft.codegen.model.test.Door", parsed.findClassDeclarationWithShortestFQName().getQualifiedName() );
  }

  @Test
  public void testInvalid() {
    Result parsed = Parser.parse( null, javaFile0 );
    expectedException.expect( IllegalArgumentException.class );
    parsed.getClassDeclaration( "does.not.Exist" );
  }

  @Test
  public void testEmpty() {
    Result result = new Result( new AnnotationProcessorEnvironmentMock() );
    expectedException.expect( IllegalStateException.class );
    result.getClassDeclaration();
  }

  @Nonnull
  private File getResourceAsFile( @Nonnull String path ) throws URISyntaxException {
    URL resource = getClass().getResource( path );
    assertNotNull( resource );
    File file = new File( resource.toURI() );
    assertTrue( file.exists() );
    return file;
  }
}
