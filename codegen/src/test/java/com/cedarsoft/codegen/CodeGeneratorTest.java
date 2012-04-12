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
import com.cedarsoft.codegen.mock.TypesMock;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.writer.SingleStreamCodeWriter;
import javax.annotation.Nonnull;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class CodeGeneratorTest {
  private CodeGenerator codeGenerator;

  @Before
  public void setUp() throws Exception {
    TypeUtils.setTypes( new TypesMock() );

    codeGenerator = new CodeGenerator( new DecisionCallback() {
    } );
  }

  @Test
  public void testInnerClasses() {
    JClass outer = codeGenerator.getModel().ref( "da.OuterClass" );
    JClass inner = codeGenerator.getModel().ref( "da.OuterClass$Inner" );

    assertEquals( "da.OuterClass", outer.fullName() );
    assertEquals( "da", outer._package().name() );

    assertEquals( "da.OuterClass$Inner", inner.fullName() );
    assertEquals( "OuterClass$Inner", inner.name() );
    assertEquals( "da", inner._package().name() );

    try {
      codeGenerator.ref( "da.Outer.Inner" );
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
    }
  }

  @Test
  public void testMethodDecorator() throws Exception {
    codeGenerator.addDecorator( new Decorator() {
      @Override
      public void decorateConstant( @Nonnull CodeGenerator codeGenerator, @Nonnull JFieldVar constant ) {
        throw new UnsupportedOperationException();
      }
    } );
  }

  @Test
  public void testBasic() {
    assertNotNull( codeGenerator.getNewInstanceFactory() );
    assertNotNull( codeGenerator.getClassRefSupport() );
    assertNotNull( codeGenerator.getDecisionCallback() );
    assertNotNull( codeGenerator.getDecorators() );
    assertNotNull( codeGenerator.getModel() );
    assertNotNull( codeGenerator.getParseExpressionFactory() );
  }

  @Test
  public void testConstant() throws Exception {
    JCodeModel model = codeGenerator.getModel();

    codeGenerator.addDecorator( new Decorator() {
      @Override
      public void decorateConstant( @Nonnull CodeGenerator codeGenerator, @Nonnull JFieldVar constant ) {
        constant.annotate( codeGenerator.ref( Nonnull.class ) );
      }
    } );

    JDefinedClass aClass = model._class( "da.Class" );

    codeGenerator.getOrCreateConstant( aClass, String.class, "DA_CONSTANT", JExpr.lit( "value" ) );
    codeGenerator.getOrCreateConstant( aClass, String.class, "DA_CONSTANT", JExpr.lit( "value" ) );
    codeGenerator.getOrCreateConstant( aClass, String.class, "DA_CONSTANT", JExpr.lit( "value" ) );
    codeGenerator.getOrCreateConstant( aClass, String.class, "DA_CONSTANT", JExpr.lit( "value" ) );


    ByteArrayOutputStream out = new ByteArrayOutputStream();
    model.build( new SingleStreamCodeWriter( out ) );

    assertEquals( "-----------------------------------da.Class.java-----------------------------------\n" +
                    "\n" +
                    "package da;\n" +
                    "\n" +
                    "import javax.annotation.Nonnull;\n" +
                    "\n" +
                    "public class Class {\n" +
                    "\n" +
                    "    @Nonnull\n" +
                    "    public final static String DA_CONSTANT = \"value\";\n" +
                    "\n" +
                    "}", out.toString().trim() );
  }

  @Test
  public void testRef() {
    assertSame( codeGenerator.ref( "com.cedarsoft.Foo" ), codeGenerator.ref( "com.cedarsoft.Foo" ) );
    assertSame( codeGenerator.ref( String.class ), codeGenerator.ref( String.class ) );
    assertSame( codeGenerator.ref( FooBar.class ), codeGenerator.ref( FooBar.class ) );
    assertSame( codeGenerator.ref( FooBar.class ), codeGenerator.ref( "com.cedarsoft.codegen.CodeGeneratorTest$FooBar" ) );

    assertSame( codeGenerator.ref( List.class ), codeGenerator.ref( "java.util.List" ) );

    JClass aClass = codeGenerator.ref( new CollectionTypeMirrorMock( List.class, String.class ) );
    assertEquals( "java.util.List<java.lang.String>", aClass.fullName() );
    assertEquals( 1, aClass.getTypeParameters().size() );
    assertEquals( "java.lang.String", aClass.getTypeParameters().get( 0 ).fullName() );

    assertSame( codeGenerator.ref( new CollectionTypeMirrorMock( List.class, String.class ) ).erasure(), codeGenerator.ref( List.class ) );
  }

  private static class FooBar {

  }

}
