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
import com.cedarsoft.presenter.Presenter;

import javax.annotation.Nonnull;

import javax.swing.Action;
import javax.swing.JComponent;
import javax.swing.JMenu;

/**
 * Creates a JMenu.
 * Looks for {@link JMenuItemPresenter} for each of its children.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class DefaultJMenuPresenter extends AbstractButtonPresenter<JMenu> implements JMenuPresenter {
  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public JMenu createPresentation() {
    return new JMenu();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  protected Presenter<? extends JComponent> getChildPresenter( @Nonnull StructPart child ) {
    JMenuItemPresenter<?> menuItemPresenter = child.getLookup().lookup( JMenuItemPresenter.class );
    if ( menuItemPresenter != null ) {
      return menuItemPresenter;
    }

    //If the child contains children --> submenu
    if ( !child.getChildren().isEmpty() ) {
      return new DefaultJMenuPresenter();
    }

    //If the child contains an action
    if ( child.getLookup().lookup( Action.class ) != null ) {
      return new DefaultJMenuItemPresenter();
    }

    //If the child contains a component, just add that component
    if ( child.getLookup().lookup( JComponent.class ) != null ) {
      return new JComponentPresenter();
    }

    throw new IllegalStateException( "No suiteable child presenter found" );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  protected boolean shallAddChildren() {
    return true;
  }
}
