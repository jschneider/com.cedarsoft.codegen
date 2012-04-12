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

package com.cedarsoft.history.core;

import org.joda.time.LocalDate;
import org.junit.*;
import org.junit.rules.*;

import static org.junit.Assert.*;

/**
 *
 */
public class ContinuousEntriesInformationTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private LocalDate begin;
  private LocalDate end;
  private ContinuousEntriesInformation<DefaultContinuousEntry> information;
  private LocalDate middle;

  @Before
  public void setUp() throws Exception {
    begin = new LocalDate( 2007, 04, 06 );
    end = new LocalDate( 2007, 8, 01 );
    information = new ContinuousEntriesInformation<DefaultContinuousEntry>( begin, end );
    middle = new LocalDate( 2007, 7, 01 );
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testFindNextEntry() {
    DefaultContinuousEntry first = new DefaultContinuousEntry( begin );
    information.addEntry( first );
    DefaultContinuousEntry second = new DefaultContinuousEntry( middle );
    information.addEntry( second );

    assertSame( second, information.findNextEntry( first ) );

    thrown.expect( NoValidElementFoundException.class );
    information.findNextEntry( second );
  }

  @Test
  public void testFind() {
    DefaultContinuousEntry first = new DefaultContinuousEntry( begin );
    information.addEntry( first );
    DefaultContinuousEntry second = new DefaultContinuousEntry( middle );
    information.addEntry( second );

    assertSame( first, information.findEntry( begin ) );
    assertSame( first, information.findEntry( begin.plusDays( 2 ) ) );

    assertSame( second, information.findEntry( middle ) );
    assertSame( second, information.findEntry( middle.plusDays( 1 ) ) );
    try {
      information.findEntry( end );
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
    }

    assertEquals( 0, information.findEntries( begin.minusDays( 1 ), begin ).size() );
    assertEquals( 1, information.findEntries( begin, middle ).size() );
    assertEquals( 2, information.findEntries( begin, middle.plusDays( 1 ) ).size() );
  }

  @Test
  public void testInterval() {
    DefaultContinuousEntry first = new DefaultContinuousEntry( begin );
    information.addEntry( first );
    DefaultContinuousEntry second = new DefaultContinuousEntry( middle );
    information.addEntry( second );


    DefaultContinuousEntry entry = information.getLatestEntry();
    assertSame( second, entry );
  }

  @Test
  public void testInsertAppends() {
    information.addEntry( new DefaultContinuousEntry( begin ) );
    assertEquals( 1, information.getEntries().size() );

    information.addEntry( new DefaultContinuousEntry( middle ) );
    assertEquals( 2, information.getEntries().size() );
  }

  @Test
  public void testInsertDelete() {
    LocalDate m1 = middle.plusDays( 1 );
    LocalDate m2 = middle.plusDays( 2 );

    information.addEntry( new DefaultContinuousEntry( begin ) );
    information.addEntry( new DefaultContinuousEntry( middle ) );
    information.addEntry( new DefaultContinuousEntry( m1 ) );

    assertEquals( 3, information.getEntries().size() );

    try {
      information.addEntry( new DefaultContinuousEntry( middle ) );
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
    }

    try {
      information.addEntry( new DefaultContinuousEntry( m1 ) );
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
    }

    information.addEntry( new DefaultContinuousEntry( m2 ) );
    assertEquals( 4, information.getEntries().size() );
  }

  @Test
  public void testInsertOverlap() {
    information.addEntry( new DefaultContinuousEntry( begin ) );
    information.addEntry( new DefaultContinuousEntry( middle ) );

    assertEquals( 2, information.getEntries().size() );

    assertEquals( begin, information.getEntries().get( 0 ).getBegin() );
    assertEquals( middle, information.getEntries().get( 1 ).getBegin() );
  }

  @Test
  public void testInsert() {
    information.addEntry( new DefaultContinuousEntry( begin ) );
    assertEquals( 1, information.getEntries().size() );
    assertTrue( !information.getEntries().isEmpty() );


    //Insert 1
    information.addEntry( new DefaultContinuousEntry( middle ) );
    assertTrue( !information.getEntries().isEmpty() );
    assertEquals( 2, information.getEntries().size() );

    assertEquals( begin, information.getEntries().get( 0 ).getBegin() );
    assertEquals( middle, information.getEntries().get( 1 ).getBegin() );

    //Insert 2
    information.addEntry( new DefaultContinuousEntry( middle.plusDays( 2 ) ) );
    assertTrue( !information.getEntries().isEmpty() );
    assertEquals( 3, information.getEntries().size() );
    assertEquals( begin, information.getEntries().get( 0 ).getBegin() );
    assertEquals( middle, information.getEntries().get( 1 ).getBegin() );
    assertEquals( middle.plusDays( 2 ), information.getEntries().get( 2 ).getBegin() );


    //Insert 3 - in the middle
    information.addEntry( new DefaultContinuousEntry( middle.plusDays( 1 ) ) );
    assertTrue( !information.getEntries().isEmpty() );
    assertEquals( 4, information.getEntries().size() );

    assertEquals( begin, information.getEntries().get( 0 ).getBegin() );
    assertEquals( middle, information.getEntries().get( 1 ).getBegin() );
    assertEquals( middle.plusDays( 1 ), information.getEntries().get( 2 ).getBegin() );
    assertEquals( middle.plusDays( 2 ), information.getEntries().get( 3 ).getBegin() );
  }

  @Test
  public void testAdd() {
    information.addEntry( new DefaultContinuousEntry( middle ) );
    information.addEntry( new DefaultContinuousEntry( begin ) );
    try {
      information.addEntry( new DefaultContinuousEntry( begin ) );
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
    }
    try {
      information.addEntry( new DefaultContinuousEntry( middle ) );
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
    }

  }

  @Test
  public void testRemove() {
    information.addEntry( new DefaultContinuousEntry( begin ) );
    assertEquals( 1, information.getEntries().size() );
    information.removeLastEntry();
    assertEquals( 0, information.getEntries().size() );

    thrown.expect( Exception.class );
    information.removeLastEntry();
  }

  @Test
  public void testSimpeHistory() {
    assertFalse( !information.getEntries().isEmpty() );

    assertEquals( begin, information.getBegin() );
    assertEquals( end, information.getEnd() );

    assertEquals( 0, information.getEntries().size() );
    assertFalse( !information.getEntries().isEmpty() );

    information.addEntry( new DefaultContinuousEntry( information.getBegin() ) );
    assertEquals( 1, information.getEntries().size() );
    assertTrue( !information.getEntries().isEmpty() );
  }

  @Test
  public void testResolve() {
    information.addEntry( new DefaultContinuousEntry( begin ) );
    information.addEntry( new DefaultContinuousEntry( middle ) );
    assertTrue( !information.getEntries().isEmpty() );

    assertNotNull( information.findEntry( new LocalDate( begin.plusDays( 4 ) ) ) );
    assertNotNull( information.findEntry( new LocalDate( middle.plusDays( 1 ) ) ) );
    assertNotSame( information.findEntry( new LocalDate( begin.plusDays( 4 ) ) ), information.findEntry( new LocalDate( middle.plusDays( 1 ) ) ) );
  }
}
