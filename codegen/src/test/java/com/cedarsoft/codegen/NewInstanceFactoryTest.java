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

import com.cedarsoft.codegen.mock.CollectionTypeMirrorMock;
import com.cedarsoft.codegen.mock.ReferenceTypeMock;
import com.cedarsoft.codegen.mock.TypesMock;
import com.cedarsoft.codegen.model.DomainObjectDescriptor;
import com.cedarsoft.codegen.model.DomainObjectDescriptorFactory;
import com.cedarsoft.codegen.parser.Parser;
import com.cedarsoft.codegen.parser.Result;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFormatter;
import com.sun.codemodel.JType;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.type.TypeMirror;

import javax.annotation.Nonnull;
import org.junit.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 *
 */
public class NewInstanceFactoryTest {
  private NewInstanceFactory factory;
  private JCodeModel codeModel;
  private JFormatter formatter;
  private StringWriter out;

  @Before
  public void setUp() throws Exception {
    TypeUtils.setTypes( new TypesMock() );
    codeModel = new JCodeModel();
    factory = new NewInstanceFactory( codeModel, new ClassRefSupport( codeModel ) );
    initializeFormatter();
  }

  private void initializeFormatter() {
    out = new StringWriter();
    formatter = new JFormatter( new PrintWriter( out ) );
  }

  @Test
  public void testString() throws IOException {
    assertFactory( String.class, "\"daValue\"" );
  }

  @Test
  public void testPrim() throws IOException {
    assertFactory( Integer.TYPE, "42" );
    assertFactory( Double.TYPE, "12.5D" );
    assertFactory( Float.TYPE, "44.0F" );
    assertFactory( Long.TYPE, "43L" );
    assertFactory( Boolean.TYPE, "true" );
    assertFactory( Character.TYPE, "'c'" );
  }

  @Test
  public void testNum() throws IOException {
    assertFactory( Integer.class, "java.lang.Integer.valueOf(42)" );
    assertFactory( Double.class, "java.lang.Double.valueOf(12.5D)" );
    assertFactory( Float.class, "java.lang.Float.valueOf(44.0F)" );
    assertFactory( Long.class, "java.lang.Long.valueOf(43L)" );
    assertFactory( Boolean.class, "java.lang.Boolean.TRUE" );
  }

  @Test
  public void testObject() throws IOException {
    assertFactory( Object.class, "new java.lang.Object()" );
  }

  @Test
  public void testInner() throws IOException {
    JDirectInnerClass innerClass = new JDirectInnerClass( codeModel, codeModel.ref( "da.pack.Outer" ), "Inner" );
    assertFactory( innerClass, "new da.pack.Outer.Inner()" );
  }

  @Test
  public void testList() throws IOException {
    TypeUtils.setTypes( new TypesMock() );
    assertFactory( new CollectionTypeMirrorMock( List.class, String.class ), "java.util.Arrays.asList(\"daValue\")" );
    assertFactory( new CollectionTypeMirrorMock( Set.class, String.class ), "new java.util.HashSet(java.util.Arrays.asList(\"daValue\"))" );

    assertFactory( codeModel.ref( List.class ).narrow( String.class ), "java.util.Arrays.asList(\"daValue\")" );
    assertFactory( codeModel.ref( Set.class ).narrow( String.class ), "new java.util.HashSet(java.util.Arrays.asList(\"daValue\"))" );
  }

  @Test
  public void testList2() throws Exception {
    assertExpression( "java.util.Arrays.asList(\"daValue\")", factory.createCollectionInvocation( codeModel.ref( String.class ), "daValue", false ) );
    assertExpression( "new java.util.HashSet(java.util.Arrays.asList(\"daValue\"))", factory.createCollectionInvocation( codeModel.ref( String.class ), "daValue", true ) );

    assertExpression( "java.util.Arrays.asList(new com.cedarsoft.Foo())", factory.createCollectionInvocation( codeModel.ref( "com.cedarsoft.Foo" ), "daValue", false ) );
    assertExpression( "new java.util.HashSet(java.util.Arrays.asList(new com.cedarsoft.Foo()))", factory.createCollectionInvocation( codeModel.ref( "com.cedarsoft.Foo" ), "daValue", true ) );
  }

