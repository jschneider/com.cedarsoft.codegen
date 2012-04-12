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

package com.cedarsoft.cmd;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.IOException;
import java.io.PrintStream;

/**
 * <p>DummyCmdLine class.</p>
 *
 * @author Johannes Schneider
 */
public class DummyCmdLine extends AbstractCmdLine {
  private PrintStream out;

  /**
   * <p>Constructor for DummyCmdLine.</p>
   */
  public DummyCmdLine() {
    this( System.out );
  }

  /**
   * <p>Constructor for DummyCmdLine.</p>
   *
   * @param out a {@link PrintStream} object.
   */
  public DummyCmdLine( @Nonnull PrintStream out ) {
    this.out = out;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean readBoolean( @Nonnull String message ) throws IOException {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public String read( @Nonnull String message ) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public String read( @Nonnull String message, @Nullable String defaultValue ) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int readInt( @Nonnull String message, int lower, int upper ) {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int readInt( @Nonnull String message ) throws IOException {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void pause( int seconds ) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public PrintStream getOut() {
    return out;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  protected ConsolePrinter getConsolePrinter() {
    return new DefaultConsolePrinter();
  }
}
