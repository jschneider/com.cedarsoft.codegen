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

package com.cedarsoft.test.utils;


import javax.annotation.Nonnull;
import org.junit.rules.*;
import org.junit.runners.model.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

/**
 *
 */
public class SystemOutRule implements MethodRule {
  @Nonnull
  private final ByteArrayOutputStream newOut = new ByteArrayOutputStream();
  private PrintStream oldOut;

  @Nonnull
  private final ByteArrayOutputStream newErr = new ByteArrayOutputStream();
  private PrintStream oldErr;

  @Override
  public Statement apply( final Statement base, FrameworkMethod method, Object target ) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        before();
        try {
          base.evaluate();
        } finally {
          after();
        }
      }
    };
  }

  protected void before() {
    if ( oldOut != null ) {
      throw new IllegalStateException( "oldOut is not null: " + oldOut );
    }
    oldOut = System.out;
    System.setOut( new PrintStream( newOut ) );

    if ( oldErr != null ) {
      throw new IllegalStateException( "oldErr is not null: " + oldErr );
    }
    oldErr = System.err;
    System.setErr( new PrintStream( newErr ) );
  }

  protected void after() {
    System.setOut( oldOut );
    oldOut = null;
  }

  @Nonnull
  public String getOutAsString() {
    return newOut.toString();
  }

  @Nonnull
  public String getErrAsString() {
    return newErr.toString();
  }

  @Nonnull
  public PrintStream getOldOut() {
    if ( oldOut == null ) {
      throw new IllegalStateException( "old out is null. Rule not activated" );
    }
    return oldOut;
  }

  @Nonnull
  public PrintStream getOldErr() {
    if ( oldErr == null ) {
      throw new IllegalStateException( "oldErr is null. Rule not activated" );
    }
    return oldErr;
  }
}
