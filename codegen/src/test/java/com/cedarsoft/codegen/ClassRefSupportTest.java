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

import com.sun.codemodel.JClass;
import com.sun.codemodel.JCodeModel;
import org.junit.*;

import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class ClassRefSupportTest {
  private ClassRefSupport classRefSupport;

  @Before
  public void setUp() throws Exception {
    classRefSupport = new ClassRefSupport( new JCodeModel() );
  }

  @Test
  public void testValid() throws Exception {
    classRefSupport.verifyValidRefName( "com.cedarsoft.DaClass" );
    classRefSupport.verifyValidRefName( "com.cedarsoft.DaClass$Inner" );

    try {
      classRefSupport.verifyValidRefName( "com.cedarsoft.DaClass.Inner" );
      fail( "Where is the Exception" );
    } catch ( IllegalArgumentException e ) {
      assertEquals( "Invalid inner class <com.cedarsoft.DaClass.Inner>. Use \"$\" sign instead.", e.getMessage() );
    }
  }

  @Test
  public void testRef() {
    assertSame( classRefSupport.ref( "com.cedarsoft.Foo" ), classRefSupport.ref( "com.cedarsoft.Foo" ) );
    assertSame( classRefSupport.ref( String.class ), classRefSupport.ref( String.class ) );
    assertSame( classRefSupport.ref( FooBar.class ), classRefSupport.ref( FooBar.class ) );
    assertSame( classRefSupport.ref( FooBar.class ), classRefSupport.ref( "com.cedarsoft.codegen.ClassRefSupportTest$FooBar" ) );
  }

  @Test
  public void testOwn() {
    JClass outer = classRefSupport.model.ref( "org.test.DaTestClass" );
    assertNotNull( outer );
  }

  /**
   * This test case shows that buggy support for class ref support!!!
   *
   * @throws Exception
   */
  @Test
  public void testInner() throws Exception {
    JClass inner = classRefSupport.ref( "org.test.DaTestClass$Inner" );

    //    assertNotNull( inner.outer() );
    //    assertEquals( "org.test.DaTestClass", inner.outer().name() );
    //    assertEquals( "Inner", inner.name() );
    //
    //    assertEquals( "org.test.DaTestClass.Inner", inner.fullName() );
  }

  @Test
  public void testInnerExisting() throws Exception {
    JClass inner = classRefSupport.model.ref( FooBar.class );

    assertNotNull( inner.outer() );
    assertEquals( "ClassRefSupportTest", inner.outer().name() );
    assertEquals( "com.cedarsoft.codegen.ClassRefSupportTest", inner.outer().fullName() );
    assertEquals( "FooBar", inner.name() );

    assertEquals( "com.cedarsoft.codegen.ClassRefSupportTest.FooBar", inner.fullName() );
  }

  @Test
  public void testInnerExisting2() throws Exception {
    assertEquals( "com.cedarsoft.codegen.ClassRefSupportTest$FooBar", FooBar.class.getName() );
    assertEquals( FooBar.class, Class.forName( FooBar.class.getName() ) );
    assertEquals( FooBar.class, Class.forName( "com.cedarsoft.codegen.ClassRefSupportTest$FooBar" ) );
    JClass inner = classRefSupport.model.ref( FooBar.class.getName() );

    assertNotNull( inner.outer() );
    assertEquals( "ClassRefSupportTest", inner.outer().name() );
    assertEquals( "com.cedarsoft.codegen.ClassRefSupportTest", inner.outer().fullName() );
    assertEquals( "FooBar", inner.name() );

    assertEquals( "com.cedarsoft.codegen.ClassRefSupportTest.FooBar", inner.fullName() );
  }

  @Test
  public void testInnerExisting3() throws Exception {
    JClass inner = classRefSupport.model.ref( "com.cedarsoft.codegen.ClassRefSupportTest$FooBar" );

    assertNotNull( inner.outer() );
    assertEquals( "ClassRefSupportTest", inner.outer().name() );
    assertEquals( "com.cedarsoft.codegen.ClassRefSupportTest", inner.outer().fullName() );
    assertEquals( "FooBar", inner.name() );

    assertEquals( "com.cedarsoft.codegen.ClassRefSupportTest.FooBar", inner.fullName() );
  }

  @Test
  public void testSimple() throws Exception {
    assertEquals( "java.lang.String", classRefSupport.ref( "java.lang.String" ).fullName() );
    assertEquals( "int", classRefSupport.ref( "int" ).fullName() );
  }

  @Test
  public void testColl() throws Exception {
    assertSame( classRefSupport.ref( List.class ), classRefSupport.ref( List.class ) );
  }

  private static class FooBar {
  }
}
