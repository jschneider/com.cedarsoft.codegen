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

package com.cedarsoft.properties;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * <p>PropertiesPath class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class PropertiesPath {
  /** Constant <code>PROPERTY_REPRESENTATION="presentation"</code> */
  @NotNull
  @NonNls
  public static final String PROPERTY_REPRESENTATION = "presentation";
  /** Constant <code>PROPERTY_ROOT_PROPERTY="rootProperty"</code> */
  @NotNull
  @NonNls
  public static final String PROPERTY_ROOT_PROPERTY = "rootProperty";
  /** Constant <code>PROPERTY_ELEMENTS="elements"</code> */
  @NotNull
  @NonNls
  public static final String PROPERTY_ELEMENTS = "elements";

  @NotNull
  @NonNls
  private final List<String> elements = new ArrayList<String>();

  /**
   * Hibernate
   */
  @Deprecated
  protected PropertiesPath() {
  }

  /**
   * <p>Constructor for PropertiesPath.</p>
   *
   * @param elements a {@link java.lang.String} object.
   */
  public PropertiesPath( @NotNull @NonNls String... elements ) {
    this( Arrays.asList( elements ) );
  }

  /**
   * <p>Constructor for PropertiesPath.</p>
   *
   * @param elements a {@link java.util.List} object.
   */
  public PropertiesPath( @NotNull @NonNls List<String> elements ) {
    if ( elements.isEmpty() ) {
      throw new IllegalArgumentException( "Need at least one element in path" );
    }
    this.elements.addAll( elements );
  }

  /**
   * <p>getRootProperty</p>
   *
   * @return a {@link java.lang.String} object.
   */
  @NotNull
  @NonNls
  public String getRootProperty() {
    return elements.get( 0 );
  }

  /**
   * <p>Getter for the field <code>elements</code>.</p>
   *
   * @return a {@link java.util.List} object.
   */
  @NotNull
  public List<String> getElements() {
    return Collections.unmodifiableList( elements );
  }

  /** {@inheritDoc} */
  @Override
  public String toString() {
    return getPresentation();
  }

  /**
   * <p>getPresentation</p>
   *
   * @return a {@link java.lang.String} object.
   */
  @NotNull
  @NonNls
  public String getPresentation() {
    StringBuilder builder = new StringBuilder();

    for ( Iterator<String> it = elements.iterator(); it.hasNext(); ) {
      String element = it.next();
      builder.append( element );
      if ( it.hasNext() ) {
        builder.append( '.' );
      }
    }

    return builder.toString();
  }
}
