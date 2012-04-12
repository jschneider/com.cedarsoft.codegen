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

package com.cedarsoft.swing.presenter.demo.graph;

import com.cedarsoft.commons.struct.StructPart;
import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.presenter.AbstractPresenter;

import javax.annotation.Nonnull;
import y.base.Node;
import y.layout.tree.TreeLayouter;
import y.view.Graph2D;

/**
 *
 */
public class Graph2DPresenter extends AbstractPresenter<Graph2D> {
  public static final ThreadLocal<Graph2D> graphLocal = new ThreadLocal<Graph2D>();

  @Nonnull
  private static final TreeLayouter treeLayouter = new TreeLayouter();

  public static void update( @Nonnull Graph2D presentation ) {
    presentation.updateViews();
    treeLayouter.doLayout( presentation );
    System.out.println( "Updating..." );

  }

  private Node rootNode;

  @Override
  protected void bind( @Nonnull Graph2D presentation, @Nonnull StructPart struct, @Nonnull Lookup lookup ) {
    presentation.setLabelText( rootNode, struct.getName() );
  }

  @Override
  protected boolean addChildPresentation( @Nonnull Graph2D presentation, @Nonnull StructPart child, int index ) {
    Node childNode = getChildPresenter( child ).present( child );
    getGraph().createEdge( rootNode, childNode );
    update( presentation );
    return true;
  }

  @Nonnull
  protected Graph2D getGraph() {
    return graphLocal.get();
  }

  @Nonnull
  protected NodePresenter getChildPresenter( @Nonnull StructPart child ) {
    NodePresenter presenter = child.getLookup().lookup( NodePresenter.class );
    if ( presenter != null ) {
      return presenter;
    }

    return new NodePresenter();
  }

  @Override
  protected boolean shallAddChildren() {
    return true;
  }

  @Override
  protected void removeChildPresentation( @Nonnull Graph2D presentation, @Nonnull StructPart child, int index ) {
    Node node = presentation.getNodeArray()[index];
    presentation.removeNode( node );
    update( presentation );
  }

  @Override
  @Nonnull
  protected Graph2D createPresentation() {
    graphLocal.set( new Graph2D() );
    rootNode = getGraph().createNode();
    return getGraph();
  }


}
