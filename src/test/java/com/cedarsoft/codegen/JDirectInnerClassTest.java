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
public class JDirectInnerClassTest {
  private JCodeModel owner;

  @Test
  public void testName() throws Exception {
    JClass outerRef = new ClassRefSupport( owner ).ref( JDirectInnerClassTest.class );

    JDirectInnerClass inner = new JDirectInnerClass( owner, outerRef, "Foo" );

    assertNotNull( inner.outer() );
    assertEquals( "JDirectInnerClassTest", inner.outer().name() );
    assertEquals( "com.cedarsoft.codegen.JDirectInnerClassTest", inner.outer().fullName() );
    assertEquals( "Foo", inner.name() );

    assertEquals( "com.cedarsoft.codegen.JDirectInnerClassTest.Foo", inner.fullName() );
    assertEquals( "com.cedarsoft.codegen.JDirectInnerClassTest$Foo", inner.binaryName() );
  }

  @Test
  public void testNotExisting() throws Exception {
    JClass inner = new ClassRefSupport( owner ).ref( "com.cedarsoft.codegen.JDirectInnerClassTest$Bar" );

    assertEquals( JDirectInnerClass.class, inner.getClass() );

    assertNotNull( inner.outer() );
    assertEquals( "JDirectInnerClassTest", inner.outer().name() );
    assertEquals( "com.cedarsoft.codegen.JDirectInnerClassTest", inner.outer().fullName() );
    assertEquals( "Bar", inner.name() );

    assertEquals( "com.cedarsoft.codegen.JDirectInnerClassTest.Bar", inner.fullName() );
    assertEquals( "com.cedarsoft.codegen.JDirectInnerClassTest$Bar", inner.binaryName() );
  }

  @Test
  public void testManual() throws Exception {
    JDirectInnerClass inner = new JDirectInnerClass( owner, owner.ref( "com.cedarsoft.codegen.JDirectInnerClassTest" ), "Bar" );

    assertEquals( JDirectInnerClass.class, inner.getClass() );

    assertNotNull( inner.outer() );
    assertEquals( "JDirectInnerClassTest", inner.outer().name() );
    assertEquals( "com.cedarsoft.codegen.JDirectInnerClassTest", inner.outer().fullName() );
    assertEquals( "Bar", inner.name() );

    assertEquals( "com.cedarsoft.codegen.JDirectInnerClassTest.Bar", inner.fullName() );
    assertEquals( "com.cedarsoft.codegen.JDirectInnerClassTest$Bar", inner.binaryName() );
  }


  @Test
  public void testWildcard() throws Exception {
    JClass innerWithWild = new JDirectInnerClass( owner, owner.ref( "com.cedarsoft.codegen.JDirectInnerClassTest" ), "Bar" ).wildcard();
    assertEquals( "? extends Bar", innerWithWild.name() );
    assertEquals( "? extends com.cedarsoft.codegen.JDirectInnerClassTest.Bar", innerWithWild.fullName() );

    JClass list = owner.ref( List.class );

    assertEquals( "List<? extends Bar>", list.narrow( innerWithWild ).name() );
    assertEquals( "java.util.List<? extends com.cedarsoft.codegen.JDirectInnerClassTest.Bar>", list.narrow( innerWithWild ).fullName() );

    assertEquals( "com.cedarsoft.codegen.JDirectInnerClassTest.Bar", TypeUtils.removeWildcard( innerWithWild ) );
  }

  @Before
  public void setUp() throws Exception {
    owner = new JCodeModel();
  }

  public static class Foo {

  }
}
