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

package com.cedarsoft.lookup;

import javax.annotation.Nonnull;
import org.junit.*;

import java.util.Collections;

import static org.junit.Assert.*;

/**
 *
 */
public class LookupChangeSupportTest {
  private LookupChangeSupport lookupChangeSupport;
  private MockLookup lookup;

  @Before
  public void setUp() throws Exception {
    lookup = new MockLookup();
    lookupChangeSupport = lookup.getLookupChangeSupport();
  }

  @Test
  public void testListenersSameDontFire() {
    LookupChangeListenerMock listener = new LookupChangeListenerMock();
    lookupChangeSupport.addChangeListener( listener );

    listener.verify();
    lookupChangeSupport.fireLookupChanged( String.class, "a", "a" );
    listener.verify();
    lookupChangeSupport.fireLookupChanged( new LookupChangeEvent<String>( lookup, String.class, "b", "b" ) );
    listener.verify();
    lookupChangeSupport.fireDelta( lookup, lookup );
    listener.verify();
    lookupChangeSupport.fireDelta( Lookups.singletonLookup( String.class, "c" ), Lookups.singletonLookup( String.class, "c" ) );
    listener.verify();
    lookupChangeSupport.fireDelta( Collections.<Class<?>, Object>singletonMap( String.class, "c" ), Lookups.singletonLookup( String.class, "c" ) );
    listener.verify();
  }

  @Test
  public void testListener() {
    assertEquals( 0, lookupChangeSupport.getListeners().size() );
    LookupChangeListenerMock listener = new LookupChangeListenerMock();
    lookup.addChangeListener( listener );
    assertEquals( 1, lookupChangeSupport.getListeners().size() );
    lookup.removeChangeListener( listener );
    assertEquals( 0, lookupChangeSupport.getListeners().size() );
  }

  @Test
  public void testAddWeak() {
    addWeakListener();
    assertEquals( 2, lookupChangeSupport.getListeners().size() );
    System.gc();
    System.gc();
    System.gc();
    System.gc();
    System.gc();
    assertEquals( 2, lookupChangeSupport.getListeners().size() );
    lookup.store( String.class, "asf" );
    assertEquals( 0, lookupChangeSupport.getListeners().size() );
  }

  private void addWeakListener() {
    lookupChangeSupport.addChangeListenerWeak( String.class, new LookupChangeListener<String>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends String> event ) {
      }
    } );
    lookupChangeSupport.addChangeListenerWeak( new LookupChangeListener<Object>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends Object> event ) {
      }
    } );
  }

  @Test
  public void testAddListeners() {
    assertEquals( 0, lookupChangeSupport.getListeners().size() );
    lookupChangeSupport.addChangeListener( String.class, new LookupChangeListener<Object>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends Object> event ) {
      }
    } );
    assertEquals( 1, lookupChangeSupport.getListeners().size() );
    lookupChangeSupport.addChangeListener( new LookupChangeListener<Object>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends Object> event ) {
      }
    } );

    assertEquals( 2, lookupChangeSupport.getListeners().size() );

    lookupChangeSupport.addChangeListener( String.class, new LookupChangeListener<Object>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends Object> event ) {
      }
    } );
    assertEquals( 3, lookupChangeSupport.getListeners().size() );
    lookupChangeSupport.addChangeListener( String.class, new LookupChangeListener<Object>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends Object> event ) {
      }
    } );


    assertEquals( 4, lookupChangeSupport.getListeners().size() );
  }
}
