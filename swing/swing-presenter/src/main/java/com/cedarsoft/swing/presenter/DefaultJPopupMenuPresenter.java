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
import com.cedarsoft.presenter.Presenter;

import javax.annotation.Nonnull;

import javax.swing.JComponent;
import javax.swing.JPopupMenu;

/**
 * Presents a JPopupMenu
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class DefaultJPopupMenuPresenter extends SwingPresenter<JPopupMenu> implements JPopupMenuPresenter {
  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  protected JPopupMenu createPresentation() {
    return new JPopupMenu();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  protected Presenter<? extends JComponent> getChildPresenter( @Nonnull StructPart child ) {
    DefaultJMenuItemPresenter presenter = child.getLookup().lookup( DefaultJMenuItemPresenter.class );
    if ( presenter != null ) {
      return presenter;
    }

    DefaultJMenuItemPresenter menuItemPresenter = child.getLookup().lookup( DefaultJMenuItemPresenter.class );
    if ( menuItemPresenter != null ) {
      return menuItemPresenter;
    }

    if ( child.getChildren().isEmpty() ) {
      return new DefaultJMenuItemPresenter();
    } else {
      return new DefaultJMenuPresenter();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected void bind( @Nonnull JPopupMenu presentation, @Nonnull StructPart struct, @Nonnull Lookup lookup ) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean shallAddChildren() {
    return true;
  }
}
