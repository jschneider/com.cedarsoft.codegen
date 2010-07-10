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

package com.cedarsoft.codegen.model;

import com.cedarsoft.codegen.ConstructorCallInfo;
import com.cedarsoft.codegen.TypeUtils;
import com.cedarsoft.codegen.parser.Parser;
import com.cedarsoft.codegen.parser.Result;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.ConstructorDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.type.TypeMirror;
import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.*;

/**
 *
 */
public class DomainObjectDescriptorFactoryTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private DomainObjectDescriptorFactory factory;
  public static final String WINDOW_RES = "/com/cedarsoft/codegen/model/test/Window.java";

  @Before
  public void setUp() throws Exception {
    URL resource = getClass().getResource( WINDOW_RES );
    assertNotNull( resource );
    File javaFile = new File( resource.toURI() );
    assertTrue( javaFile.exists() );

    Result parsed = Parser.parse( javaFile, null );
    assertNotNull( parsed );
    assertEquals( 1, parsed.getClassDeclarations().size() );
    ClassDeclaration classDeclaration = parsed.getClassDeclaration( "com.cedarsoft.codegen.model.test.Window" );

    TypeUtils.setTypes( parsed.getEnvironment().getTypeUtils() );
    factory = new DomainObjectDescriptorFactory( classDeclaration );
  }

  @Test
  public void testFindField() {
    FieldDeclaration fieldDeclaration = DomainObjectDescriptor.findFieldDeclaration( factory.getClassDeclaration(), "width" );
    assertEquals( "width", fieldDeclaration.getSimpleName() );
    assertEquals( "double", fieldDeclaration.getType().toString() );
  }

  @Test
  public void testCreate() {
    DomainObjectDescriptor model = factory.create();
    assertEquals( "com.cedarsoft.codegen.model.test.Window", model.getQualifiedName() );
    assertEquals( 3, model.getFieldsToSerialize().size() );
    assertEquals( "width", model.getFieldsToSerialize().get( 0 ).getSimpleName() );
    assertEquals( "double", model.getFieldsToSerialize().get( 0 ).getType().toString() );

    assertEquals( "height", model.getFieldsToSerialize().get( 1 ).getSimpleName() );
    assertEquals( "double", model.getFieldsToSerialize().get( 1 ).getType().toString() );

    assertEquals( "description", model.getFieldsToSerialize().get( 2 ).getSimpleName() );
    assertEquals( "java.lang.String", model.getFieldsToSerialize().get( 2 ).getType().toString() );
  }

  @Test
  public void testGetter() {
    FieldDeclaration fieldDeclaration = DomainObjectDescriptor.findFieldDeclaration( factory.getClassDeclaration(), "width" );

    MethodDeclaration getterDeclaration = DomainObjectDescriptor.findGetterForField( factory.getClassDeclaration(), fieldDeclaration );
    assertNotNull( getterDeclaration );

    assertEquals( getterDeclaration.getReturnType(), fieldDeclaration.getType() );
    assertEquals( "getWidth", getterDeclaration.getSimpleName() );
  }

  @Test
  public void testFieldCons() {
    FieldInitializedInConstructorInfo fieldInfo = factory.findFieldInitializedInConstructor( "width" );
    assertNotNull( fieldInfo );
    assertEquals( "width", fieldInfo.getFieldDeclaration().getSimpleName() );
    assertEquals( 1, fieldInfo.getConstructorCallInfo().getIndex() );
    assertEquals( "width", fieldInfo.getConstructorCallInfo().getParameterDeclaration().getSimpleName() );
  }

  @Test
  public void testGetConstructor() {
    ConstructorDeclaration constructorDeclaration = DomainObjectDescriptor.findBestConstructor( factory.getClassDeclaration() );
    assertNotNull( constructorDeclaration );
  }

  @Test
  public void testFindConstrParam() {
    FieldDeclaration fieldDeclaration = DomainObjectDescriptor.findFieldDeclaration( factory.getClassDeclaration(), "width" );

    ConstructorCallInfo found = factory.findConstructorCallInfoForField( fieldDeclaration );
    assertEquals( 1, found.getIndex() );
    assertEquals( "width", found.getParameterDeclaration().getSimpleName() );
  }

  @Test
  public void testFindConstrParamWrongType() {
    FieldDeclaration fieldDeclaration = DomainObjectDescriptor.findFieldDeclaration( factory.getClassDeclaration(), "width" );
    TypeMirror type = DomainObjectDescriptor.findFieldDeclaration( factory.getClassDeclaration(), "description" ).getType();

    thrown.expect( IllegalArgumentException.class );
    thrown.expectMessage( "Type mismatch for <width>. Was <double> but expected <java.lang.String>" );

    factory.findConstructorCallInfoForField( fieldDeclaration.getSimpleName(), type );
  }
}
