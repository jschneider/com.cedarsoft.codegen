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
import java.util.AbstractMap;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Lazy implementation of lookup. This class will call {@link #getValue()} as late as possible and cache the results.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public abstract class LazyLookup<T> extends AbstractLookup {
  /**
   * The instance.
   */
  private T instance;
  /**
   * Caches the superclasses and super interfaces
   */
  private Map<Class<?>, Object> lookups;

  /**
   * <p>Constructor for LazyLookup.</p>
   */
  protected LazyLookup() {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Map<Class<?>, Object> lookups() {
    if ( this.lookups == null ) {
      Set<Map.Entry<Class<?>, Object>> entries = new HashSet<Map.Entry<Class<?>, Object>>();
      registerSuperTypes( getType(), entries );
      lookups = Collections.unmodifiableMap( new LookupsMap( entries ) );
    }
    //noinspection ReturnOfCollectionOrArrayField
    return lookups;
  }

  private void registerSuperTypes( @Nonnull Class<?> clazz, @Nonnull Set<Map.Entry<Class<?>, Object>> entries ) {
    Class<?> currentClass = clazz;
    //noinspection ObjectEquality
    while ( currentClass != null && currentClass != Object.class ) {
      entries.add( new LazyEntry( currentClass ) );
      for ( Class<?> aClass : currentClass.getInterfaces() ) {
        registerSuperTypes( aClass, entries );
        entries.add( new LazyEntry( aClass ) );
      }
      currentClass = currentClass.getSuperclass();
    }
  }

  /**
   * This method is only called once as soon as the first lookup request is made.
   *
   * @return an instance of {@link #getType()}
   */
  @Nonnull
  protected abstract T createInstance();

  /**
   * The type the instance.
   *
   * @return a {@link Class} object.
   */
  public abstract Class<? extends T> getType();

  /**
   * {@inheritDoc}
   * <p/>
   * This lookup calls {@link #getValue()} when the parameter fits the type ({@link #getType()}).
   */
  @Override
  @Nullable
  public <T> T lookup( @Nonnull Class<T> type ) {
    if ( type.isAssignableFrom( getType() ) ) {
      return type.cast( getValue() );
    }
    return null;
  }

  /**
   * This method calls {@link #createInstance()} and caches the value.
   * Subclasses should not override this method but {@link #createInstance()} instead.
   *
   * @return the value for the instance
   */
  @Nullable
  protected T getValue() {
    if ( instance == null ) {
      instance = createInstance();
    }
    return instance;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void bind( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener ) {
    lookupChangeListener.lookupChanged( new LookupChangeEvent<T>( this, type, null, lookup( type ) ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void bind( @Nonnull TypedLookupChangeListener<T> lookupChangeListener ) {
    bind( lookupChangeListener.getType(), lookupChangeListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void bindWeak( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener ) {
    lookupChangeListener.lookupChanged( new LookupChangeEvent<T>( this, type, null, lookup( type ) ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void bindWeak( @Nonnull TypedLookupChangeListener<T> lookupChangeListener ) {
    bindWeak( lookupChangeListener.getType(), lookupChangeListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addChangeListenerWeak( @Nonnull LookupChangeListener<?> lookupChangeListener ) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void addChangeListenerWeak( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener ) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void addChangeListener( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener ) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public <T> void removeChangeListener( @Nonnull Class<T> type, @Nonnull LookupChangeListener<? super T> lookupChangeListener ) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeChangeListener( @Nonnull LookupChangeListener<?> lookupChangeListener ) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addChangeListener( @Nonnull LookupChangeListener<?> lookupChangeListener ) {
  }

  /**
   * A special class map
   */
  private static final class LookupsMap extends AbstractMap<Class<?>, Object> {
    @Nonnull
    private final Set<Entry<Class<?>, Object>> entries;

    LookupsMap( @Nonnull Set<Entry<Class<?>, Object>> entries ) {
      //noinspection AssignmentToCollectionOrArrayFieldFromParameter
      this.entries = entries;
    }

    @Nonnull
    @Override
    public Set<Entry<Class<?>, Object>> entrySet() {
      //noinspection ReturnOfCollectionOrArrayField
      return entries;
    }
  }

  /**
   * An entry containing a lazy value.
   */
  private final class LazyEntry implements Map.Entry<Class<?>, Object> {
    @Nonnull
    private final Class<?> aClass;

    LazyEntry( @Nonnull Class<?> aClass ) {
      this.aClass = aClass;
    }

    @Override
    @Nonnull
    public Class<?> getKey() {
      return aClass;
    }

    @Override
    @Nullable
    public Object getValue() {
      return LazyLookup.this.getValue();
    }

    @Override
    public Object setValue( Object value ) {
      throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals( Object o ) {
      if ( this == o ) return true;
      if ( o == null || getClass() != o.getClass() ) return false;

      LazyEntry lazyEntry = ( LazyEntry ) o;

      if ( !aClass.equals( lazyEntry.aClass ) ) return false;

      return true;
    }

    @Override
    public int hashCode() {
      return aClass.hashCode();
    }
  }
}
