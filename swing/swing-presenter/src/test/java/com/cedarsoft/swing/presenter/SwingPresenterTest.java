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

import com.cedarsoft.commons.struct.DefaultNode;
import com.cedarsoft.commons.struct.Node;
import com.cedarsoft.commons.struct.StructPart;
import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.lookup.Lookups;
import javax.annotation.Nonnull;

import com.cedarsoft.presenter.Presenter;
import org.junit.*;

import javax.swing.JComponent;
import javax.swing.JPanel;
import java.awt.Component;

import static org.junit.Assert.*;

/**
 *
 */
public class SwingPresenterTest {
  @Test
  public void testNormalAd() {
    Node root = new DefaultNode( "root" );
    root.addChild( new DefaultNode( "0", Lookups.dynamicLookup( new MySwingPresenter( true ) ) ) );
    root.addChild( new DefaultNode( "1", Lookups.dynamicLookup( new MySwingPresenter( true ) ) ) );
    root.addChild( new DefaultNode( "2", Lookups.dynamicLookup( new MySwingPresenter( true ) ) ) );

    JPanel panel = new MySwingPresenter( true ).present( root );
    assertEquals( 3, panel.getComponentCount() );

    Component child0 = panel.getComponent( 0 );
    Component child1 = panel.getComponent( 1 );
    Component child2 = panel.getComponent( 2 );

    root.addChild( 2, new DefaultNode( "1.5", Lookups.dynamicLookup( new MySwingPresenter( true ) ) ) );

    assertEquals( 4, panel.getComponentCount() );
    assertSame( child0, panel.getComponent( 0 ) );
    assertSame( child1, panel.getComponent( 1 ) );
    assertSame( child2, panel.getComponent( 3 ) );
  }

  @Test
  public void testBaseic() {
    assertTrue( new MySwingPresenter( true ).createPresentation().isEnabled() );
    assertFalse( new MySwingPresenter( false ).createPresentation().isEnabled() );
  }

  private static class MySwingPresenter extends SwingPresenter<JPanel> {
    final boolean enablePanel;

    private MySwingPresenter( boolean enablePanel ) {
      this.enablePanel = enablePanel;
    }

    @Override
    @Nonnull
    protected Presenter<? extends JComponent> getChildPresenter( @Nonnull StructPart child ) {
      return child.getLookup().lookup( SwingPresenter.class );
    }

    @Override
    protected void bind( @Nonnull JPanel presentation, @Nonnull StructPart struct, @Nonnull Lookup lookup ) {
    }

    @Override
    @Nonnull
    protected JPanel createPresentation() {
      JPanel panel = new JPanel();
      panel.setEnabled( enablePanel );
      return panel;
    }

    @Override
    protected boolean shallAddChildren() {
      return true;
    }
  }
}
