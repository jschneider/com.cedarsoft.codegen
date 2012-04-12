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

import java.text.MessageFormat;

/**
 * A console printer that is ansi aware
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class AnsiAwareConsolePrinter implements ConsolePrinter {
  private static final char ANSI_ESCAPE = ( char ) 27;
  @Nonnull
  private static final String ANSI_BLUE = ANSI_ESCAPE + "[1;34m";
  @Nonnull
  private static final String ANSI_RED = ANSI_ESCAPE + "[1;31m";
  @Nonnull
  private static final String ANSI_GREEN = ANSI_ESCAPE + "[1;32m";
  @Nonnull
  private static final String ANSI_DEFAULT = ANSI_ESCAPE + "[0m";

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public String createError( @Nonnull String message, @Nonnull Object... objects ) {
    return ANSI_RED + MessageFormat.format( message, objects ) + ANSI_DEFAULT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public String createWarning( @Nonnull String message, @Nonnull Object... objects ) {
    return ANSI_BLUE + MessageFormat.format( message, objects ) + ANSI_DEFAULT;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public String createSuccess( @Nonnull String message, @Nonnull Object... objects ) {
    return ANSI_GREEN + MessageFormat.format( message, objects ) + ANSI_DEFAULT;
  }
}
