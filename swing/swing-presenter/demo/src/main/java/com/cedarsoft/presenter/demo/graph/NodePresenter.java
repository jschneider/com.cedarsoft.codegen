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
import com.cedarsoft.lookup.LookupChangeEvent;
import com.cedarsoft.lookup.LookupChangeListener;
import com.cedarsoft.presenter.AbstractPresenter;

import javax.annotation.Nonnull;
import y.base.Node;
import y.view.Graph2D;

import javax.swing.Action;

/**
 *
 */
public class NodePresenter extends AbstractPresenter<Node> {
  @Override
  protected void bind( @Nonnull final Node presentation, @Nonnull final StructPart struct, @Nonnull Lookup lookup ) {
    final Graph2D graph2D = getGraph( presentation );

    lookup.bind( Action.class, new LookupChangeListener<Action>() {
      @Override
      public void lookupChanged( @Nonnull LookupChangeEvent<? extends Action> event ) {
        Action newValue = event.getNewValue();
        if ( newValue != null ) {
          graph2D.getRealizer( presentation ).setLabelText( ( String ) newValue.getValue( Action.NAME ) );
        } else {
          graph2D.getRealizer( presentation ).setLabelText( struct.getName() );
        }
        graph2D.updateViews();
      }
    } );
  }

  @Override
  protected boolean addChildPresentation( @Nonnull Node presentation, @Nonnull StructPart child, int index ) {
    Graph2D graph2D = getGraph( presentation );
    Node childNode = getChildPresenter( child ).present( child );
    graph2D.createEdge( presentation, childNode );
    Graph2DPresenter.update( getGraph() );
    return true;
  }

  @Override
  protected boolean shallAddChildren() {
    return true;
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
  protected void removeChildPresentation( @Nonnull Node presentation, @Nonnull StructPart child, int index ) {
    //implement later
  }

  @Override
  @Nonnull
  protected Node createPresentation() {
    return getGraph().createNode();
  }

  @Nonnull
  protected Graph2D getGraph( Node presentation ) {
    Graph2D graph = ( Graph2D ) presentation.getGraph();
    Graph2DPresenter.graphLocal.set( graph );
    return graph;
  }


  @Nonnull
  protected Graph2D getGraph() {
    return Graph2DPresenter.graphLocal.get();
  }
}
