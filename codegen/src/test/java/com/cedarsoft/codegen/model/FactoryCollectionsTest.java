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
import com.google.common.collect.ImmutableList;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.ConstructorDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import com.sun.mirror.declaration.MethodDeclaration;
import com.sun.mirror.declaration.ParameterDeclaration;
import org.junit.*;

import java.io.File;
import java.net.URL;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertNotNull;
import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;

/**
 *
 */
public class FactoryCollectionsTest {
  private DomainObjectDescriptorFactory factory;
  private ClassDeclaration classDeclaration;
  private Result parsed;

  @BeforeMethod
  protected void setUp() throws Exception {
    URL resource = getClass().getResource( "/com/cedarsoft/codegen/model/test/Room.java" );
    assertNotNull( resource );
    File javaFile = new File( resource.toURI() );
    assertTrue( javaFile.exists() );

    parsed = Parser.parse( javaFile );
    assertNotNull( parsed );
    assertEquals( parsed.getClassDeclarations().size(), 1 );

    classDeclaration = parsed.getClassDeclaration( "com.cedarsoft.codegen.model.test.Room" );
    TypeUtils.setTypes( parsed.getEnvironment().getTypeUtils() );
    factory = new DomainObjectDescriptorFactory( classDeclaration );
  }

  @Test
  public void testAssignCall() {
    DomainObjectDescriptor descriptor = new DomainObjectDescriptor( classDeclaration );
    FieldDeclaration fieldDeclaration = descriptor.findFieldDeclaration( "windows" );
    assertNotNull( fieldDeclaration );
    assertEquals( fieldDeclaration.getType().toString(), "java.util.List<com.cedarsoft.codegen.model.test.Window>" );

    ConstructorDeclaration constructorDeclaration = descriptor.findBestConstructor();
    ImmutableList<ParameterDeclaration> constructorParameters = ImmutableList.copyOf( constructorDeclaration.getParameters() );
    assertEquals( constructorParameters.size(), 3 );
    ParameterDeclaration param = constructorParameters.get( 1 );
    assertEquals( param.getSimpleName(), "windows" );
    assertEquals( param.getType().toString(), "java.util.Collection<? extends com.cedarsoft.codegen.model.test.Window>" );

    assertEquals( fieldDeclaration.getType().toString(), "java.util.List<com.cedarsoft.codegen.model.test.Window>" );
    assertEquals( param.getType().toString(), "java.util.Collection<? extends com.cedarsoft.codegen.model.test.Window>" );

    assertTrue( TypeUtils.isAssignable( fieldDeclaration.getType(), param.getType() ) );
    assertFalse( TypeUtils.isAssignable( param.getType(), fieldDeclaration.getType() ) );
  }

  @Test
  public void testFindConstructorArgs() {
    DomainObjectDescriptor descriptor = new DomainObjectDescriptor( classDeclaration );
    FieldDeclaration fieldDeclaration = descriptor.findFieldDeclaration( "windows" );

    ConstructorCallInfo infoForField = factory.findConstructorCallInfoForField( fieldDeclaration );
    assertNotNull( infoForField );
    assertEquals( infoForField.getIndex(), 1 );
    assertEquals( infoForField.getParameterDeclaration().getSimpleName(), "windows" );
    assertEquals( infoForField.getParameterDeclaration().getType().toString(), "java.util.Collection<? extends com.cedarsoft.codegen.model.test.Window>" );
  }

  @Test
  public void testFindDoorsSetter() {
    DomainObjectDescriptor descriptor = new DomainObjectDescriptor( classDeclaration );
    FieldDeclaration fieldDeclaration = descriptor.findFieldDeclaration( "doors" );
    assertEquals( fieldDeclaration.getType().toString(), "java.util.List<com.cedarsoft.codegen.model.test.Door>" );

    MethodDeclaration setter = DomainObjectDescriptor.findSetter( classDeclaration, fieldDeclaration );
    assertEquals( setter.getSimpleName(), "setDoors" );
    assertEquals( setter.getReturnType().toString(), "void" );
    assertEquals( setter.getParameters().size(), 1 );
    assertEquals( setter.getParameters().iterator().next().getType().toString(), "java.util.List<? extends com.cedarsoft.codegen.model.test.Door>" );
  }

  @Test
  public void testIt() {
    DomainObjectDescriptor descriptor = factory.create();
    assertNotNull( descriptor );
    assertEquals( descriptor.getFieldsToSerialize().size(), 3 );
  }

  @Test
  public void testIsCollType() {
    DomainObjectDescriptor descriptor = new DomainObjectDescriptor( classDeclaration );
    assertFalse( factory.getFieldInitializeInConstructorInfo( descriptor.findFieldDeclaration( "description" ) ).isCollectionType() );
    assertTrue( factory.getFieldInitializeInConstructorInfo( descriptor.findFieldDeclaration( "doors" ) ).isCollectionType() );
    assertTrue( factory.getFieldInitializeInConstructorInfo( descriptor.findFieldDeclaration( "windows" ) ).isCollectionType() );
  }

  @Test
  public void testIsCollType2() {
    DomainObjectDescriptor descriptor = new DomainObjectDescriptor( classDeclaration );

    try {
      factory.getFieldInitializeInConstructorInfo( descriptor.findFieldDeclaration( "description" ) ).getCollectionParam();
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
    }

    assertEquals( factory.getFieldInitializeInConstructorInfo( descriptor.findFieldDeclaration( "doors" ) ).getCollectionParam().toString(), "com.cedarsoft.codegen.model.test.Door" );
    assertEquals( factory.getFieldInitializeInConstructorInfo( descriptor.findFieldDeclaration( "windows" ) ).getCollectionParam().toString(), "com.cedarsoft.codegen.model.test.Window" );
  }
}
