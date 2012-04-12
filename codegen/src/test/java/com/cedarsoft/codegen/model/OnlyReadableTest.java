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

import com.cedarsoft.codegen.TypeUtils;
import com.cedarsoft.codegen.parser.Parser;
import com.cedarsoft.codegen.parser.Result;
import com.sun.mirror.declaration.ClassDeclaration;
import com.sun.mirror.declaration.FieldDeclaration;
import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class OnlyReadableTest {
  public static final String RESOURCE = "/com/cedarsoft/codegen/model/test/CollectionsObject.java";

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  private DomainObjectDescriptorFactory factory;

  @Before
  public void setUp() throws Exception {
    URL resource = getClass().getResource( RESOURCE );
    assertNotNull( resource );
    File javaFile = new File( resource.toURI() );
    assertTrue( javaFile.exists() );

    Result parsed = Parser.parse( null, javaFile );
    assertNotNull( parsed );
    assertEquals( 1, parsed.getClassDeclarations().size() );
    ClassDeclaration classDeclaration = parsed.getClassDeclaration( "com.cedarsoft.codegen.model.test.CollectionsObject" );

    TypeUtils.setTypes( parsed.getEnvironment().getTypeUtils() );
    factory = new DomainObjectDescriptorFactory( classDeclaration );
  }

  @Test
  public void testFindField() {
    FieldDeclaration fieldDeclaration = TypeUtils.findFieldDeclaration( factory.getClassDeclaration(), "strings" );
    assertEquals( "strings", fieldDeclaration.getSimpleName() );
    assertEquals( "java.util.List<java.lang.String>", fieldDeclaration.getType().toString() );
  }

  @Test
  public void testBasic() {
    DomainObjectDescriptor descriptor = factory.create();

    assertEquals( "com.cedarsoft.codegen.model.test.CollectionsObject", descriptor.getQualifiedName() );
    FieldDeclaration fieldDeclaration = descriptor.findFieldDeclaration( "strings" );
    assertEquals( "strings", fieldDeclaration.getSimpleName() );
    assertEquals( "java.util.List<java.lang.String>", fieldDeclaration.getType().toString() );

    assertEquals( 0, descriptor.getFieldsInitializedInConstructor().size() );
    assertEquals( 0, descriptor.getFieldsInitializedInSetter().size() );
    assertEquals( 1, descriptor.getFieldsNotInitialized().size() );
  }
}
