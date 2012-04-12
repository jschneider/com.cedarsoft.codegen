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

import org.fest.reflect.core.Reflection;
import javax.annotation.Nonnull;
import org.joda.time.LocalDate;
import org.junit.*;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

/**
 *
 */
public class DiscreteHistoryTest {
  private LocalDate begin;
  private LocalDate end;
  private DiscreteHistory<DefaultDiscreteHistoryEntry> history;

  @Before
  public void setUp() throws Exception {
    begin = new LocalDate( 2007, 04, 06 );
    end = new LocalDate( 2007, 8, 01 );

    history = new DiscreteHistory<DefaultDiscreteHistoryEntry>();
  }

  @After
  public void tearDown() throws Exception {
  }

  @Test
  public void testListenersUpdate() {
    try {
      history.getFirstSubHistory();
      fail( "Where is the Exception" );
    } catch ( HistoryNotFoundException e ) {
    }


    history.add( new DefaultDiscreteHistoryEntry() );
    History<DefaultDiscreteHistoryEntry> subHistory = history.getFirstSubHistory();
    assertEquals( 1, subHistory.getHistoryListeners().size() );

    subHistory.removeHistoryListener( subHistory.getHistoryListeners().get( 0 ) );
    Reflection.field( "listeners" ).ofType( Map.class ).in( history ).set( null );

    assertEquals( 0, subHistory.getHistoryListeners().size() );

    //Now check if the listener is readded
    assertNotNull( history.getFirstEntry() );
    assertEquals( 1, subHistory.getHistoryListeners().size() );
  }

  @Test
  public void testIsLastTest() {
    DefaultDiscreteHistoryEntry entry = new DefaultDiscreteHistoryEntry();
    assertFalse( history.isLatestEntry( entry ) );

    history.addEntry( entry );
    assertTrue( history.isLatestEntry( entry ) );

    history.removeEntry( entry );
    assertFalse( history.isLatestEntry( entry ) );
  }

  @Test
  public void testRemoveBug() {
    assertEquals( 0, history.getHistories().size() );

    DefaultDiscreteHistoryEntry first = new DefaultDiscreteHistoryEntry( begin );
    history.addEntry( first );
    DefaultDiscreteHistoryEntry second = new DefaultDiscreteHistoryEntry( begin );
    history.addEntry( second );
    DefaultDiscreteHistoryEntry third = new DefaultDiscreteHistoryEntry( begin );
    history.addEntry( third );

    assertEquals( 1, history.getHistories().size() );
    assertEquals( 3, history.getEntries().size() );
    assertEquals( 3, history.getSubHistory( begin ).getEntries().size() );

    history.removeEntry( second );
    assertEquals( 1, history.getHistories().size() );
    assertEquals( 2, history.getEntries().size() );
    assertEquals( 2, history.getSubHistory( begin ).getEntries().size() );

    history.removeEntry( first );
    assertEquals( 1, history.getHistories().size() );
    assertEquals( 1, history.getEntries().size() );
    assertEquals( 1, history.getSubHistory( begin ).getEntries().size() );

    history.removeEntry( third );
    assertEquals( 0, history.getEntries().size() );
    assertEquals( 0, history.getHistories().size() );
  }

  @Test
  public void testRemove() {
    DefaultDiscreteHistoryEntry first = new DefaultDiscreteHistoryEntry( begin );
    history.addEntry( first );

    assertEquals( 1, history.getEntries().size() );
    DefaultDiscreteHistoryEntry other = new DefaultDiscreteHistoryEntry( end );
    history.addEntry( other );
    assertEquals( 2, history.getEntries().size() );

    History<DefaultDiscreteHistoryEntry> subHistory = history.getSubHistory( begin );

    history.removeEntry( first );
    assertEquals( 1, history.getEntries().size() );
    assertTrue( history.isLatestEntry( other ) );


    history.addHistoryListener( new HistoryListener<DefaultDiscreteHistoryEntry>() {
      @Override
      public void entryAdded( @Nonnull DefaultDiscreteHistoryEntry entry ) {
        fail( "Argh, not unregistered" );
      }

      @Override
      public void entryRemoved( @Nonnull DefaultDiscreteHistoryEntry entry ) {
        fail( "Argh, not unregistered" );
      }

      @Override
      public void entryChanged( @Nonnull DefaultDiscreteHistoryEntry entry ) {
      }
    } );

    subHistory.addEntry( new DefaultDiscreteHistoryEntry() );
  }

  @Test
  public void testGetLastEntry() {
    try {
      history.getLastSubHistory();
      fail( "Where is the Exception" );
    } catch ( HistoryNotFoundException ignore ) {
    }

    history.addEntry( new DefaultDiscreteHistoryEntry( new LocalDate( 2007, 7, 1 ) ) );
    history.addEntry( new DefaultDiscreteHistoryEntry( new LocalDate( 2007, 5, 1 ) ) );
    history.addEntry( new DefaultDiscreteHistoryEntry( new LocalDate( 2007, 6, 1 ) ) );

    History<DefaultDiscreteHistoryEntry> lastHistory = history.getLastSubHistory();
    assertEquals( new LocalDate( 2007, 7, 1 ), lastHistory.getLatestEntry().getValidityDate() );

    assertEquals( history.getLastSubHistory().getLatestEntry(), history.getLatestEntry() );
  }

