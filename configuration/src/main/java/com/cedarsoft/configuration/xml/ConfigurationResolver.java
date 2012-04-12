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

package com.cedarsoft.configuration.xml;

import org.apache.commons.configuration.Configuration;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Resolves a value from a configuration.
 * This class offers a common access to a configuration for every type.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public abstract class ConfigurationResolver<T> {
  /**
   * Resulve the value of the configuration for the given key
   *
   * @param configuration the configuration the value is extracted for
   * @param key           the key
   * @return the value
   */
  @Nullable
  public abstract T resolve( @Nonnull Configuration configuration, @Nonnull String key );

  /**
   * Stores the value to the configuration
   *
   * @param configuration the configuration
   * @param key           the key
   * @param value         the value
   */
  public abstract void store( @Nonnull Configuration configuration, @Nonnull String key, @Nonnull T value );

  /**
   * Returns the resolver for the given type.
   *
   * @param type the type
   * @param <T>  a T object.
   * @return the resolver for the given type
   */
  @Nonnull
  public static <T> ConfigurationResolver<T> getResolver( @Nonnull Class<? extends T> type ) {
    ConfigurationResolver<?> resolver = resolvers.get( type );
    if ( resolver == null ) {
      throw new IllegalArgumentException( "No resolver found for type: " + type );
    }
    //noinspection unchecked
    return ( ConfigurationResolver<T> ) resolver;
  }

  @Nonnull
  private static final Map<Class<?>, ConfigurationResolver<?>> resolvers = new HashMap<Class<?>, ConfigurationResolver<?>>();

  static {
    resolvers.put( String.class, new StringResolver() );
    resolvers.put( Double.class, new DoubleResolver() );
    resolvers.put( Integer.class, new IntegerResolver() );
    resolvers.put( File.class, new FileResolver() );
    resolvers.put( List.class, new ListResolver() );
    resolvers.put( Map.class, new MapResolver() );
  }

  public static class MapResolver extends ConfigurationResolver<Map<?, ?>> {
    @Override
    @Nullable
    public Map<?, ?> resolve( @Nonnull Configuration configuration, @Nonnull String key ) {
      List<String> list = configuration.getList( key );
      if ( list.isEmpty() ) {
        return null;
      }

      Map<String, String> values = new HashMap<String, String>();
      for ( String raw : list ) {
        String[] parts = raw.split( "\\|" );
        if ( parts.length != 2 ) {
          throw new IllegalStateException( "Invalid Entry: \"" + raw + '\"' );
        }

        values.put( parts[0], parts[1] );
      }

      return values;
    }

    @Override
    public void store( @Nonnull Configuration configuration, @Nonnull String key, @Nonnull Map<?, ?> value ) {
      List<String> condenced = new ArrayList<String>();
      for ( Map.Entry<?, ?> entry : value.entrySet() ) {
        condenced.add( entry.getKey() + "|" + entry.getValue() );
      }
      configuration.setProperty( key, condenced );
    }
  }

  public static class ListResolver extends ConfigurationResolver<List<?>> {
    @Override
    @Nullable
    public List<?> resolve( @Nonnull Configuration configuration, @Nonnull String key ) {
      List<?> list = configuration.getList( key );
      if ( list.isEmpty() ) {
        return null;
      }
      return list;
    }

    @Override
    public void store( @Nonnull Configuration configuration, @Nonnull String key, @Nonnull List<?> value ) {
      configuration.setProperty( key, value );
    }
  }

  public static class StringResolver extends ConfigurationResolver<String> {
    @Override
    @Nullable
    public String resolve( @Nonnull Configuration configuration, @Nonnull String key ) {
      return configuration.getString( key );
    }

    @Override
    public void store( @Nonnull Configuration configuration, @Nonnull String key, @Nonnull String value ) {
      configuration.setProperty( key, value );
    }
  }

  public static class FileResolver extends ConfigurationResolver<File> {
    @Override
    @Nullable
    public File resolve( @Nonnull Configuration configuration, @Nonnull String key ) {
      String path = configuration.getString( key );
      return path == null ? null : new File( path );
    }

    @Override
    public void store( @Nonnull Configuration configuration, @Nonnull String key, @Nonnull File value ) {
      configuration.setProperty( key, value.getAbsolutePath() );
    }
  }

  private static class DoubleResolver extends ConfigurationResolver<Double> {
    @Override
    @Nullable
    public Double resolve( @Nonnull Configuration configuration, @Nonnull String key ) {
      return configuration.getDouble( key );
    }

    @Override
    public void store( @Nonnull Configuration configuration, @Nonnull String key, @Nonnull Double value ) {
      configuration.setProperty( key, value );
    }
  }

  private static class IntegerResolver extends ConfigurationResolver<Integer> {
    @Override
    @Nullable
    public Integer resolve( @Nonnull Configuration configuration, @Nonnull String key ) {
      return configuration.getInt( key );
    }

    @Override
    public void store( @Nonnull Configuration configuration, @Nonnull String key, @Nonnull Integer value ) {
      configuration.setProperty( key, value );
    }
  }
}
