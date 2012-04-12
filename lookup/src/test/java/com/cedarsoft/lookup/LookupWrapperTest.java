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

package com.cedarsoft.lookup;

import org.junit.*;

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: 09.10.2006<br>
 * Time: 09:00:49<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class LookupWrapperTest {
  private MappedLookup lookup;

  @Before
  public void setUp() throws Exception {
    lookup = new MappedLookup();
    lookup.store( String.class, "asdf" );
  }

  @Test
  public void testWrapping() {
    LookupChangeListenerMock listenerMock = new LookupChangeListenerMock();

    LookupWrapper lookupWrapper = new LookupWrapper( lookup );
    assertEquals( "asdf", lookupWrapper.lookup( String.class ) );
    lookupWrapper.addChangeListener( listenerMock );

    listenerMock.addExpected( String.class, "asdf", "2" );
    lookup.store( String.class, "2" );
    listenerMock.verify();
    assertEquals( "2", lookup.lookup( String.class ) );
    assertEquals( "2", lookupWrapper.lookup( String.class ) );

    listenerMock.addExpected( String.class, "2", "3" );

    lookupWrapper.store( String.class, "3" );
    listenerMock.verify();

    assertEquals( "2", lookup.lookup( String.class ) );
    assertEquals( "3", lookupWrapper.lookup( String.class ) );
  }

}
