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
import java.lang.ref.WeakReference;

/**
 * This listener can be used to automatically register a lookup change listener to a lookup.
 * The listener is automatically unregistered whenever the delegating listener
 * has been garbage collected.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class WeakLookupChangeListener<T> implements LookupChangeListener<T> {
  private WeakReference<LookupChangeListener<? super T>> listenerReference;
  private Class<T> typeClass;

  /**
   * Creates a new instance with {@link #getTypeClass()} set to null
   *
   * @param listener the listener that is wrapped
   */
  public WeakLookupChangeListener( @Nonnull LookupChangeListener<T> listener ) {
    this( null, listener );
  }

  /**
   * Creates a new instance
   *
   * @param typeClass the type
   * @param listener  the listener this delegates to
   */
  public WeakLookupChangeListener( @Nullable Class<T> typeClass, @Nonnull LookupChangeListener<? super T> listener ) {
    this.typeClass = typeClass;
    this.listenerReference = new WeakReference<LookupChangeListener<? super T>>( listener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void lookupChanged( @Nonnull LookupChangeEvent<? extends T> event ) {
    LookupChangeListener<? super T> delegatingListener = getWrappedListener();
    if ( delegatingListener == null ) {
      removeListener( event.getSource() );
    } else {
      delegatingListener.lookupChanged( event );
    }
  }

  /**
   * Returns the wrapped lookup (or null if the reference has been lost
   *
   * @return the wrapped lookup  (or null if the reference has been lost
   */
  @Nullable
  protected LookupChangeListener<? super T> getWrappedListener() {
    return this.listenerReference.get();
  }

  /**
   * <p>Getter for the field <code>typeClass</code>.</p>
   *
   * @return a {@link Class} object.
   */
  @Nonnull
  public Class<T> getTypeClass() {
    return typeClass;
  }

  private void removeListener( @Nonnull Lookup source ) {
    if ( typeClass != null ) {
      source.removeChangeListener( typeClass, this );
    }
    source.removeChangeListener( this );
  }


  /**
   * Wraps a given lookup change listener with a weak listener
   *
   * @param listener the listener that is wrapped
   * @param <T>      a T object.
   * @return the weak lookup change listener that wraps the given listener
   */
  public static <T> WeakLookupChangeListener<T> wrap( @Nonnull LookupChangeListener<T> listener ) {
    return wrap( null, listener );
  }

  /**
   * Wraps a given lookup change listener with a weak listener
   *
   * @param type     the type the listener is resgistered for
   * @param listener the listener that is wrapped
   * @param <T>      a T object.
   * @return the weak lookup change listener that wraps the given listener
   */
  @Nonnull
  public static <T> WeakLookupChangeListener<T> wrap( @Nullable Class<T> type, @Nonnull LookupChangeListener<? super T> listener ) {
    return new WeakLookupChangeListener<T>( type, listener );
  }

}
