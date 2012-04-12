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

package com.cedarsoft.cluster;


import com.cedarsoft.objectaccess.ChangeListener;
import com.cedarsoft.objectaccess.ChangeListenerSupport;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.lang.Object;
import java.lang.String;

/**
 * A change listener support with transient and non transient listeners support
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class ClusteredChangeListenerSupport<T> {
  @Nonnull
  private final ChangeListenerSupport<T> transientSupport;
  @Nonnull
  private final TransientChangeListenerSupport<T> nonTransientSupport;

  @Nullable
  private ContextProvider contextProvider;

  /**
   * <p>Constructor for ClusteredChangeListenerSupport.</p>
   *
   * @param observerdObject a T object.
   */
  public ClusteredChangeListenerSupport( @Nonnull T observerdObject ) {
    this( observerdObject, null );
  }

  /**
   * <p>Constructor for ClusteredChangeListenerSupport.</p>
   *
   * @param observerdObject a T object.
   * @param contextProvider a {@link ClusteredChangeListenerSupport.ContextProvider} object.
   */
  public ClusteredChangeListenerSupport( @Nonnull T observerdObject, @Nullable ContextProvider contextProvider ) {
    this.contextProvider = contextProvider;
    transientSupport = new ChangeListenerSupport<T>( observerdObject );
    nonTransientSupport = new TransientChangeListenerSupport<T>( observerdObject );
  }

  /**
   * <p>addChangeListener</p>
   *
   * @param listener    a {@link com.cedarsoft.objectaccess.ChangeListener} object.
   * @param isTransient a boolean.
   */
  public void addChangeListener( @Nonnull ChangeListener<T> listener, boolean isTransient ) {
    if ( isTransient ) {
      transientSupport.addChangeListener( listener );
    } else {
      nonTransientSupport.addChangeListener( listener );
    }
  }

  /**
   * <p>removeChangeListener</p>
   *
   * @param listener a {@link com.cedarsoft.objectaccess.ChangeListener} object.
   */
  public void removeChangeListener( @Nonnull ChangeListener<T> listener ) {
    transientSupport.removeChangeListener( listener );
    nonTransientSupport.removeChangeListener( listener );
  }

  /**
   * Uses the set context provider to recieve the context
   *
   * @param propertiesPath the properties path
   */
  public void changed( @Nonnull String... propertiesPath ) {
    ContextProvider provider = getContextProvider();
    if ( provider != null ) {
      changed( provider.getContext(), propertiesPath );
    } else {
      changed( null, propertiesPath );
    }
  }

  /**
   * <p>changed</p>
   *
   * @param context        a {@link Object} object.
   * @param propertiesPath a {@link String} object.
   */
  public void changed( @Nullable Object context, @Nonnull String... propertiesPath ) {
    if ( propertiesPath.length == 0 ) {
      throw new IllegalArgumentException( "Empty properties path" );
    }

    transientSupport.changed( context, propertiesPath );
    nonTransientSupport.changed( context, propertiesPath );
  }

  /**
   * <p>Getter for the field <code>contextProvider</code>.</p>
   *
   * @return a {@link ClusteredChangeListenerSupport.ContextProvider} object.
   */
  @Nullable
  public ContextProvider getContextProvider() {
    return contextProvider;
  }

  /**
   * <p>Setter for the field <code>contextProvider</code>.</p>
   *
   * @param contextProvider a {@link ClusteredChangeListenerSupport.ContextProvider} object.
   */
  public void setContextProvider( @Nullable ContextProvider contextProvider ) {
    this.contextProvider = contextProvider;
  }

  /**
   * <p>createPropertyListenerDelegate</p>
   *
   * @param propertiesPath a {@link String} object.
   * @return a {@link PropertyChangeListener} object.
   */
  @Nonnull
  public PropertyChangeListener createPropertyListenerDelegate( @Nonnull String... propertiesPath ) {
    final String[] actual = new String[propertiesPath.length + 1];
    System.arraycopy( propertiesPath, 0, actual, 0, propertiesPath.length );

    return new PropertyChangeListener() {
      @Override
      public void propertyChange( PropertyChangeEvent evt ) {
        actual[actual.length - 1] = evt.getPropertyName();
        changed( evt, actual );
      }
    };
  }

  /**
   * Provides the context
   */
  public interface ContextProvider {
    @Nullable
    Object getContext();
  }

}
