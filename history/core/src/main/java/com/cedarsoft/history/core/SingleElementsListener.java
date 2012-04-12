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

package com.cedarsoft.history.core;

import javax.annotation.Nonnull;

/**
 * Special element implementation that delegates the calls of {@link ElementsListener}
 * to methods with only *one* element as argument.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 * @param <E> the type of the elements
 */
public abstract class SingleElementsListener<E> implements ElementsListener<E> {
  /**
   * Wraps the given element listener and creates an ElementsListener
   *
   * @param elementListener the wrapped element listener
   * @param <E>             the type
   * @return an ElementsListener that delegates to the given ElementListener
   */
  @Nonnull
  public static <E> ElementsListener<E> wrap( @Nonnull final ElementListener<E> elementListener ) {
    return new SingleElementsListener<E>() {
      @Override
      public void elementDeleted( @Nonnull ObservableCollection<? extends E> source, @Nonnull E element, int index ) {
        elementListener.elementDeleted( element );
      }

      @Override
      public void elementAdded( @Nonnull ObservableCollection<? extends E> source, @Nonnull E element, int index ) {
        elementListener.elementAdded( element );
      }

      @Override
      public void elementChanged( @Nonnull ObservableCollection<? extends E> source, @Nonnull E element, int index ) {
        elementListener.elementChanged( element );
      }
    };
  }

  /**
   * Is called when an entry has been deleted
   *
   * @param source  a {@link ObservableCollection} object.
   * @param element the entry that has been deleted
   * @param index   a int.
   */
  public abstract void elementDeleted( @Nonnull ObservableCollection<? extends E> source, @Nonnull E element, int index );

  /**
   * Is called when an entry has been added
   *
   * @param source  a {@link ObservableCollection} object.
   * @param element the entry that has been added
   * @param index   a int.
   */
  public abstract void elementAdded( @Nonnull ObservableCollection<? extends E> source, @Nonnull E element, int index );

  /**
   * Is called when an enty has been changed
   *
   * @param source  a {@link ObservableCollection} object.
   * @param element the entry that has been changed
   * @param index   a int.
   */
  public abstract void elementChanged( @Nonnull ObservableCollection<? extends E> source, @Nonnull E element, int index );


  /**
   * {@inheritDoc}
   */
  @Override
  public void elementsDeleted( @Nonnull ElementsChangedEvent<? extends E> event ) {
    for ( int i = 0; i < event.getElements().size(); i++ ) {
      E e = event.getElements().get( i );
      int index = event.getIndicies().get( i );
      elementDeleted( event.getSource(), e, index );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void elementsAdded( @Nonnull ElementsChangedEvent<? extends E> event ) {
    for ( int i = 0; i < event.getElements().size(); i++ ) {
      E e = event.getElements().get( i );
      int index = event.getIndicies().get( i );
      elementAdded( event.getSource(), e, index );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void elementsChanged( @Nonnull ElementsChangedEvent<? extends E> event ) {
    for ( int i = 0; i < event.getElements().size(); i++ ) {
      E e = event.getElements().get( i );
      int index = event.getIndicies().get( i );
      elementChanged( event.getSource(), e, index );
    }
  }
}
