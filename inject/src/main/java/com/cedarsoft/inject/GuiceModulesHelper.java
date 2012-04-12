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

package com.cedarsoft.inject;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <p>GuiceModulesHelper class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class GuiceModulesHelper {
  private GuiceModulesHelper() {
  }

  /**
   * <p>minimize</p>
   *
   * @param modules   a {@link List} object.
   * @param testTypes a {@link Class} object.
   * @return a {@link GuiceModulesHelper.Result} object.
   */
  @Nonnull
  public static Result minimize( @Nonnull List<? extends Module> modules, @Nonnull Class<?>... testTypes ) {
    return minimize( modules, convertToKeys( testTypes ) );
  }

  @Nonnull
  public static Key<?>[] convertToKeys( @Nonnull Class<?>... types ) {
    Key<?>[] keys = new Key<?>[types.length];

    for ( int i = 0, testTypesLength = types.length; i < testTypesLength; i++ ) {
      keys[i] = Key.get( types[i] );
    }
    return keys;
  }

  /**
   * <p>minimize</p>
   *
   * @param modules a {@link List} object.
   * @param keys    a {@link Key} object.
   * @return a {@link GuiceModulesHelper.Result} object.
   */
  @Nonnull
  public static Result minimize( @Nonnull List<? extends Module> modules, @Nonnull Key<?>... keys ) {
    //Verify to ensure it works with all modules
    verifyInjection( modules, keys );

    return minimize( new Result( modules ), keys );
  }

  /**
   * <p>minimize</p>
   *
   * @param result   a {@link GuiceModulesHelper.Result} object.
   * @param testType a {@link Class} object.
   * @return a {@link GuiceModulesHelper.Result} object.
   */
  @Nonnull
  public static Result minimize( @Nonnull Result result, @Nonnull Class<?> testType ) {
    return minimize( result, Key.get( testType ) );
  }

  /**
   * <p>minimize</p>
   *
   * @param result a {@link GuiceModulesHelper.Result} object.
   * @param keys   a {@link Key} object.
   * @return a {@link GuiceModulesHelper.Result} object.
   */
  public static Result minimize( @Nonnull Result result, @Nonnull Key<?>... keys ) {
    //Iterate over all types (copy because the result is updated)
    List<Module> modules = new ArrayList<Module>( result.getTypes() );
    for ( Module current : modules ) {

      try {
        Collection<Module> copy = new ArrayList<Module>( modules );
        copy.remove( current );
        verifyInjection( copy, keys );

        //Update the result
        result.remove( current );

        if ( copy.isEmpty() ) {
          result.removeAll();
          return result; //fast exit
        }

        //Try to minimize further
        return minimize( result, keys );
      } catch ( Exception ignore ) {
      }
    }

    return result; //no minimization
  }

  private static void verifyInjection( @Nonnull Iterable<? extends Module> modules, @Nonnull Key<?>... keys ) {
    if ( keys.length == 0 ) {
      throw new IllegalArgumentException( "Need at least one key" );
    }

    Injector injector = Guice.createInjector( modules );

    for ( Key<?> key : keys ) {
      injector.getInstance( key );
    }
  }

  /**
   * <p>assertMinimizeNotPossible</p>
   *
   * @param modules   a {@link List} object.
   * @param testTypes the {@link Class} types.
   * @throws AssertionError if any.
   */
  public static void assertMinimizeNotPossible( @Nonnull List<? extends Module> modules, @Nonnull Class<?>... testTypes ) throws AssertionError {
    assertMinimizeNotPossible( modules, convertToKeys( testTypes ) );
  }

  /**
   * <p>assertMinimizeNotPossible</p>
   *
   * @param modules a {@link List} object.
   * @param keys    a {@link Key} object.
   * @throws AssertionError if any.
   */
  public static void assertMinimizeNotPossible( @Nonnull List<? extends Module> modules, @Nonnull Key<?>... keys ) throws AssertionError {
    GuiceModulesHelper.Result minimal = minimize( modules, keys );
    if ( !minimal.getRemoved().isEmpty() ) {
      throw new AssertionError( "Can be minimized:\nRemove:\n" + minimal.getRemovedClassNamesAsString() + minimal.asInstantiations() );
    }
  }

  public static class Result {
    @Nonnull
    private final List<? extends Module> types;

    @Nonnull
    private final List<Module> removed = new ArrayList<Module>();

    public Result( @Nonnull List<? extends Module> types ) {
      this.types = new ArrayList<Module>( types );
    }

    public int size() {
      return types.size();
    }

    public void removeAll() {
      removed.addAll( types );
      types.clear();
    }

    public void remove( @Nonnull Module toRemove ) {
      types.remove( toRemove );
      removed.add( toRemove );
    }

    @Nonnull
    public List<? extends Module> getTypes() {
      return Collections.unmodifiableList( types );
    }

    @Nonnull
    public List<? extends Module> getRemoved() {
      return Collections.unmodifiableList( removed );
    }

    @Nonnull
    public String getRemovedClassNamesAsString() {
      StringBuilder builder = new StringBuilder();

      for ( Module module : removed ) {
        builder.append( "- " );
        builder.append( module.getClass().getName() );
        builder.append( "\n" );
      }

      return builder.toString();
    }

    @Nonnull
    public String asInstantiations() {
      StringBuilder builder = new StringBuilder();

      for ( Iterator<? extends Module> iterator = types.iterator(); iterator.hasNext(); ) {
        Module module = iterator.next();
        builder.append( "new " ).append( module.getClass().getName() ).append( "()" );

        if ( iterator.hasNext() ) {
          builder.append( ", " );
        }
      }

      return builder.toString();
    }
  }
}

