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

import com.cedarsoft.renderer.Renderer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.IOException;
import java.util.List;

/**
 * Provides access to the command line
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public interface CmdLine {
  /**
   * Reads a boolean from the command line
   *
   * @param message the message that is shown
   * @return the value the user has entered
   *
   * @throws IOException if any.
   */
  boolean readBoolean( @Nonnull String message ) throws IOException;

  /**
   * Prints an error on the console
   *
   * @param message the error message
   * @param objects the objects
   */
  void error( @Nonnull String message, @Nonnull Object... objects );

  /**
   * Prints a warning
   *
   * @param message the message
   * @param objects the objects
   */
  void warning( @Nonnull String message, @Nonnull Object... objects );

  /**
   * Prints a success message
   *
   * @param message the message
   * @param objects the objects
   */
  void success( @Nonnull String message, @Nonnull Object... objects );

  /**
   * Reads a string form the console
   *
   * @param message the mssage
   * @return the string
   */
  @Nonnull
  String read( @Nonnull String message );

  /**
   * Reads a string from the console. The user may optinally select the given default value
   *
   * @param message      the message
   * @param defaultValue the default value
   * @return the string
   */
  @Nonnull
  String read( @Nonnull String message, @Nullable String defaultValue );

  /**
   * Reads a string
   *
   * @param message  the message
   * @param elements the elements that may be selected
   * @return the entered value (a free value or one of the elements)
   */
  @Nonnull
  String read( @Nonnull String message, @Nonnull List<String> elements );

  /**
   * Reads an int from the console
   *
   * @param message the message
   * @param lower   the lower bounds
   * @param upper   the upper bounds
   * @return the read int
   */
  int readInt( @Nonnull String message, int lower, int upper );

  /**
   * Reads an int from the console
   *
   * @param message the message
   * @return the int
   *
   * @throws IOException if any.
   */
  int readInt( @Nonnull String message ) throws IOException;

  /**
   * Select an element from the list
   *
   * @param message   the message
   * @param elements  the elements that may be choosen @return the selected element
   * @param presenter an optional presenter that creates a string representation for the elements
   * @param <T>       a T object.
   * @return the selected element
   */
  @Nonnull
  <T> T readSelection( @Nonnull String message, @Nonnull List<? extends T> elements, @Nullable Renderer<? super T, Object> presenter );

  /**
   * Selects an element
   *
   * @param message  the message
   * @param elements the elements
   * @param <T>      a T object.
   * @return the selected element
   */
  @Nonnull
  <T> T readSelection( @Nonnull String message, @Nonnull List<? extends T> elements );

  /**
   * Pauses the script
   *
   * @param seconds the seconds that it is paused
   */
  void pause( int seconds );

  /**
   * Prints out a new line
   */
  void outNl();

  /**
   * Prints a message
   *
   * @param message the message
   * @param objects the objects
   */
  void out( @Nonnull String message, @Nonnull Object... objects );

  /**
   * Redirect the output of the given process
   *
   * @param process the process the output for is redirected
   */
  void out( @Nonnull Process process );

  /**
   * Reads a string from the console
   *
   * @param message       the message
   * @param elements      the elements (the user may select one of them)
   * @param presenter     the presenter
   * @param objectFactory a {@link ObjectFactory} object.
   * @param <T>           a T object.
   * @return the string that has been entered manually or the object that has been selected (String or T)
   */
  @Nonnull
  <T> T read( @Nonnull String message, @Nonnull List<? extends T> elements, @Nullable Renderer<T, Object> presenter, @Nonnull ObjectFactory<T> objectFactory );

  /**
   * Reads a string from the console
   *
   * @param message     the message
   * @param elements    the elements
   * @param preselected the preselected string
   * @return the read string from the console (one of the elements, the preselected value or a newly entered value)
   */
  @Nonnull
  String read( @Nonnull String message, @Nonnull List<String> elements, @Nonnull String preselected );
}
