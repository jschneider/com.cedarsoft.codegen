/**
 * Copyright (C) 2010 cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce.txt
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

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This is a utily class that creates several lookups for special purposes
 */
public class Lookups {
  private static final Lookup EMPTY_LOOKUP = new EmptyLookup();

  private Lookups() {
  }

  @NotNull
  public static <T> InstantiatorLookup<T> instantiator( @NotNull Class<? extends T> type, @NotNull Instantiater<T> instantiater ) {
    return new InstantiatorLookup<T>( type, instantiater );
  }

  @NotNull
  public static <T> InstantiatorLookup<T> instantiator( @NotNull Instantiater.Typed<T> instantiater ) {
    return new InstantiatorLookup<T>( instantiater );
  }

  @NotNull
  public static MergingLookup merge( @NotNull Lookup first, @NotNull Lookup second ) {
    return new MergingLookup( first, second );
  }

  /**
   * Wraps a lookup
   *
   * @param wrapped the lookup that is wrapped
   * @return the lookup
   */
  @NotNull
  public static LookupWrapper wrap( @NotNull Lookup wrapped ) {
    return new LookupWrapper( wrapped );
  }

  /**
   * Creates a dynamit lookup
   *
   * @param objects the objects
   * @return the lookup
   */
  @NotNull
  public static DynamicLookup dynamicLookupFromList( @NotNull List<Object> objects ) {
    return dynamicLookup( objects.toArray() );
  }

  /**
   * Creates a singleton lookup
   *
   * @param type  the type
   * @param value the value
   * @return the singleton lookup
   */
  public static <T> Lookup singletonLookup( @NotNull Class<T> type, @NotNull T value ) {
    return new SingletonLookup<T>( type, value );
  }

  @NotNull
  public static MappedLookup mappedLookup( @NotNull Map<Class<?>, ?> values ) {
    return new MappedLookup( values );
  }

  /**
   * Create a dynamic lookup
   *
   * @param values the values
   * @return the dynamik lookup
   */
  public static DynamicLookup dynamicLookup( @NotNull Object... values ) {
    return new DynamicLookup( values );
  }

  /**
   * Creates an empty lookup
   *
   * @return the empty lookup
   */
  public static Lookup emtyLookup() {
    return EMPTY_LOOKUP;
  }

  /**
   * Creates a lookup where the given objects are registered only under their class
   *
   * @param objects the objects
   * @return the lookup
   */
  @NotNull
  public static Lookup createLookup( @NotNull Object... objects ) {
    MappedLookup lookup = new MappedLookup();
    for ( Object object : objects ) {
      lookup.store( ( Class<Object> ) object.getClass(), object );
    }
    return lookup;
  }

  private static class EmptyLookup extends AbstractLookup {
    @Override
    @Nullable
    public <T> T lookup( @NotNull Class<T> type ) {
      return null;
    }

    @Override
    @NotNull
    public Map<Class<?>, Object> lookups() {
      return Collections.emptyMap();
    }

    @Override
    public <T> void bind( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
      lookupChangeListener.lookupChanged( new LookupChangeEvent<T>( this, type, null, lookup( type ) ) );
    }

    @Override
    public <T> void bind( @NotNull TypedLookupChangeListener<T> lookupChangeListener ) {
      Class<T> type = lookupChangeListener.getType();
      lookupChangeListener.lookupChanged( new LookupChangeEvent<T>( this, type, null, lookup( type ) ) );
    }

    @Override
    public <T> void bindWeak( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
    }

    @Override
    public <T> void bindWeak( @NotNull TypedLookupChangeListener<T> lookupChangeListener ) {
    }

    @Override
    public void addChangeListenerWeak( @NotNull LookupChangeListener<?> lookupChangeListener ) {
    }

    @Override
    public <T> void addChangeListenerWeak( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
    }

    @Override
    public void addChangeListener( @NotNull LookupChangeListener<?> lookupChangeListener ) {
    }

    @Override
    public <T> void addChangeListener( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
    }

    @Override
    public void removeChangeListener( @NotNull LookupChangeListener<?> lookupChangeListener ) {
    }

    @Override
    public <T> void removeChangeListener( @NotNull Class<T> type, @NotNull LookupChangeListener<? super T> lookupChangeListener ) {
    }
  }
}
