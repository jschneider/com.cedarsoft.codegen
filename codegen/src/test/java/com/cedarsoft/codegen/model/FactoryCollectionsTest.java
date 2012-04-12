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

import static org.junit.Assert.*;

/**
 *
 */
public class FactoryCollectionsTest {
  private DomainObjectDescriptorFactory factory;
  private ClassDeclaration classDeclaration;
  private Result parsed;

  @Before
  public void setUp() throws Exception {
    URL resource = getClass().getResource( "/com/cedarsoft/codegen/model/test/Room.java" );
    assertNotNull( resource );
    File javaFile = new File( resource.toURI() );
    assertTrue( javaFile.exists() );

    parsed = Parser.parse( null, javaFile );
    assertNotNull( parsed );
    assertEquals( 1, parsed.getClassDeclarations().size() );

    classDeclaration = parsed.getClassDeclaration( "com.cedarsoft.codegen.model.test.Room" );
    TypeUtils.setTypes( parsed.getEnvironment().getTypeUtils() );
    factory = new DomainObjectDescriptorFactory( classDeclaration );
  }

  @Test
  public void testAssignCall() {
    DomainObjectDescriptor descriptor = new DomainObjectDescriptor( classDeclaration );
    FieldDeclaration fieldDeclaration = descriptor.findFieldDeclaration( "windows" );
    assertNotNull( fieldDeclaration );
    assertEquals( "java.util.List<com.cedarsoft.codegen.model.test.Window>", fieldDeclaration.getType().toString() );

    ConstructorDeclaration constructorDeclaration = descriptor.findBestConstructor();
    ImmutableList<ParameterDeclaration> constructorParameters = ImmutableList.copyOf( constructorDeclaration.getParameters() );
    assertEquals( 3, constructorParameters.size() );
    ParameterDeclaration param = constructorParameters.get( 1 );
    assertEquals( "windows", param.getSimpleName() );
    assertEquals( "java.util.Collection<? extends com.cedarsoft.codegen.model.test.Window>", param.getType().toString() );

    assertEquals( "java.util.List<com.cedarsoft.codegen.model.test.Window>", fieldDeclaration.getType().toString() );
    assertEquals( "java.util.Collection<? extends com.cedarsoft.codegen.model.test.Window>", param.getType().toString() );

    assertTrue( TypeUtils.isAssignable( fieldDeclaration.getType(), param.getType() ) );
    assertFalse( TypeUtils.isAssignable( param.getType(), fieldDeclaration.getType() ) );
  }

  @Test
  public void testFindConstructorArgs() {
    DomainObjectDescriptor descriptor = new DomainObjectDescriptor( classDeclaration );
    FieldDeclaration fieldDeclaration = descriptor.findFieldDeclaration( "windows" );

    ConstructorCallInfo infoForField = factory.findConstructorCallInfoForField( fieldDeclaration );
    assertNotNull( infoForField );
    assertEquals( 1, infoForField.getIndex() );
    assertEquals( "windows", infoForField.getParameterDeclaration().getSimpleName() );
    assertEquals( "java.util.Collection<? extends com.cedarsoft.codegen.model.test.Window>", infoForField.getParameterDeclaration().getType().toString() );
  }

  @Test
  public void testFindDoorsSetter() {
    DomainObjectDescriptor descriptor = new DomainObjectDescriptor( classDeclaration );
    FieldDeclaration fieldDeclaration = descriptor.findFieldDeclaration( "doors" );
    assertEquals( "java.util.List<com.cedarsoft.codegen.model.test.Door>", fieldDeclaration.getType().toString() );

    MethodDeclaration setter = TypeUtils.findSetter( classDeclaration, fieldDeclaration );
    assertEquals( "setDoors", setter.getSimpleName() );
    assertEquals( "void", setter.getReturnType().toString() );
    assertEquals( 1, setter.getParameters().size() );
    assertEquals( "java.util.List<? extends com.cedarsoft.codegen.model.test.Door>", setter.getParameters().iterator().next().getType().toString() );
  }

  @Test
  public void testIt() {
    DomainObjectDescriptor descriptor = factory.create();
    assertNotNull( descriptor );
    assertEquals( 3, descriptor.getFieldInfos().size() );
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

    assertEquals( "com.cedarsoft.codegen.model.test.Door", factory.getFieldInitializeInConstructorInfo( descriptor.findFieldDeclaration( "doors" ) ).getCollectionParam().toString() );
    assertEquals( "com.cedarsoft.codegen.model.test.Window", factory.getFieldInitializeInConstructorInfo( descriptor.findFieldDeclaration( "windows" ) ).getCollectionParam().toString() );
  }
}
