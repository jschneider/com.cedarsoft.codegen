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

package com.cedarsoft.inject;

import com.google.inject.AbstractModule;
import com.google.inject.ConfigurationException;
import com.google.inject.Key;
import org.junit.*;
import org.junit.rules.*;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class GuiceModulesHelperTest {
  @Test
  public void testFail() {
    try {
      GuiceModulesHelper.minimize( Arrays.asList( new Module1(), new Module2() ), List.class );
      fail( "Where is the Exception" );
    } catch ( ConfigurationException ignore ) {
    }
  }

  @Test
  public void testMinimization() {
    GuiceModulesHelper.Result result = GuiceModulesHelper.minimize( Arrays.asList( new Module1(), new Module2() ), Object.class );
    assertEquals( 2, result.getRemoved().size() );
    assertEquals( 0, result.getTypes().size() );
  }

  @Test
  public void testMinimization2() {
    GuiceModulesHelper.Result result = GuiceModulesHelper.minimize( Arrays.asList( new Module1(), new Module2() ), MyObject.class );
    assertEquals( 1, result.getRemoved().size() );
    assertEquals( 1, result.getTypes().size() );
  }

  @Test
  public void testMinimization3() {
    GuiceModulesHelper.Result result = new GuiceModulesHelper.Result( Arrays.asList( new Module1(), new Module2() ) );
    assertSame( result, GuiceModulesHelper.minimize( result, MyObject.class ) );
    assertEquals( 1, result.getRemoved().size() );
    assertEquals( 1, result.getTypes().size() );
  }

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @Test
  public void testAssertMini() throws Exception {
    expectedException.expect( AssertionError.class );
    GuiceModulesHelper.assertMinimizeNotPossible( Arrays.asList( new Module1(), new Module2() ), MyObject.class );
  }

  @Test
  public void testAssertMini4() throws Exception {
    expectedException.expect( AssertionError.class );
    GuiceModulesHelper.assertMinimizeNotPossible( Arrays.asList( new Module2() ), Object.class );
  }

  @Test
  public void testAssertMini2() throws Exception {
    GuiceModulesHelper.assertMinimizeNotPossible( Arrays.asList( new Module1() ), MyObject.class, Object.class );
  }

  @Test
  public void testAssertMini3() throws Exception {
    //    GuiceModulesHelper.assertMinimizeNotPossible( Arrays.asList( new Module1() ), MyObject.class, Object.class );
    GuiceModulesHelper.assertMinimizeNotPossible( Arrays.asList( new Module1() ), MyObject.class );
    GuiceModulesHelper.assertMinimizeNotPossible( Arrays.asList( new Module1() ), Object.class, MyObject.class );
    GuiceModulesHelper.assertMinimizeNotPossible( Arrays.asList( new Module1() ), new Key<MyObject>() {
    }, new Key<Object>() {
    } );
    GuiceModulesHelper.assertMinimizeNotPossible( Arrays.asList( new Module1() ), new Key<Object>() {
    }, new Key<MyObject>() {
    }, new Key<Object>() {
    } );
  }

  public static class Module1 extends AbstractModule {
    @Override
    protected void configure() {
      bind( MyObject.class ).toInstance( new MyObject( "theId" ) );
    }
  }

  public static class Module2 extends AbstractModule {
    @Override
    protected void configure() {
    }
  }

  public static class MyObject {
    private final String id;

    public MyObject( String id ) {
      this.id = id;
    }
  }
}
