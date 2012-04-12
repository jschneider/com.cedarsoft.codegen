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
import javax.annotation.Nullable;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * A wrapper for a lookup
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class LookupWrapper extends AbstractLookup implements LookupStore {
  private Lookup wrappedLookup;
  private Map<Class<?>, Object> store = new HashMap<Class<?>, Object>();

  private LookupChangeSupport lcs = new LookupChangeSupport( this );

  /**
   * Creates a new lookup wrapper
   *
   * @param wrappedLookup the wrapped lookup
   */
  public LookupWrapper( @Nonnull Lookup wrappedLookup ) {
    this.wrappedLookup = wrappedLookup;
    this.wrappedLookup.addChangeListener( new LookupChangeListener<Object>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends Object> event ) {
        //If it is overridden by the "local" store do nothing
        if ( store.get( event.getType() ) != null ) {
          return;
        }

        lcs.fireLookupChanged( ( Class<Object> ) event.getType(), event.getOldValue(), event.getNewValue() );
      }
    } );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void bind( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener ) {
    lcs.bind( type, lookupChangeListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void bind( @Nonnull TypedLookupChangeListener<T> lookupChangeListener ) {
    lcs.bind( lookupChangeListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void bindWeak( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener ) {
    lcs.bindWeak( type, lookupChangeListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void bindWeak( @Nonnull TypedLookupChangeListener<T> lookupChangeListener ) {
    lcs.bindWeak( lookupChangeListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addChangeListenerWeak( @Nonnull LookupChangeListener<?> lookupChangeListener ) {
    lcs.addChangeListenerWeak( lookupChangeListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void addChangeListenerWeak( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener ) {
    lcs.addChangeListenerWeak( type, lookupChangeListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addChangeListener( @Nonnull LookupChangeListener<?> lookupChangeListener ) {
    lcs.addChangeListener( lookupChangeListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeChangeListener( @Nonnull LookupChangeListener<?> lookupChangeListener ) {
    lcs.removeChangeListener( lookupChangeListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void addChangeListener( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener ) {
    lcs.addChangeListener( type, lookupChangeListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void removeChangeListener( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener ) {
    lcs.removeChangeListener( type, lookupChangeListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nullable
  public <T> T lookup( @Nonnull Class<T> type ) {
    Object value = store.get( type );
    if ( value != null ) {
      return type.cast( value );
    }
    return wrappedLookup.lookup( type );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void store( @Nonnull Class<T> type, @Nonnull T value ) {
    T old = type.cast( lookup( type ) );
    store.put( type, value );
    lcs.fireLookupChanged( type, old, value );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Map<Class<?>, Object> lookups() {
    Map<Class<?>, Object> lookups = new HashMap<Class<?>, Object>();

    lookups.putAll( wrappedLookup.lookups() );
    lookups.putAll( store );

    return Collections.unmodifiableMap( lookups );
  }

}
