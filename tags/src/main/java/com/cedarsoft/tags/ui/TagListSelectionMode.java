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

package com.cedarsoft.tags.ui;

import com.cedarsoft.tags.Tag;
import com.cedarsoft.tags.TagChangeListener;
import com.cedarsoft.tags.TagChangeSupport;
import com.cedarsoft.tags.TagObservable;
import org.jetbrains.annotations.NotNull;

import javax.swing.DefaultListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>TagListSelectionMode class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class TagListSelectionMode extends DefaultListSelectionModel implements TagObservable {
  private final transient TagChangeSupport tagChangeSupport = new TagChangeSupport();
  private final transient TagListModel model;

  /**
   * <p>Constructor for TagListSelectionMode.</p>
   *
   * @param model a {@link TagListModel} object.
   */
  public TagListSelectionMode( @NotNull TagListModel model ) {
    this.model = model;
    addListSelectionListener( new ListSelectionListener() {
      @Override
      public void valueChanged( ListSelectionEvent e ) {
        if ( e.getValueIsAdjusting() ) {
          return;
        }
        tagChangeSupport.notifyTagChanged( new TagChangeListener.TagChangeEvent( TagListSelectionMode.this, TagChangeListener.TagEventType.UNKNOWN, null, -1 ) );
      }
    } );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addTagChangeListener( @NotNull TagChangeListener listener ) {
    tagChangeSupport.addTagChangeListener( listener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeTagChangeListener( @NotNull TagChangeListener listener ) {
    tagChangeSupport.removeTagChangeListener( listener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @NotNull
  public List<? extends Tag> getTags() {
    int min = getMinSelectionIndex();
    int max = getMaxSelectionIndex();

    if ( min < max ) {
      return Collections.emptyList();
    }

    List<Tag> tags = new ArrayList<Tag>();
    for ( int i = min; i <= max; i++ ) {
      if ( isSelectedIndex( i ) ) {
        tags.add( model.getElementAt( i ) );
      }
    }
    return Collections.unmodifiableList( tags );
  }
}
