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

import java.lang.Class;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * This is a utily class that creates several lookups for special purposes
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class Lookups {
  private static final Lookup EMPTY_LOOKUP = new EmptyLookup();

  private Lookups() {
  }

  /**
   * <p>instantiator</p>
   *
   * @param type         a {@link Class} object.
   * @param instantiater a {@link Instantiater} object.
   * @return a {@link InstantiatorLookup} object.
   */
  @Nonnull
  public static <T> InstantiatorLookup<T> instantiator( @Nonnull Class<? extends T> type, @Nonnull Instantiater<T> instantiater ) {
    return new InstantiatorLookup<T>( type, instantiater );
  }

  /**
   * <p>instantiator</p>
   *
   * @param instantiater a {@link Instantiater.Typed} object.
   * @return a {@link InstantiatorLookup} object.
   */
  @Nonnull
  public static <T> InstantiatorLookup<T> instantiator( @Nonnull Instantiater.Typed<T> instantiater ) {
    return new InstantiatorLookup<T>( instantiater );
  }

  /**
   * <p>merge</p>
   *
   * @param first  a {@link Lookup} object.
   * @param second a {@link Lookup} object.
   * @return a {@link MergingLookup} object.
   */
  @Nonnull
  public static MergingLookup merge( @Nonnull Lookup first, @Nonnull Lookup second ) {
    return new MergingLookup( first, second );
  }

  /**
   * Wraps a lookup
   *
   * @param wrapped the lookup that is wrapped
   * @return the lookup
   */
  @Nonnull
  public static LookupWrapper wrap( @Nonnull Lookup wrapped ) {
    return new LookupWrapper( wrapped );
  }

  /**
   * Creates a dynamit lookup
   *
   * @param objects the objects
   * @return the lookup
   */
  @Nonnull
  public static DynamicLookup dynamicLookupFromList( @Nonnull List<Object> objects ) {
    return dynamicLookup( objects.toArray() );
  }

  /**
   * Creates a singleton lookup
   *
   * @param type  the type
   * @param value the value
   * @param <T>   a T object.
   * @return the singleton lookup
   */
  public static <T> Lookup singletonLookup( @Nonnull Class<T> type, @Nonnull T value ) {
    return new SingletonLookup<T>( type, value );
  }

  /**
   * <p>mappedLookup</p>
   *
   * @param values a {@link Map} object.
   * @return a {@link MappedLookup} object.
   */
  @Nonnull
  public static MappedLookup mappedLookup( @Nonnull Map<Class<?>, ?> values ) {
    return new MappedLookup( values );
  }

  /**
   * Create a dynamic lookup
   *
   * @param values the values
   * @return the dynamik lookup
   */
  public static DynamicLookup dynamicLookup( @Nonnull Object... values ) {
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
  @Nonnull
  public static Lookup createLookup( @Nonnull Object... objects ) {
    MappedLookup lookup = new MappedLookup();
    for ( Object object : objects ) {
      lookup.store( ( Class<Object> ) object.getClass(), object );
    }
    return lookup;
  }

  private static class EmptyLookup extends AbstractLookup {
    @Override
    @Nullable
    public <T> T lookup( @Nonnull Class<T> type ) {
      return null;
    }

    @Override
    @Nonnull
    public Map<Class<?>, Object> lookups() {
      return Collections.emptyMap();
    }

    @Override
    public <T> void bind( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener ) {
      lookupChangeListener.lookupChanged( new LookupChangeEvent<T>( this, type, null, lookup( type ) ) );
    }

    @Override
    public <T> void bind( @Nonnull TypedLookupChangeListener<T> lookupChangeListener ) {
      Class<T> type = lookupChangeListener.getType();
      lookupChangeListener.lookupChanged( new LookupChangeEvent<T>( this, type, null, lookup( type ) ) );
    }

    @Override
    public <T> void bindWeak( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener ) {
    }

    @Override
    public <T> void bindWeak( @Nonnull TypedLookupChangeListener<T> lookupChangeListener ) {
    }

    @Override
    public void addChangeListenerWeak( @Nonnull LookupChangeListener<?> lookupChangeListener ) {
    }

    @Override
    public <T> void addChangeListenerWeak( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener ) {
    }

    @Override
    public void addChangeListener( @Nonnull LookupChangeListener<?> lookupChangeListener ) {
    }

    @Override
    public <T> void addChangeListener( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener ) {
    }

    @Override
    public void removeChangeListener( @Nonnull LookupChangeListener<?> lookupChangeListener ) {
    }

    @Override
    public <T> void removeChangeListener( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener ) {
    }
  }
}
