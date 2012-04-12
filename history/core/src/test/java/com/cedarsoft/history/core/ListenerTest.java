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

import javax.annotation.Nonnull;
import org.joda.time.LocalDate;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class ListenerTest {
  @Before
  public void setUp() throws Exception {
  }

  @Test
  public void testListener() {
    DefaultHistory<DefaultHistoryEntry> history = new DefaultHistory<DefaultHistoryEntry>();

    final List<DefaultHistoryEntry> adds = new ArrayList<DefaultHistoryEntry>();
    final List<DefaultHistoryEntry> removes = new ArrayList<DefaultHistoryEntry>();

    history.addHistoryListener( new HistoryListener<DefaultHistoryEntry>() {
      @Override
      public void entryAdded( @Nonnull DefaultHistoryEntry entry ) {
        adds.add( entry );
      }

      @Override
      public void entryChanged( @Nonnull DefaultHistoryEntry entry ) {
      }

      @Override
      public void entryRemoved( @Nonnull DefaultHistoryEntry entry ) {
        removes.add( entry );
      }
    } );

    assertEquals( 0, adds.size() );
    history.addEntry( new DefaultHistoryEntry() );
    assertEquals( 1, adds.size() );

    assertEquals( 0, removes.size() );
    history.removeEntry( history.getEntry( history.getEntries().size() - 1 ) );
    assertEquals( 1, removes.size() );
  }

  @Test
  public void testListener2() {
    ContinuousEntriesInformation<ContinuousEntry> entriesInformation = new ContinuousEntriesInformation<ContinuousEntry>( new LocalDate( 2007, 1, 1 ), new LocalDate( 2008, 1, 1 ) );

    final List<ContinuousEntry> adds = new ArrayList<ContinuousEntry>();
    final List<ContinuousEntry> removes = new ArrayList<ContinuousEntry>();

    entriesInformation.addHistoryListener( new HistoryListener<ContinuousEntry>() {
      @Override
      public void entryChanged( @Nonnull ContinuousEntry entry ) {
      }

      @Override
      public void entryAdded( @Nonnull ContinuousEntry entry ) {
        adds.add( entry );
      }

      @Override
      public void entryRemoved( @Nonnull ContinuousEntry entry ) {
        removes.add( entry );
      }
    } );

    assertEquals( 0, adds.size() );
    entriesInformation.addEntry( new DefaultContinuousEntry( new LocalDate( 2007, 1, 1 ) ) );
    assertEquals( 1, adds.size() );

    assertEquals( 0, removes.size() );
    entriesInformation.removeLastEntry();
    assertEquals( 1, removes.size() );
  }

  @Test
  public void testListener3() {
    DiscreteHistory<DiscreteHistoryEntry> history = new DiscreteHistory<DiscreteHistoryEntry>();

    final List<DiscreteHistoryEntry> adds = new ArrayList<DiscreteHistoryEntry>();
    final List<DiscreteHistoryEntry> removes = new ArrayList<DiscreteHistoryEntry>();

    history.addHistoryListener( new HistoryListener<DiscreteHistoryEntry>() {
      @Override
      public void entryAdded( @Nonnull DiscreteHistoryEntry entry ) {
        adds.add( entry );
      }

      @Override
      public void entryRemoved( @Nonnull DiscreteHistoryEntry entry ) {
        removes.add( entry );
      }

      @Override
      public void entryChanged( @Nonnull DiscreteHistoryEntry entry ) {
      }
    } );

    assertEquals( 0, adds.size() );
    history.addEntry( new DefaultDiscreteHistoryEntry( new LocalDate( 2007, 1, 1 ) ) );
    assertEquals( 1, adds.size() );

    assertEquals( 0, removes.size() );
    history.removeEntry( history.getEntries().get( history.getEntries().size() - 1 ) );
    assertEquals( 1, removes.size() );
  }
}
