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

package com.cedarsoft.registry;

import com.cedarsoft.registry.hierarchy.AbstractChildDetector;
import com.cedarsoft.registry.hierarchy.ChildDetector;
import javax.annotation.Nonnull;
import org.junit.*;

import javax.swing.JComponent;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: Apr 27, 2007<br>
 * Time: 3:46:29 PM<br>
 */
public class TypeRegistryTest {
  private TypeRegistry<ChildDetector<?, ?>> registry;

  @Before
  public void setUp() throws Exception {
    registry = new TypeRegistry<ChildDetector<?, ?>>();
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testInterfaces() {
    registry.addElement( String.class, new AbstractChildDetector<String, Object>() {
      @Override
      @Nonnull
      public List<? extends Object> findChildren( @Nonnull String parent ) {
        return Collections.singletonList( "String" );
      }
    } );

    registry.addElement( Integer.class, new AbstractChildDetector<Integer, Object>() {
      @Override
      @Nonnull
      public List<? extends Object> findChildren( @Nonnull Integer parent ) {
        return Collections.singletonList( "Integer" );
      }
    } );

    assertEquals( "String", ( ( ChildDetector<String, Object> ) registry.getElement( String.class ) ).findChildren( "asdf" ).get( 0 ) );
    assertEquals( "Integer", ( ( ChildDetector<Integer, Object> ) registry.getElement( Integer.class ) ).findChildren( 5 ).get( 0 ) );
  }

  @Test
  public void testSuperTypes() {
    registry.addElement( Collection.class, new AbstractChildDetector<Object, Object>() {
      @Override
      @Nonnull
      public List<? extends Object> findChildren( @Nonnull Object parent ) {
        return Collections.singletonList( "Collection" );
      }
    } );

    assertEquals( "Collection", ( ( ( ChildDetector<String, ?> ) registry.getElement( Collection.class ) ) ).findChildren( "asdf" ).get( 0 ) );
    assertEquals( "Collection", ( ( ( ChildDetector<String, ?> ) registry.getElement( List.class ) ) ).findChildren( "asdf" ).get( 0 ) );
    assertEquals( "Collection", ( ( ( ChildDetector<String, ?> ) registry.getElement( ArrayList.class ) ) ).findChildren( "asdf" ).get( 0 ) );
  }

  @Test
  public void testFindDefault() {
    registry.addElement( Object.class, new AbstractChildDetector<Object, Object>() {
      @Override
      @Nonnull
      public List<? extends Object> findChildren( @Nonnull Object parent ) {
        throw new UnsupportedOperationException();
      }
    } );

    assertNotNull( registry.getElement( String.class ) );
  }

  @Test
  public void testRegistry() {
    registry.addElement( JComponent.class, new AbstractChildDetector<String, String>() {
      @Override
      @Nonnull
      public List<? extends String> findChildren( @Nonnull String parent ) {
        throw new UnsupportedOperationException();
      }
    } );

    assertNotNull( registry.getElement( JLabel.class ) );
    assertNotNull( registry.getElement( JComponent.class ) );

    assertSame( registry.getElement( JLabel.class ), registry.getElement( JComponent.class ) );
  }

  @Test
  public void testRegistry2() {
    registry = new TypeRegistry<ChildDetector<?, ?>>( false );

    registry.addElement( JComponent.class, new AbstractChildDetector<String, String>() {
      @Override
      @Nonnull
      public List<? extends String> findChildren( @Nonnull String parent ) {
        throw new UnsupportedOperationException();
      }
    } );

    assertNotNull( registry.getElement( JComponent.class ) );
    try {
      registry.getElement( JLabel.class );
      fail( "Where is the Exception" );
    } catch ( Exception ignore ) {
    }
  }
}
