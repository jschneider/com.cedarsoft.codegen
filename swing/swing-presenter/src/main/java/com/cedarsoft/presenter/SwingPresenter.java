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
import org.jetbrains.annotations.NotNull;

import javax.swing.JComponent;

/**
 * Base class for swing presenters.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public abstract class SwingPresenter<T extends JComponent> extends AbstractPresenter<T> {
  /** {@inheritDoc} */
  @Override
  protected void removeChildPresentation( @NotNull T presentation, @NotNull StructPart child, int index ) {
    presentation.remove( index );
    presentation.validate();
  }

  /** {@inheritDoc} */
  @Override
  protected boolean addChildPresentation( @NotNull T presentation, @NotNull StructPart child, int index ) {
    Presenter<? extends JComponent> childPresenter = getChildPresenter( child );
    return addChild( presentation, index, childPresenter.present( child ) );
  }

  /**
   * <p>addChild</p>
   *
   * @param presentation a T object.
   * @param index a int.
   * @param childPresentation a {@link javax.swing.JComponent} object.
   * @return a boolean.
   */
  protected boolean addChild( @NotNull T presentation, int index, @NotNull JComponent childPresentation ) {
    presentation.add( childPresentation, index );
    presentation.validate();
    return true;
  }

  /**
   * <p>getChildPresenter</p>
   *
   * @param child a {@link com.cedarsoft.commons.struct.StructPart} object.
   * @return a {@link com.cedarsoft.presenter.Presenter} object.
   */
  @NotNull
  protected abstract Presenter<? extends JComponent> getChildPresenter( @NotNull StructPart child );
}
