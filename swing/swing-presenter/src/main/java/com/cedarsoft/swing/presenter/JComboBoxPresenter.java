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

package com.cedarsoft.swing.presenter;

import com.cedarsoft.commons.struct.StructPart;
import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.lookup.binding.PropertyCallback;
import com.cedarsoft.presenter.Presenter;
import com.cedarsoft.swing.presenter.model.StructBasedComboBoxModel;

import javax.annotation.Nonnull;

import javax.swing.Action;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import java.awt.Component;

/**
 * Creates a combo box
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class JComboBoxPresenter extends SwingPresenter<JComboBox> {
  /**
   * Constant <code>PROPERTY_ACTION="action"</code>
   */
  @Nonnull
  public static final String PROPERTY_ACTION = "action";
  /**
   * Constant <code>PROPERTY_RENDERER="renderer"</code>
   */
  @Nonnull
  public static final String PROPERTY_RENDERER = "renderer";

  @Nonnull
  private static final Object KEY_ACTION_CALLBACK = JComboBoxPresenter.class.getName() + "###ActionCallback";

  @Nonnull
  private static final Object KEY_RENDERER_CALLBACK = JComboBoxPresenter.class.getName() + "###RendererCallback";


  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  protected Presenter<? extends JComponent> getChildPresenter( @Nonnull StructPart child ) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean shallAddChildren() {
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bind( @Nonnull JComboBox presentation, @Nonnull StructPart struct, @Nonnull Lookup lookup ) {
    {
      PropertyCallback<Action> callback = new PropertyCallback<Action>( presentation, PROPERTY_ACTION, Action.class );
      lookup.bindWeak( callback );
      presentation.putClientProperty( KEY_ACTION_CALLBACK, callback );
    }

    //sets a renderer
    {
      PropertyCallback<ListCellRenderer> callback = new PropertyCallback<ListCellRenderer>( presentation, PROPERTY_RENDERER, ListCellRenderer.class );
      lookup.bindWeak( callback );
      presentation.putClientProperty( KEY_RENDERER_CALLBACK, callback );
    }

    if ( lookup.lookup( ListCellRenderer.class ) == null ) {
      presentation.setRenderer( new StructDefaultCellRenderer() );
    }

    //set the model
    presentation.setModel( new StructBasedComboBoxModel( struct ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  protected JComboBox createPresentation() {
    return new JComboBox();
  }

  /**
   * Default list cell renderer that uses the name of the node as representation
   */
  public static class StructDefaultCellRenderer extends DefaultListCellRenderer {
    @Override
    public Component getListCellRendererComponent( JList list, Object value, int index, boolean isSelected, boolean cellHasFocus ) {
      super.getListCellRendererComponent( list, value, index, isSelected, cellHasFocus );
      if ( value instanceof StructPart ) {
        setText( ( ( StructPart ) value ).getName() );
      }
      return this;
    }
  }
}
