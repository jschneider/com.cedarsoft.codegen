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

package com.cedarsoft.registry;

import javax.annotation.Nonnull;

import java.lang.IllegalArgumentException;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a special registry that registers an implementation of T for
 * given type. (A map with a class as key and value T).
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 * @param <T> the type that is stored within this registry
 */
public class TypeRegistry<T> {
  private final boolean searchSuperTypes;
  @Nonnull
  private final Map<Class<?>, T> elements = new HashMap<Class<?>, T>();

  /**
   * Creates a new registry with
   * {@link #searchSuperTypes} set to true.
   */
  public TypeRegistry() {
    this( true );
  }

  /**
   * Creates a new registry
   *
   * @param searchSuperTypes whether the super types are used as keys
   */
  public TypeRegistry( boolean searchSuperTypes ) {
    this.searchSuperTypes = searchSuperTypes;
  }

  /**
   * Sets the elements. This method iterates over the given elements and calls {@link #addElement(Class,Object)}
   * for each entry.
   *
   * @param elements the elements that are added
   */
  public void setElements( @Nonnull Map<Class<?>, T> elements ) {
    this.elements.clear();
    for ( Map.Entry<Class<?>, T> entry : elements.entrySet() ) {
      addElement( entry.getKey(), entry.getValue() );
    }
  }

  /**
   * Returns whether the search for super types is enabled
   *
   * @return whether the search for super types is enabled
   */
  public boolean isSearchSuperTypes() {
    return searchSuperTypes;
  }

  /**
   * Adds the element.
   * if {@link #isSearchSuperTypes()} is set to true, the element is also
   * added for all superclasses and declared interfaces.
   *
   * @param type the type
   * @param t    the element that is stored for the given type
   */
  public void addElement( @Nonnull Class<?> type, @Nonnull T t ) {
    elements.put( type, t );
  }

  /**
   * Returns the stored element for the given type
   *
   * @param type the type
   * @return the stored element
   *
   * @throws IllegalArgumentException
   *          if any.
   */
  @Nonnull
  public T getElement( @Nonnull Class<?> type ) throws IllegalArgumentException {
    {
      T element = elements.get( type );
      if ( element != null ) {
        return element;
      }
    }

    if ( searchSuperTypes ) {
      //Now search for all interfaces
      for ( Class<?> interfaceType : type.getInterfaces() ) {
        T element = elements.get( interfaceType );
        if ( element != null ) {
          return element;
        }
      }

      //Now search for super types
      Class<?> superType = type.getSuperclass();
      if ( superType != null ) {
        try {
          return getElement( superType );
        } catch ( IllegalArgumentException ignore ) {
        }
      }
    }
    throw new IllegalArgumentException( "No element found for " + type );
  }
}
