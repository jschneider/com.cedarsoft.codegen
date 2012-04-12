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

package com.cedarsoft.convert;

import org.junit.*;
import org.junit.rules.*;

import java.awt.Point;
import java.awt.Rectangle;
import java.io.File;

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: 13.11.2006<br>
 * Time: 17:00:54<br>
 */
public class StringConverterTest {
  @Rule
  public
  ExpectedException expectedException = ExpectedException.none();

  private StringConverterManager converterManager;

  @Before
  public void setUp() throws Exception {
    converterManager = new StringConverterManager( true );
  }

  @Test
  public void testNoDefaults() {
    converterManager = new StringConverterManager( false );

    expectedException.expect( IllegalArgumentException.class );
    assertNull( converterManager.serialize( "asdf" ) );
  }

  @Test
  public void testRegister() {
    converterManager = new StringConverterManager( false );
    converterManager.addStringConverter( Boolean.class, new StringConverterManager.BooleanConverter() );

    assertEquals( "true", converterManager.serialize( true ) );
    assertTrue( converterManager.deserialize( Boolean.class, "true" ) );
  }

  @Test
  public void testFile() {
    assertEquals( "/tmp/a", converterManager.serialize( new File( "/tmp/a" ) ) );
    assertEquals( new File( "/tmp/a" ), converterManager.deserialize( File.class, "/tmp/a" ) );
  }

  @Test
  public void testClass() {
    assertEquals( "java.lang.String", converterManager.serialize( String.class ) );
    assertEquals( String.class, converterManager.deserialize( Class.class, "java.lang.String" ) );
  }

  @Test
  public void testString() {
    assertEquals( "asdf", converterManager.serialize( "asdf" ) );
    assertEquals( "asdf", converterManager.deserialize( String.class, "asdf" ) );
  }

  @Test
  public void testBounds() {
    StringConverter<Rectangle> converter = converterManager.findConverter( Rectangle.class );
    assertNotNull( converter );

    Rectangle created = converter.createObject( converter.createRepresentation( new Rectangle( 10, 20, 30, 40 ) ) );
    assertEquals( 10, created.x );
    assertEquals( 20, created.y );
    assertEquals( 30, created.width );
    assertEquals( 40, created.height );
  }

  @Test
  public void testPoint() {
    StringConverter<Point> converter = converterManager.findConverter( Point.class );
    assertNotNull( converter );

    Point created = converter.createObject( converter.createRepresentation( new Point( 10, 20 ) ) );
    assertEquals( 10, created.x );
    assertEquals( 20, created.y );
  }
}
