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

package com.cedarsoft.presenter;

import com.cedarsoft.commons.struct.StructPart;
import com.cedarsoft.commons.struct.StructureChangedEvent;
import com.cedarsoft.commons.struct.StructureListener;
import com.cedarsoft.lookup.Lookup;
import javax.annotation.Nonnull;

import java.lang.ref.WeakReference;
import java.util.HashSet;
import java.util.Set;

/**
 * An abstract implementation of {@link Presenter}.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 * @param <T> the type of the presentation that is created
 */
public abstract class AbstractPresenter<T> implements Presenter<T> {
  /**
   * Holds the references of the structure listeners.
   */
  @SuppressWarnings( {"MismatchedQueryAndUpdateOfCollection"} )
  private Set<StructureListener> structureListeners = new HashSet<StructureListener>();

  /**
   * {@inheritDoc}
   * <p/>
   * Creates the presentation for the given struct. This method should not be overridden.
   */
  @Override
  @Nonnull
  public T present( @Nonnull StructPart struct ) {
    Lookup lookup = struct.getLookup();
    T presentation = createPresentation();

    //Only hold the presentation as weak reference
    final WeakReference<T> weakPresentationReference = new WeakReference<T>( presentation );

    bind( presentation, struct, lookup );

    if ( shallAddChildren() ) {
      int i = 0;
      for ( StructPart child : struct.getChildren() ) {
        if ( addChildPresentation( presentation, child, i ) ) {
          i++;
        }
      }

      StructureListener structureListener = new ChildStructureListener<T>( weakPresentationReference, this );
      structureListeners.add( structureListener );
      struct.addStructureListenerWeak( structureListener );
    }
    return presentation;
  }

  /**
   * Returns whether the children shall be added
   *
   * @return whether the children shall be added
   */
  protected abstract boolean shallAddChildren();

  /**
   * The default implementation returns {@link StructureChangedEvent#getIndex()}
   *
   * @param event the event
   * @return the index
   */
  protected int calculateIndex( @Nonnull StructureChangedEvent event ) {
    return event.getIndex();
  }

  /**
   * Bind the presentation
   *
   * @param presentation the presentation
   * @param struct       the struct
   * @param lookup       a {@link Lookup} object.
   */
  protected abstract void bind( @Nonnull T presentation, @Nonnull StructPart struct, @Nonnull Lookup lookup );

  /**
   * Adds the child presentation to the parent presentation (if possible).
   *
   * @param presentation the presentation (the parent)
   * @param child        the child struct
   * @param index        the index
   * @return whether the child has been added or not.
   */
  protected abstract boolean addChildPresentation( @Nonnull T presentation, @Nonnull StructPart child, int index );

  /**
   * Removes the child presentation
   *
   * @param presentation the presentation
   * @param child        the child that has been removed
   * @param index        the index
   */
  protected abstract void removeChildPresentation( @Nonnull T presentation, @Nonnull StructPart child, int index );

  /**
   * Create the basic presentation.
   * Do not wire anything up here. Use the {@link #bind(Object, StructPart, Lookup)}
   * method instead.
   *
   * @return the presentation
   */
  @Nonnull
  protected abstract T createPresentation();

  /**
   * Adds/removes the child presentations if children are added/detached
   */
  private static class ChildStructureListener<T> implements StructureListener {
    @Nonnull
    private final WeakReference<T> weakPresentationReference;
    @Nonnull
    private final AbstractPresenter<T> presenter;

    private ChildStructureListener( @Nonnull WeakReference<T> weakPresentationReference, @Nonnull AbstractPresenter<T> presenter ) {
      this.weakPresentationReference = weakPresentationReference;
      this.presenter = presenter;
    }

    @Override
    public void childAdded( @Nonnull StructureChangedEvent event ) {
      StructPart child = event.getStructPart();
      T presentation = weakPresentationReference.get();
      if ( presentation != null ) {
        presenter.addChildPresentation( presentation, child, presenter.calculateIndex( event ) );
      }
    }

    @Override
    public void childDetached( @Nonnull StructureChangedEvent event ) {
      StructPart child = event.getStructPart();
      T presentation = weakPresentationReference.get();
      if ( presentation != null ) {
        presenter.removeChildPresentation( presentation, child, event.getIndex() );
      }
    }
  }
}