  @Test
  public void testResolving() {
    try {
      history.getSubHistoryBefore( new LocalDate( 2007, 4, 1 ) );
    } catch ( HistoryNotFoundException ignore ) {
    }

    history.addEntry( new DefaultDiscreteHistoryEntry( new LocalDate( 2007, 4, 1 ) ) );
    history.addEntry( new DefaultDiscreteHistoryEntry( new LocalDate( 2007, 5, 1 ) ) );

    assertEquals( new LocalDate( 2007, 4, 1 ), history.getSubHistoryBefore( new LocalDate( 2007, 5, 1 ) ).getLatestEntry().getValidityDate() );
    assertEquals( new LocalDate( 2007, 4, 1 ), history.getSubHistoryBefore( new LocalDate( 2007, 4, 2 ) ).getLatestEntry().getValidityDate() );
    assertEquals( new LocalDate( 2007, 5, 1 ), history.getSubHistoryBefore( new LocalDate( 2007, 5, 2 ) ).getLatestEntry().getValidityDate() );

    try {
      history.getSubHistoryBefore( new LocalDate( 2007, 4, 1 ) );
    } catch ( HistoryNotFoundException ignore ) {
    }

    assertEquals( new LocalDate( 2007, 5, 1 ), history.getBestSubHistoryFor( new LocalDate( 2007, 5, 1 ) ).getLatestEntry().getValidityDate() );
    assertEquals( new LocalDate( 2007, 4, 1 ), history.getBestSubHistoryFor( new LocalDate( 2007, 4, 2 ) ).getLatestEntry().getValidityDate() );
    assertEquals( new LocalDate( 2007, 4, 1 ), history.getBestSubHistoryFor( new LocalDate( 2007, 4, 1 ) ).getLatestEntry().getValidityDate() );
    assertEquals( new LocalDate( 2007, 5, 1 ), history.getBestSubHistoryFor( new LocalDate( 2007, 5, 2 ) ).getLatestEntry().getValidityDate() );
  }

  @Test
  public void testValues() {
    history.addEntry( new DefaultDiscreteHistoryEntry( new LocalDate( 2007, 1, 1 ) ) );
    history.addEntry( new DefaultDiscreteHistoryEntry( new LocalDate( 2007, 1, 1 ) ) );
    history.addEntry( new DefaultDiscreteHistoryEntry( new LocalDate( 2007, 2, 1 ) ) );

    List<? extends DefaultDiscreteHistoryEntry> entries = history.getLatestEntries();
    assertEquals( 2, entries.size() );
    assertEquals( new LocalDate( 2007, 1, 1 ), entries.get( 0 ).getValidityDate() );
    assertEquals( new LocalDate( 2007, 2, 1 ), entries.get( 1 ).getValidityDate() );
  }

  @Test
  public void testUpToDate() throws InterruptedException {
    history.addEntry( new DefaultDiscreteHistoryEntry( new LocalDate( 2007, 1, 1 ) ) );
    Thread.sleep( 1 );
    history.addEntry( new DefaultDiscreteHistoryEntry( new LocalDate( 2007, 1, 1 ) ) );
    Thread.sleep( 1 );
    history.addEntry( new DefaultDiscreteHistoryEntry( new LocalDate( 2007, 1, 1 ) ) );
    Thread.sleep( 1 );
    DefaultDiscreteHistoryEntry latest = new DefaultDiscreteHistoryEntry( new LocalDate( 2006, 1, 1 ) );
    history.addEntry( latest );

    assertEquals( 2, history.getHistories().size() );
    assertSame( latest, history.getMostUpToDateEntry() );
  }

  @Test
  public void testSubHistory2() {
    history.addEntry( new DefaultDiscreteHistoryEntry( new LocalDate( 2007, 1, 1 ) ) );
    history.addEntry( new DefaultDiscreteHistoryEntry( new LocalDate( 2007, 1, 1 ) ) );
    history.addEntry( new DefaultDiscreteHistoryEntry( new LocalDate( 2007, 1, 1 ) ) );

    assertEquals( 1, history.getHistories().size() );
  }

  @Test
  public void testSubHistory() {
    history.addEntry( new DefaultDiscreteHistoryEntry( begin ) );

    History<DefaultDiscreteHistoryEntry> subHistory = history.getSubHistory( begin );
    assertNotNull( subHistory );
    assertEquals( 1, subHistory.getEntries().size() );

    try {
      history.getSubHistory( end );
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
    }
  }

  @Test
  public void testIt() {
    DefaultDiscreteHistoryEntry entryBegin = new DefaultDiscreteHistoryEntry( begin );
    history.addEntry( entryBegin );
    DefaultDiscreteHistoryEntry entryEnd1 = new DefaultDiscreteHistoryEntry( end );
    history.addEntry( entryEnd1 );
    DefaultDiscreteHistoryEntry entryEnd2 = new DefaultDiscreteHistoryEntry( end, new LocalDate().plusDays( 1 ) );
    history.addEntry( entryEnd2 );

    assertEquals( 1, history.getEntries( begin ).size() );
    assertEquals( 2, history.getEntries( end ).size() );

    try {
      history.getEntries( new LocalDate( 1, 1, 1 ) );
      fail( "Where is the Exception" );
    } catch ( Exception e ) {
    }

    assertEquals( entryEnd2, history.getSubHistory( end ).getLatestEntry() );
  }

  @Test
  public void testWrongOrder() {
    history.addEntry( new DefaultDiscreteHistoryEntry( begin ) );
    history.addEntry( new DefaultDiscreteHistoryEntry( end ) );
    history.addEntry( new DefaultDiscreteHistoryEntry( end, new LocalDate().plusDays( 1 ) ) );
    history.addEntry( new DefaultDiscreteHistoryEntry( end, new LocalDate().plusDays( -2 ) ) );
    history.addEntry( new DefaultDiscreteHistoryEntry( begin ) );
  }
}
