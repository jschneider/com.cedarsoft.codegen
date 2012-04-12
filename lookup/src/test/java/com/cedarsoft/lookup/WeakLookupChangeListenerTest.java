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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class WeakLookupChangeListenerTest {
  private MockLookup lookup;

  @Before
  public void setUp() throws Exception {
    lookup = new MockLookup();
  }

  @Test
  public void testWeakRemove() {
    LookupChangeListener<String> listener = new LookupChangeListener<String>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends String> event ) {
      }
    };
    lookup.addChangeListenerWeak( listener );
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );
    assertSame( listener, ( ( WeakLookupChangeListener<?> ) lookup.getLookupChangeSupport().getListeners().get( 0 ) ).getWrappedListener() );
    lookup.removeChangeListener( listener );
    assertEquals( 0, lookup.getLookupChangeSupport().getListeners().size() );
  }

  @Test
  public void testAdd() {
    LookupChangeListener<String> listener = new LookupChangeListener<String>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends String> event ) {
      }
    };
    lookup.addChangeListenerWeak( listener );
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );
    assertSame( listener, ( ( WeakLookupChangeListener<?> ) lookup.getLookupChangeSupport().getListeners().get( 0 ) ).getWrappedListener() );
  }

  @Test
  public void testWeakDirect() {
    {
      lookup.addChangeListenerWeak( new LookupChangeListener<String>() {
        @Override
        public void lookupChanged( @Nonnull LookupChangeEvent<? extends String> event ) {
        }
      } );
    }
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );
    gc();
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );

    lookup.store( String.class, "asf" );
    assertEquals( 0, lookup.getLookupChangeSupport().getListeners().size() );
  }

  @Test
  public void testWeakListener() {
    lookup.addChangeListener( new WeakLookupChangeListener<String>( String.class, new LookupChangeListener<String>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends String> event ) {
      }
    } ) );
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );
    gc();
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );

    lookup.store( String.class, "asf" );
    assertEquals( 0, lookup.getLookupChangeSupport().getListeners().size() );
  }

  @Test
  public void testWeakFactoryMethod() {
    lookup.addChangeListener( WeakLookupChangeListener.wrap( String.class, new LookupChangeListener<String>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends String> event ) {
      }
    } ) );
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );
    gc();
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );

    lookup.store( String.class, "asf" );
    assertEquals( 0, lookup.getLookupChangeSupport().getListeners().size() );
  }

  @Test
  public void testWeakBind() {
    lookup.bindWeak( String.class, new LookupChangeListener<String>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends String> event ) {
      }
    } );
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );
    gc();
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );

    lookup.store( String.class, "asf" );
    assertEquals( 0, lookup.getLookupChangeSupport().getListeners().size() );
  }

  @Test
  public void testWeakBindTyped() {
    lookup.bindWeak( new TypedLookupChangeListener<String>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends String> event ) {
      }

      @Override
      @Nonnull
      public Class<String> getType() {
        return String.class;
      }
    } );
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );
    gc();
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );

    lookup.store( String.class, "asf" );
    assertEquals( 0, lookup.getLookupChangeSupport().getListeners().size() );
  }

  @Test
  public void testWeakAdd() {
    lookup.addChangeListenerWeak( String.class, new LookupChangeListener<String>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends String> event ) {
      }
    } );
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );
    gc();
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );

    lookup.store( String.class, "asf" );
    assertEquals( 0, lookup.getLookupChangeSupport().getListeners().size() );
  }

  @Test
  public void testWeakAdd2() {
    lookup.addLookupChangeListenerWeak( new LookupChangeListener<String>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends String> event ) {
      }
    } );
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );
    gc();
    assertEquals( 1, lookup.getLookupChangeSupport().getListeners().size() );

    lookup.store( String.class, "asf" );
    assertEquals( 0, lookup.getLookupChangeSupport().getListeners().size() );
  }

  @Test
  public void testWrap() {
    final List<LookupChangeEvent<?>> events = new ArrayList<LookupChangeEvent<?>>();

    lookup.addChangeListener( WeakLookupChangeListener.wrap( new LookupChangeListener<Object>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends Object> event ) {
        events.add( event );
      }
    } ) );

    lookup.store( String.class, "asf" );
    assertEquals( 1, events.size() );
  }

  private static void gc() {
    for ( int i = 0; i < 20; i++ ) {
      System.gc();
    }
  }
}
