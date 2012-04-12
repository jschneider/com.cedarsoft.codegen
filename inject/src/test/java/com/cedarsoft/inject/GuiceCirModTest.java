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
import com.google.inject.Guice;
import com.google.inject.Key;
import com.google.inject.Provides;
import org.junit.*;

import javax.inject.Inject;
import javax.inject.Qualifier;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

import static org.junit.Assert.*;

/**
 *
 */
public class GuiceCirModTest {
  @Test
  public void testIt() {
    MyObject myObject = Guice.createInjector( new Module1(), new Module2() ).getInstance( MyObject.class );
    assertEquals( "", Guice.createInjector( new Module1(), new Module2() ).getInstance( String.class ) );
    assertEquals( 7, myObject.id );

    GuiceModulesHelper.Result result = GuiceModulesHelper.minimize( Arrays.asList( new Module1(), new Module2() ), MyObject.class );
    assertEquals( 0, result.getRemoved().size() );
  }

  public static class Module1 extends AbstractModule {
    @Override
    protected void configure() {
    }

    @Provides
    public Integer providesInteger( @StringMarker String string ) {
      assertEquals( "magic", string );
      return 7;
    }
  }

  public static class Module2 extends AbstractModule {
    @Override
    protected void configure() {
      bind( String.class ).annotatedWith( StringMarker.class ).toInstance( "magic" );
    }
  }

  public static class MyObject {
    private final int id;

    @Inject
    public MyObject( Integer id ) {
      this.id = id;
    }
  }

  @Retention( RetentionPolicy.RUNTIME )
  @Target( {ElementType.FIELD, ElementType.PARAMETER} )
  @Documented
  @Qualifier
  public @interface StringMarker {

  }

}
