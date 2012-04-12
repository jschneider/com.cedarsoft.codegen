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

import com.cedarsoft.codegen.mock.ClassTypeMock;
import com.cedarsoft.codegen.mock.CollectionTypeMirrorMock;
import com.cedarsoft.codegen.mock.TypesMock;
import com.sun.codemodel.JCodeModel;
import org.junit.*;
import org.junit.rules.*;

import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

/**
 *
 */
public class TypeUtilsTest {
  private JCodeModel codeModel;

  @Before
  public void setup() {
    TypeUtils.setTypes( new TypesMock() );
    codeModel = new JCodeModel();
  }

  @Test
  public void testCollParam() {
    assertEquals( "java.lang.String", TypeUtils.getCollectionParam( new CollectionTypeMirrorMock( List.class, String.class ) ).toString() );
    assertEquals( "java.lang.String", TypeUtils.getCollectionParam( new CollectionTypeMirrorMock( Set.class, String.class ) ).toString() );
    assertEquals( "java.lang.Integer", TypeUtils.getCollectionParam( new CollectionTypeMirrorMock( List.class, Integer.class ) ).toString() );

    assertEquals( "java.lang.String", TypeUtils.getCollectionParam( codeModel.ref( List.class ).narrow( String.class ) ).fullName() );
    assertEquals( "java.lang.String", TypeUtils.getCollectionParam( codeModel.ref( Set.class ).narrow( String.class ) ).fullName() );
    assertEquals( "java.lang.Integer", TypeUtils.getCollectionParam( codeModel.ref( List.class ).narrow( Integer.class ) ).fullName() );
  }

  @Test
  public void testIsColl() {
    assertTrue( TypeUtils.isCollectionType( new CollectionTypeMirrorMock( List.class, String.class ) ) );
    assertTrue( TypeUtils.isCollectionType( new CollectionTypeMirrorMock( Set.class, String.class ) ) );
    assertTrue( TypeUtils.isCollectionType( new CollectionTypeMirrorMock( List.class, Integer.class ) ) );
    assertTrue( TypeUtils.isCollectionType( new CollectionTypeMirrorMock( List.class ) ) );

    assertTrue( TypeUtils.isCollectionType( codeModel.ref( List.class ).narrow( String.class ) ) );
    assertTrue( TypeUtils.isCollectionType( codeModel.ref( Set.class ).narrow( String.class ) ) );
    assertTrue( TypeUtils.isCollectionType( codeModel.ref( List.class ).narrow( Integer.class ) ) );

    assertTrue( TypeUtils.isCollectionType( codeModel.ref( List.class ) ) );
  }

  @Test
  public void testPrimitiveType() {
    assertTrue( TypeUtils.isSimpleType( new ClassTypeMock( Integer.TYPE ) ) );
    assertTrue( TypeUtils.isSimpleType( new ClassTypeMock( Integer.class ) ) );
    assertTrue( TypeUtils.isSimpleType( new ClassTypeMock( String.class ) ) );
    assertTrue( TypeUtils.isSimpleType( new ClassTypeMock( Character.class ) ) );

    assertFalse( TypeUtils.isSimpleType( new ClassTypeMock( Object.class ) ) );
    assertFalse( TypeUtils.isSimpleType( new ClassTypeMock( List.class ) ) );


    assertTrue( TypeUtils.isSimpleType( codeModel._ref( Integer.TYPE ) ) );
    assertTrue( TypeUtils.isSimpleType( codeModel.ref( Integer.class ) ) );
    assertTrue( TypeUtils.isSimpleType( codeModel.ref( String.class ) ) );
    assertTrue( TypeUtils.isSimpleType( codeModel.ref( Character.class ) ) );

    assertFalse( TypeUtils.isSimpleType( codeModel.ref( Object.class ) ) );
    assertFalse( TypeUtils.isSimpleType( codeModel.ref( List.class ) ) );
  }

  @Test
  public void testReasure() throws Exception {
    assertEquals( "java.lang.String", TypeUtils.removeWildcard( codeModel.ref( String.class ) ) );
    assertEquals( "java.lang.String", TypeUtils.removeWildcard( codeModel.ref( String.class ).wildcard() ) );

    expectedException.expect( IllegalArgumentException.class );
    expectedException.expectMessage( "Invalid type - cannot remove wildcard. Call erasure() first: java.util.List<? extends java.lang.String>" );

    assertEquals( "java.util.List", TypeUtils.removeWildcard( codeModel.ref( List.class ).narrow( codeModel.ref( String.class ).wildcard() ) ) );
  }

  @Rule
  public ExpectedException expectedException = ExpectedException.none();
}
