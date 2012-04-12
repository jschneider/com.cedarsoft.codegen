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

import java.lang.Object;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A dynamic lookup
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class DynamicLookup extends AbstractLookup implements LookupStore {
  @Nonnull
  @SuppressWarnings( {"ThisEscapedInObjectConstruction"} )
  private final LookupChangeSupport lcs = new LookupChangeSupport( this );
  @Nonnull
  private final Map<Class<?>, Object> store = new HashMap<Class<?>, Object>();//todo use type registry(?)

  /**
   * Creates a new dynamic lookup
   *
   * @param values the values that are stored within the lookup
   */
  public DynamicLookup( @Nonnull Object... values ) {
    for ( Object value : values ) {
      addValue( value );
    }
  }

  /**
   * Clears the lookup
   */
  public void clear() {
    Map<Class<?>, Object> oldEntries = new HashMap<Class<?>, Object>( store );
    store.clear();
    lcs.fireDelta( oldEntries, this );
  }

  /**
   * {@inheritDoc}
   * <p/>
   * The type is ignored
   */
  @Override
  public <T> void store( @Nonnull Class<T> type, @Nonnull T value ) {
    addValue( value );
  }

  /**
   * Adds a value to the store. The value may be lookup up using the type of value and all its super classes
   * and interfaces.
   *
   * @param value the value that is added
   * @return a boolean.
   */
  public final boolean addValue( @Nonnull Object value ) {
    boolean added = false;

    //Create the store map
    //Super classes
    Class<?> type = value.getClass();
    while ( type != null ) {
      Object oldValue = store.put( type, value );
      //noinspection ObjectEquality
      added = added || oldValue != value;

      lcs.fireLookupChanged( ( ( Class<Object> ) type ), oldValue, value );

      boolean addedInterface = addInterfaces( value, type );
      added = added || addedInterface;

      type = type.getSuperclass();
    }

    return added;
  }

  /**
   * <p>removeValue</p>
   *
   * @param value a {@link Object} object.
   * @return a boolean.
   */
  public final boolean removeValue( @Nonnull Object value ) {
    boolean removed = false;

    //Create the store map
    //Super classes
    Class<?> type = value.getClass();
    while ( type != null ) {
      Object oldValue = store.remove( type );
      removed = removed || oldValue != null;

      lcs.fireLookupChanged( ( ( Class<Object> ) type ), oldValue, value );
      boolean removedInterface = removeInterfaces( value, type );
      removed = removed || removedInterface;

      type = type.getSuperclass();
    }

    return removed;
  }

  private boolean addInterfaces( @Nonnull Object value, @Nonnull Class<?> superType ) {
    boolean added = false;

    //Interfaces
    for ( Class<?> type : superType.getInterfaces() ) {
      Object oldValue = store.put( type, value );
      //noinspection ObjectEquality
      added = added || oldValue != value;

      lcs.fireLookupChanged( ( ( Class<Object> ) type ), oldValue, value );
      boolean addedInterface = addInterfaces( value, type );

      added = added || addedInterface;
    }

    return added;
  }

  private boolean removeInterfaces( @Nonnull Object value, @Nonnull Class<?> superType ) {
    boolean removed = false;

    //Interfaces
    for ( Class<?> type : superType.getInterfaces() ) {
      Object oldValue = store.remove( type );

      removed = removed || oldValue != null;

      lcs.fireLookupChanged( ( ( Class<Object> ) type ), oldValue, value );
      boolean removedInterface = removeInterfaces( value, type );

      removed = removed || removedInterface;
    }

    return removed;
  }

  /**
   * <p>addValues</p>
   *
   * @param objects a {@link Object} object.
   * @return a boolean.
   */
  public final boolean addValues( @Nonnull Object... objects ) {
    boolean added = false;

    for ( Object object : objects ) {
      boolean addedThis = addValue( object );
      added = added || addedThis;
    }

    return added;
  }

  /**
   * <p>addValues</p>
   *
   * @param lookup a {@link Lookup} object.
   * @return a boolean.
   */
  public final boolean addValues( @Nonnull Lookup lookup ) {
    boolean added = false;
    for ( Map.Entry<Class<?>, Object> entry : lookup.lookups().entrySet() ) {
      boolean addedThis = addValue( entry.getValue() );
      added = added || addedThis;
    }
    return added;
  }

  /**
   * <p>removeValues</p>
   *
   * @param lookup a {@link Lookup} object.
   * @return a boolean.
   */
  public final boolean removeValues( @Nonnull Lookup lookup ) {
    boolean removed = false;
    for ( Map.Entry<Class<?>, Object> entry : lookup.lookups().entrySet() ) {
      boolean removedThis = removeValue( entry.getValue() );
      removed = removed || removedThis;
    }
    return removed;
  }

  /**
   * <p>removeValues</p>
   *
   * @param objects a {@link Object} object.
   * @return a boolean.
   */
  public final boolean removeValues( @Nonnull Object... objects ) {
    boolean removed = false;
    for ( Object object : objects ) {
      boolean removedThis = removeValue( object );
      removed = removed || removedThis;
    }
    return removed;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nullable
  public <T> T lookup( @Nonnull Class<T> type ) {
    return type.cast( store.get( type ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Map<Class<?>, Object> lookups() {
    return Collections.unmodifiableMap( store );
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
  public void removeChangeListener( @Nonnull LookupChangeListener<?> lookupChangeListener ) {
    lcs.removeChangeListener( lookupChangeListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void removeChangeListener( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener ) {
    lcs.removeChangeListener( type, lookupChangeListener );
  }

  /**
   * <p>getLookupChangeListeners</p>
   *
   * @return a {@link List} object.
   */
  @Nonnull
  public List<? extends LookupChangeListener<?>> getLookupChangeListeners() {
    return lcs.getListeners();
  }
}
