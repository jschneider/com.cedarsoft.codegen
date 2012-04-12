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

import java.util.HashMap;
import java.util.Map;

/**
 * Merges two lookups. The first lookup has predecense. Every object that is contained within the first lookup
 * is resolved using the first.
 * Only if no object of a given type is contained within the first lookup the second lookup is queried.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class MergingLookup extends AbstractLookup implements Lookup {
  private LookupChangeSupport lcs = new LookupChangeSupport( this );

  private Lookup first;
  private Lookup second;

  /**
   * <p>Constructor for MergingLookup.</p>
   *
   * @param first  a {@link Lookup} object.
   * @param second a {@link Lookup} object.
   */
  public MergingLookup( @Nonnull Lookup first, @Nonnull Lookup second ) {
    this.first = first;
    this.second = second;

    first.addChangeListener( new LookupChangeListener<Object>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<?> event ) {
        lcs.fireLookupChanged( new LookupChangeEvent<Object>( MergingLookup.this, ( Class<Object> ) event.getType(), event.getOldValue(), event.getNewValue() ) );
      }
    } );

    second.addChangeListener( new LookupChangeListener<Object>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<?> event ) {
        Class<?> type = event.getType();
        if ( MergingLookup.this.first.lookup( type ) == null ) {
          lcs.fireLookupChanged( new LookupChangeEvent<Object>( MergingLookup.this, ( Class<Object> ) type, event.getOldValue(), event.getNewValue() ) );
        }
      }
    } );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nullable
  public <T> T lookup( @Nonnull Class<T> type ) {
    T firstValue = first.lookup( type );
    if ( firstValue != null ) {
      return firstValue;
    }
    return second.lookup( type );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Map<Class<?>, Object> lookups() {
    Map<Class<?>, Object> map = new HashMap<Class<?>, Object>();
    map.putAll( second.lookups() );
    //automatically overwrite all values from the second now if necessary
    map.putAll( first.lookups() );
    return map;
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
  public void removeChangeListener( @Nonnull LookupChangeListener<?> lookupChangeListener ) {
    lcs.removeChangeListener( lookupChangeListener );
  }
}
