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

import com.cedarsoft.commons.struct.DepthFirstStructureTreeWalker;
import com.cedarsoft.commons.struct.StructPart;
import com.cedarsoft.commons.struct.StructureTreeWalker;

import javax.annotation.Nonnull;

import java.lang.String;

/**
 * <p>StructStringPresenter class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class StructStringPresenter implements Presenter<String> {
  @Nonnull
  private final String intendSequence;
  private static final char NEWLINE_CHAR = '\n';

  /**
   * <p>Constructor for StructStringPresenter.</p>
   */
  public StructStringPresenter() {
    this( "  " );
  }

  /**
   * <p>Constructor for StructStringPresenter.</p>
   *
   * @param intendSequence a {@link String} object.
   */
  public StructStringPresenter( @Nonnull String intendSequence ) {
    this.intendSequence = intendSequence;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public String present( @Nonnull StructPart struct ) {
    final StringBuilder builder = new StringBuilder();

    StructureTreeWalker treeWalker = new DepthFirstStructureTreeWalker();
    treeWalker.walk( struct, new StructureTreeWalker.WalkerCallBack() {
      @Override
      public void nodeReached( @Nonnull StructPart node, int level ) {
        for ( int i = 0; i < level; i++ ) {
          builder.append( intendSequence );
        }
        builder.append( node.getName() );
        builder.append( NEWLINE_CHAR );
      }
    } );

    return builder.toString();
  }
}
