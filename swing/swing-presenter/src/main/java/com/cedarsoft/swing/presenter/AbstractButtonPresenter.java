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

import javax.annotation.Nonnull;

import javax.swing.AbstractButton;
import javax.swing.Action;
import javax.swing.JComponent;

/**
 * Abstract base class for presenters that create AbstractButtons that are associated with Actions.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public abstract class AbstractButtonPresenter<T extends AbstractButton> extends SwingPresenter<T> {
  /**
   * Constant <code>PROPERTY_ACTION="action"</code>
   */
  @Nonnull
  public static final String PROPERTY_ACTION = "action";

  /**
   * Constant <code>KEY_ACTION_LISTENER</code>
   */
  @Nonnull
  public static final Object KEY_ACTION_LISTENER = "actionListener";

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bind( @Nonnull T presentation, @Nonnull StructPart struct, @Nonnull Lookup lookup ) {
    Action action = lookup.lookup( Action.class );
    if ( action == null ) {
      throw new IllegalStateException( "Can not create button: No Action found" );
    }
    PropertyCallback<Action> callback = new PropertyCallback<Action>( presentation, PROPERTY_ACTION, Action.class );
    presentation.putClientProperty( KEY_ACTION_LISTENER, callback );//Ensure the weak instance is not lost
    lookup.bindWeak( callback );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  protected Presenter<? extends JComponent> getChildPresenter( @Nonnull StructPart child ) {
    throw new UnsupportedOperationException();
  }
}
