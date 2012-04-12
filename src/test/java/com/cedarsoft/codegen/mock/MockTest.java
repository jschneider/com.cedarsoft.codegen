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

package com.cedarsoft.codegen.mock;

import com.cedarsoft.codegen.TypeUtils;
import org.junit.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.*;

/**
 *
 */
public class MockTest {
  @Before
  public void setup() {
    TypeUtils.setTypes( new TypesMock() );
  }

  @Test
  public void testMock() throws Exception {
    assertTrue( TypeUtils.isCollectionType( new CollectionTypeMirrorMock( List.class, String.class ) ) );
    assertEquals( TypeUtils.getCollectionParam( new CollectionTypeMirrorMock( List.class, String.class ) ).toString(), String.class.getName() );
  }

  @Test
  public void testCollType() throws Exception {
    assertTrue( TypeUtils.isCollectionType( new CollectionTypeMirrorMock( List.class, String.class ) ) );
    assertTrue( TypeUtils.isCollectionType( new CollectionTypeMirrorMock( Collection.class, String.class ) ) );
  }

  @Test
  public void testSet() {
    assertTrue( TypeUtils.isCollectionType( new CollectionTypeMirrorMock( Set.class, String.class ) ) );
    assertTrue( TypeUtils.isCollectionType( new CollectionTypeMirrorMock( HashSet.class, String.class ) ) );
  }

  @Test
  public void testArrayList() {
    assertTrue( TypeUtils.isCollectionType( new CollectionTypeMirrorMock( List.class, String.class ) ) );
    assertEquals( "java.lang.String", TypeUtils.getCollectionParam( new CollectionTypeMirrorMock( List.class, String.class ) ).toString() );

    assertTrue( TypeUtils.isCollectionType( new CollectionTypeMirrorMock( ArrayList.class, String.class ) ) );
    assertEquals( "java.lang.String", TypeUtils.getCollectionParam( new CollectionTypeMirrorMock( ArrayList.class, String.class ) ).toString() );
  }

  @Test
  public void testMap() {
    assertFalse( TypeUtils.isCollectionType( new CollectionTypeMirrorMock( Map.class, String.class ) ) );
    assertFalse( TypeUtils.isCollectionType( new CollectionTypeMirrorMock( HashMap.class, String.class ) ) );
  }
}
