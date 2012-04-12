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

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JCodeModel;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JExpression;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JInvocation;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JPackage;
import com.sun.codemodel.JVar;
import com.sun.codemodel.fmt.JTextFile;
import com.sun.codemodel.writer.SingleStreamCodeWriter;
import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.EventListener;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class CodeModelTest {
  private ByteArrayOutputStream out;
  private CodeWriter codeWriter;
  private JCodeModel model;

  @Before
  public void setUp() throws Exception {
    out = new ByteArrayOutputStream();
    codeWriter = new SingleStreamCodeWriter( out );
    model = new JCodeModel();
  }

  @Test
  public void testWriter() throws JClassAlreadyExistsException, IOException {
    model._class( "a.b.c.Foo" );
    model._class( "a.b.c.Bar" );
    model._class( "a.b.c.d.Foo" );
    model._class( "a.b.c.d.Bar" );
    {
      JTextFile rsrc = new JTextFile( "daTextFile1.txt" );
      rsrc.setContents( "" );
      model._package( "a.b.c.d" ).addResourceFile( rsrc );
    }
    {
      JTextFile rsrc = new JTextFile( "daTextFile2" );
      rsrc.setContents( "dacontent2" );
      model._package( "a.b.c.d" ).addResourceFile( rsrc );
    }


    MemoryCodeWriter out = new MemoryCodeWriter();
    model.build( out );

    assertEquals( 6, out.getFiles().size() );

    assertEquals( "\n" +
      "package a.b.c;\n" +
      "\n" +
      "\n" +
      "public class Foo {\n" +
      "\n" +
      "\n" +
      "}\n", out.getFileContent( "a.b.c", "Foo.java" ) );
    assertEquals( "", out.getFileContent( "a.b.c.d", "daTextFile1.txt" ) );
    assertEquals( "dacontent2", out.getFileContent( "a.b.c.d", "daTextFile2" ) );

    assertEquals( "-----------------------------------a.b.c.Bar.java-----------------------------------\n" +
      "\n" +
      "package a.b.c;\n" +
      "\n" +
      "\n" +
      "public class Bar {\n" +
      "\n" +
      "\n" +
      "}\n" +
      "-----------------------------------a.b.c.Foo.java-----------------------------------\n" +
      "\n" +
      "package a.b.c;\n" +
      "\n" +
      "\n" +
      "public class Foo {\n" +
      "\n" +
      "\n" +
      "}\n" +
      "-----------------------------------a.b.c.d.Bar.java-----------------------------------\n" +
      "\n" +
      "package a.b.c.d;\n" +
      "\n" +
      "\n" +
      "public class Bar {\n" +
      "\n" +
      "\n" +
      "}\n" +
      "-----------------------------------a.b.c.d.Foo.java-----------------------------------\n" +
      "\n" +
      "package a.b.c.d;\n" +
      "\n" +
      "\n" +
      "public class Foo {\n" +
      "\n" +
      "\n" +
      "}\n" +
      "-----------------------------------a.b.c.d.daTextFile1.txt-----------------------------------\n" +
      "-----------------------------------a.b.c.d.daTextFile2-----------------------------------\n" +
      "dacontent2", out.allFilesToString() );
  }

  @Test
  public void testCreateResource() throws JClassAlreadyExistsException, IOException {
    model._class( "a.b.c.Foo" );

    JPackage daPackage = model._package( "a.b.c" );
    assertFalse( daPackage.hasResourceFile( "test.xml" ) );

    JTextFile testXml = new JTextFile( "test.xml" );
    daPackage.addResourceFile( testXml );
    assertTrue( daPackage.hasResourceFile( "test.xml" ) );

    testXml.setContents( "<xml>DaXmlContent</xml>" );

    model.build( codeWriter );
    assertEquals( "-----------------------------------a.b.c.Foo.java-----------------------------------\n" +
      "\n" +
      "package a.b.c;\n" +
      "\n" +
      "\n" +
      "public class Foo {\n" +
      "\n" +
      "\n" +
      "}\n" +
      "-----------------------------------a.b.c.test.xml-----------------------------------\n" +
      "<xml>DaXmlContent</xml>", out.toString().trim() );
  }

  @Test
  public void testSimple() throws Exception {
    JCodeModel codeModel = new JCodeModel();
    JDefinedClass foo = codeModel._class( "a.b.c.Foo" ); //Creates a new class

    JMethod method = foo.method( JMod.PUBLIC, Void.TYPE, "doFoo" ); //Adds a method to the class
    method.body()._return( JExpr.lit( 42 ) ); //the return type

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    codeModel.build( new SingleStreamCodeWriter( out ) );

    assertEquals( "-----------------------------------a.b.c.Foo.java-----------------------------------\n" +
      "\n" +
      "package a.b.c;\n" +
      "\n" +
      "\n" +
      "public class Foo {\n" +
      "\n" +
      "\n" +
      "    public void doFoo() {\n" +
      "        return  42;\n" +
      "    }\n" +
      "\n" +
      "}\n", out.toString() );
  }

  @Test
  public void testClass() throws Exception {
    JDefinedClass daClass = model._class( "a.b.c.Foo" );

    JMethod method = daClass.method( JMod.PUBLIC, Void.TYPE, "daMethod" );
    JExpression dotClass = JExpr.dotclass( model.ref( String.class ) );
    method.body()._return( dotClass );

    model.build( codeWriter );
    assertEquals( out.toString().trim(), "-----------------------------------a.b.c.Foo.java-----------------------------------\n" +
      "\n" +
      "package a.b.c;\n" +
      "\n" +
      "\n" +
      "public class Foo {\n" +
      "\n" +
      "\n" +
      "    public void daMethod() {\n" +
      "        return String.class;\n" +
      "    }\n" +
      "\n" +
      "}".trim() );
  }

  @Test
  public void testFqNames() throws JClassAlreadyExistsException, IOException {
    JDefinedClass daClass = model._class( "a.b.c.Foo" );
    daClass._extends( model.ref( "a.b.c.Bar" ) );

    model.build( codeWriter );
    assertEquals( out.toString().trim(), "-----------------------------------a.b.c.Foo.java-----------------------------------\n" +
      "\n" +
      "package a.b.c;\n" +
      "\n" +
      "\n" +
      "public class Foo\n" +
      "    extends Bar\n" +
      "{\n" +
      "\n" +
      "\n" +
      "}".trim() );
  }

  @Test
  public void testIt() throws IOException, JClassAlreadyExistsException, InterruptedException {
    {
      JDefinedClass fooClass = model._class( "com.cedarsoft.generator.test.Foo" );
      fooClass._implements( EventListener.class );
      fooClass.field( JMod.PRIVATE, String.class, "id" );
    }
    {
      JDefinedClass barClass = model._class( "com.cedarsoft.generator.test.bar.Bar" );
      barClass._implements( EventListener.class );
      barClass.field( Modifier.PRIVATE | Modifier.FINAL, Integer.TYPE, "id" );
      barClass.field( JMod.PRIVATE | JMod.FINAL, Integer.TYPE, "id2" );
    }

    model.build( codeWriter );
    assertEquals( out.toString().trim(), "-----------------------------------com.cedarsoft.generator.test.bar.Bar.java-----------------------------------\n" +
      "\n" +
      "package com.cedarsoft.generator.test.bar;\n" +
      "\n" +
      "import java.util.EventListener;\n" +
      "\n" +
      "public class Bar\n" +
      "    implements EventListener\n" +
      "{\n" +
      "\n" +
      "    protected static int id;\n" +
      "    private final int id2;\n" +
      "\n" +
      "}\n" +
      "-----------------------------------com.cedarsoft.generator.test.Foo.java-----------------------------------\n" +
      "\n" +
      "package com.cedarsoft.generator.test;\n" +
      "\n" +
      "import java.util.EventListener;\n" +
      "\n" +
      "public class Foo\n" +
      "    implements EventListener\n" +
      "{\n" +
      "\n" +
      "    private String id;\n" +
      "\n" +
      "}" );
  }

  @Test
  public void testGenerics() throws Exception {
    JDefinedClass aClass = model._class( "org.test.MyClass" );

    //    JExpression assignment = codeModel.ref( ArrayList.class ).dotclass();
    //    JExpression assignment = codeModel.ref( ArrayList.class ).;
    JInvocation assignment = JExpr._new( model.ref( ArrayList.class ).narrow( String.class ) );

    JFieldVar field = aClass.field( JMod.PRIVATE | JMod.FINAL, model.ref( List.class ).narrow( model.ref( String.class ) ), "ids", assignment );
    aClass.field( JMod.PRIVATE | JMod.FINAL, model.ref( List.class ).narrow( model.ref( String.class ).wildcard() ), "ids2", assignment );


    model.build( codeWriter );
    assertEquals( out.toString().trim(), "-----------------------------------org.test.MyClass.java-----------------------------------\n" +
      "\n" +
      "package org.test;\n" +
      "\n" +
      "import java.util.ArrayList;\n" +
      "import java.util.List;\n" +
      "\n" +
      "public class MyClass {\n" +
      "\n" +
      "    private final List<String> ids = new ArrayList<String>();\n" +
      "    private final List<? extends String> ids2 = new ArrayList<String>();\n" +
      "\n" +
      "}" );
  }

  @Test
  public void testMethod() throws Exception {
    JDefinedClass aClass = model._class( "org.test.DaTestClass" );

    aClass.method( JMod.PUBLIC, String.class, "getString" );
    aClass.method( JMod.PUBLIC, Void.TYPE, "doIt" );

    model.build( codeWriter );
    assertEquals( out.toString().trim(), "-----------------------------------org.test.DaTestClass.java-----------------------------------\n" +
      "\n" +
      "package org.test;\n" +
      "\n" +
      "\n" +
      "public class DaTestClass {\n" +
      "\n" +
      "\n" +
      "    public String getString() {\n" +
      "    }\n" +
      "\n" +
      "    public void doIt() {\n" +
      "    }\n" +
      "\n" +
      "}" );
  }

  @Test
  public void testMethodBody() throws Exception {
    JDefinedClass aClass = model._class( "org.test.DaTestClass" );
    JMethod method = aClass.method( JMod.PUBLIC, String.class, "getString" );
    JVar param = method.param( String.class, "daString" );

    method.body().add( param.invoke( "substring" ).arg( JExpr.lit( 0 ) ).arg( JExpr.lit( 7 ) ) );
    method.body()._return( param.invoke( "length" ) );

    model.build( codeWriter );
    assertEquals( out.toString().trim(), "-----------------------------------org.test.DaTestClass.java-----------------------------------\n" +
      "\n" +
      "package org.test;\n" +
      "\n" +
      "\n" +
      "public class DaTestClass {\n" +
      "\n" +
      "\n" +
      "    public String getString(String daString) {\n" +
      "        daString.substring(0, 7);\n" +
      "        return daString.length();\n" +
      "    }\n" +
      "\n" +
      "}" );
  }

  @Test
  public void testMethodBody2() throws Exception {
    JDefinedClass aClass = model._class( "org.test.DaTestClass" );
    JMethod method = aClass.method( JMod.PUBLIC, String.class, "getString" );

    JVar assignmentVar = method.body().decl( model.ref( String.class ), "daAssignmentTarget", JExpr.invoke( "init" ) );
    method.body()._return( assignmentVar );

    model.build( codeWriter );
    assertEquals( out.toString().trim(), "-----------------------------------org.test.DaTestClass.java-----------------------------------\n" +
      "\n" +
      "package org.test;\n" +
      "\n" +
      "\n" +
      "public class DaTestClass {\n" +
      "\n" +
      "\n" +
      "    public String getString() {\n" +
      "        String daAssignmentTarget = init();\n" +
      "        return daAssignmentTarget;\n" +
      "    }\n" +
      "\n" +
      "}" );
  }

  @Test
  public void testcomments() throws Exception {
    JDefinedClass aClass = model._class( "org.test.DaTestClass" );
    JMethod method = aClass.method( JMod.PUBLIC, String.class, "getString" );

    method.body().directStatement( "//a comment!!" );
    method.body().directStatement( "//a comment2!!" );

    model.build( codeWriter );
    assertEquals( out.toString().trim(), "-----------------------------------org.test.DaTestClass.java-----------------------------------\n" +
      "\n" +
      "package org.test;\n" +
      "\n" +
      "\n" +
      "public class DaTestClass {\n" +
      "\n" +
      "\n" +
      "    public String getString() {\n" +
      "        //a comment!!\n" +
      "        //a comment2!!\n" +
      "    }\n" +
      "\n" +
      "}" );
  }

  @Test
  public void testInner() throws Exception {
    JDefinedClass aClass = model._class( "org.test.DaTestClass" );
    JClass daInner = aClass._class( "Inner" );

    assertEquals( "org.test.DaTestClass.Inner", daInner.fullName() );
    assertEquals( "Inner", daInner.name() );

    aClass.method( JMod.PUBLIC, daInner, "getInner" );
    model._class( "org.test.OtherClass" ).method( JMod.PUBLIC, daInner, "getInner" );

    model.build( codeWriter );
    assertEquals( "-----------------------------------org.test.DaTestClass.java-----------------------------------\n" +
      "\n" +
      "package org.test;\n" +
      "\n" +
      "\n" +
      "public class DaTestClass {\n" +
      "\n" +
      "\n" +
      "    public DaTestClass.Inner getInner() {\n" +
      "    }\n" +
      "\n" +
      "    public class Inner {\n" +
      "\n" +
      "\n" +
      "    }\n" +
      "\n" +
      "}\n" +
      "-----------------------------------org.test.OtherClass.java-----------------------------------\n" +
      "\n" +
      "package org.test;\n" +
      "\n" +
      "\n" +
      "public class OtherClass {\n" +
      "\n" +
      "\n" +
      "    public org.test.DaTestClass.Inner getInner() {\n" +
      "    }\n" +
      "\n" +
      "}", out.toString().trim() );

  }

  @Test
  public void testInnerOwn() throws Exception {
    JDefinedClass aClass = model._class( "org.test.DaTestClass" );

    JClass daInner = new JDirectInnerClass( model, aClass, "Inner" );

    assertEquals( "org.test.DaTestClass.Inner", daInner.fullName() );
    assertEquals( "Inner", daInner.name() );

    aClass.method( JMod.PUBLIC, daInner, "getInner" );
    model._class( "org.test.OtherClass" ).method( JMod.PUBLIC, daInner, "getInner" );

    model.build( codeWriter );
    assertEquals( "-----------------------------------org.test.DaTestClass.java-----------------------------------\n" +
      "\n" +
      "package org.test;\n" +
      "\n" +
      "\n" +
      "public class DaTestClass {\n" +
      "\n" +
      "\n" +
      "    public DaTestClass.Inner getInner() {\n" +
      "    }\n" +
      "\n" +
      "}\n" +
      "-----------------------------------org.test.OtherClass.java-----------------------------------\n" +
      "\n" +
      "package org.test;\n" +
      "\n" +
      "\n" +
      "public class OtherClass {\n" +
      "\n" +
      "\n" +
      "    public org.test.DaTestClass.Inner getInner() {\n" +
      "    }\n" +
      "\n" +
      "}", out.toString().trim() );

  }
}
