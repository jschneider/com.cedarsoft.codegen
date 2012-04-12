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

package com.cedarsoft.commons;

import org.junit.*;

import java.util.Locale;
import java.util.ResourceBundle;

import static org.junit.Assert.*;

/**
 *
 */
public class ExtendedBooleanTest {
  @Before
  public void setUp() throws Exception {
    Locale.setDefault( Locale.US );
    ResourceBundle.clearCache();
  }

  @Test
  public void testResourceBundles() throws Exception {
    assertEquals( "", ResourceBundle.getBundle( ExtendedBoolean.class.getName() ).getLocale().getLanguage() );
    assertEquals( "", ResourceBundle.getBundle( ExtendedBoolean.class.getName(), Locale.FRENCH ).getLocale().getLanguage() );
    assertEquals( "", ResourceBundle.getBundle( ExtendedBoolean.class.getName(), Locale.ENGLISH ).getLocale().getLanguage() );
    assertEquals( "", ResourceBundle.getBundle( ExtendedBoolean.class.getName(), Locale.US ).getLocale().getLanguage() );
    assertEquals( "de", ResourceBundle.getBundle( ExtendedBoolean.class.getName(), Locale.GERMANY ).getLocale().getLanguage() );


    assertNotNull( ResourceBundle.getBundle( ExtendedBoolean.class.getName(), Locale.GERMANY ) );
    assertNotNull( ResourceBundle.getBundle( ExtendedBoolean.class.getName(), Locale.US ) );

    assertNotSame( ResourceBundle.getBundle( ExtendedBoolean.class.getName(), Locale.GERMANY ),
                   ResourceBundle.getBundle( ExtendedBoolean.class.getName(), Locale.ENGLISH )
    );

    assertNotNull( ResourceBundle.getBundle( ExtendedBoolean.class.getName(), Locale.GERMANY ) );
    assertNotNull( ResourceBundle.getBundle( ExtendedBoolean.class.getName(), Locale.US ) );
    assertNotSame( ResourceBundle.getBundle( ExtendedBoolean.class.getName(), Locale.GERMANY ),
                   ResourceBundle.getBundle( ExtendedBoolean.class.getName(), Locale.ENGLISH )
    );


    assertEquals( "Yes", ResourceBundle.getBundle( ExtendedBoolean.class.getName(), Locale.US ).getString( "TRUE" ) );
  }

  @Test
  public void testTranslation() {
    assertEquals( "Ja", ExtendedBoolean.TRUE.getDescription( Locale.GERMANY ) );
    assertEquals( "Yes", ExtendedBoolean.TRUE.getDescription( Locale.US ) );
    assertEquals( "Yes", ExtendedBoolean.TRUE.getDescription( Locale.FRANCE ) );

    assertEquals( "Nein", ExtendedBoolean.FALSE.getDescription( Locale.GERMANY ) );
    assertEquals( "No", ExtendedBoolean.FALSE.getDescription( Locale.US ) );
    assertEquals( "No", ExtendedBoolean.FALSE.getDescription( Locale.FRANCE ) );

    assertEquals( "?", ExtendedBoolean.UNKNOWN.getDescription( Locale.GERMANY ) );
    assertEquals( "?", ExtendedBoolean.UNKNOWN.getDescription( Locale.US ) );
    assertEquals( "?", ExtendedBoolean.UNKNOWN.getDescription( Locale.FRANCE ) );
  }

  @Test
  public void testValueOf() {
    assertEquals( ExtendedBoolean.TRUE, ExtendedBoolean.valueOf( true ) );
    assertEquals( ExtendedBoolean.FALSE, ExtendedBoolean.valueOf( false ) );
  }

  @Test
  public void testTranslation2() {
    Locale old = Locale.getDefault();

    try {
      Locale.setDefault( Locale.GERMANY );
      assertEquals( "Ja", ExtendedBoolean.TRUE.getDescription() );
      assertEquals( "Nein", ExtendedBoolean.FALSE.getDescription() );
      assertEquals( "?", ExtendedBoolean.UNKNOWN.getDescription() );
    } finally {
      Locale.setDefault( old );
    }
  }

  @Test
  public void testBasic() {
    assertTrue( ExtendedBoolean.TRUE.isTrue() );
    assertFalse( ExtendedBoolean.TRUE.isFalse() );
    assertFalse( ExtendedBoolean.TRUE.isUnknown() );

    assertFalse( ExtendedBoolean.FALSE.isTrue() );
    assertTrue( ExtendedBoolean.FALSE.isFalse() );
    assertFalse( ExtendedBoolean.FALSE.isUnknown() );

    assertFalse( ExtendedBoolean.UNKNOWN.isTrue() );
    assertFalse( ExtendedBoolean.UNKNOWN.isFalse() );
    assertTrue( ExtendedBoolean.UNKNOWN.isUnknown() );
  }
}