  @Test
  public void testLis3() throws Exception {
    assertExpression( "java.util.Arrays.asList(\"daValue\")", factory.createCollectionInvocation( new ReferenceTypeMock( String.class ), "daValue", false ) );
    assertExpression( "new java.util.HashSet(java.util.Arrays.asList(\"daValue\"))", factory.createCollectionInvocation( new ReferenceTypeMock( String.class ), "daValue", true ) );

    assertExpression( "java.util.Arrays.asList(new com.cedarsoft.codegen.NewInstanceFactoryTest.Foo())", factory.createCollectionInvocation( new ReferenceTypeMock( Foo.class ), "daValue", false ) );
    assertExpression( "new java.util.HashSet(java.util.Arrays.asList(new com.cedarsoft.codegen.NewInstanceFactoryTest.Foo()))", factory.createCollectionInvocation( new ReferenceTypeMock( Foo.class ), "daValue", true ) );
  }

  @Test
  public void testComplex() throws Exception {
    File file = new File( getClass().getResource( "test/AClass.java" ).toURI() );
    Result result = Parser.parse( null, file );

    assertEquals( 1, result.getClassDeclarations().size() );
    ClassDeclaration aClassDeclaration = result.getClassDeclaration( "com.cedarsoft.codegen.test.AClass" );
    assertNotNull( aClassDeclaration );

    DomainObjectDescriptor descriptor = new DomainObjectDescriptorFactory( aClassDeclaration ).create();

    assertFactory( descriptor.findFieldDeclaration( "single" ).getType(), "\"daValue\"" );
    assertFactory( descriptor.findFieldDeclaration( "theStrings" ).getType(), "java.util.Arrays.asList(\"daValue\")" );
    assertFactory( descriptor.findFieldDeclaration( "wildcardStrings" ).getType(), "java.util.Arrays.asList(\"daValue\")" );

    assertFactory( descriptor.findFieldDeclaration( "wildcardSet" ).getType(), "new java.util.HashSet(java.util.Arrays.asList(\"daValue\"))" );
    assertFactory( descriptor.findFieldDeclaration( "wildcardSet" ).getType(), "new java.util.HashSet(java.util.Arrays.asList(\"daValue\"))" );

    JExpression expression = factory.create( descriptor.findFieldDeclaration( "wildcardStrings" ).getType(), "daSimpleName" );
    assertExpression( "java.util.Arrays.asList(\"daSimpleName\")", expression );
  }

  @Test
  public void testOther() {
    assertFactory( codeModel.ref( List.class ).narrow( codeModel.ref( String.class ).wildcard() ), "java.util.Arrays.asList(\"daValue\")" );
    assertFactory( codeModel.ref( Set.class ).narrow( codeModel.ref( String.class ).wildcard() ), "new java.util.HashSet(java.util.Arrays.asList(\"daValue\"))" );
  }

  @Test
  public void testErasure() throws Exception {
    JClass aClass = codeModel.ref( Set.class ).narrow( codeModel.ref( String.class ).wildcard() );
    assertEquals( "java.util.Set<? extends java.lang.String>", aClass.fullName() );
    assertEquals( "java.util.Set", aClass.erasure().fullName() );
    assertEquals( "java.util.Set", aClass.erasure().erasure().fullName() );

    JClass param = TypeUtils.getCollectionParam( aClass );
    assertEquals( "? extends java.lang.String", param.fullName() );
    assertEquals( "com.sun.codemodel.JTypeWildcard", param.getClass().getName() );
    assertEquals( "? extends java.lang.String", param.erasure().fullName() );
    assertEquals( "java.lang.String", TypeUtils.removeWildcard( param ) );
  }

  private void assertFactory( @Nonnull Class<?> type, @Nonnull String expected ) throws IOException {
    assertFactory( new ReferenceTypeMock( type ), expected );
    assertFactory( codeModel._ref( type ), expected );
  }

  private void assertFactory( @Nonnull TypeMirror referenceType, @Nonnull String expected ) {
    assertExpression( expected, factory.create( referenceType, "daValue" ) );
  }

  private void assertFactory( @Nonnull JType referenceType, @Nonnull String expected ) {
    assertExpression( expected, factory.create( referenceType, "daValue" ) );
  }

  private void assertExpression( @Nonnull String expected, @Nonnull JExpression expression ) {
    initializeFormatter();
    expression.generate( formatter );
    assertOutput( expected );
  }

  private void assertOutput( @Nonnull String expected ) {
    assertEquals( expected.trim(), out.toString().trim() );
  }

  public static class Foo {

  }
}
