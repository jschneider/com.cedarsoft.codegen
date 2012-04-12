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

package com.cedarsoft.workflow;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Defines a transition between two states.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 * @param <T> the type of object that is used within the workflow
 */
public class TransitionDefinition<T> {
  @Nonnull
  private final State<T> source;
  @Nonnull
  private final State<T> target;
  @Nonnull
  private final List<Action<T>> actions = new ArrayList<Action<T>>();

  /**
   * <p>Constructor for TransitionDefinition.</p>
   *
   * @param source  a {@link State} object.
   * @param target  a {@link State} object.
   * @param actions a {@link Action} object.
   */
  public TransitionDefinition( @Nonnull State<T> source, @Nonnull State<T> target, @Nonnull Action<T>... actions ) {
    this( source, target, Arrays.asList( actions ) );
  }

  /**
   * Creates a new transition definition between the given source and target
   *
   * @param source  the source
   * @param target  the target
   * @param actions a {@link List} object.
   */
  public TransitionDefinition( @Nonnull State<T> source, @Nonnull State<T> target, @Nonnull List<? extends Action<T>> actions ) {
    this.source = source;
    this.target = target;
    this.actions.addAll( actions );
  }

  /**
   * <p>Getter for the field <code>source</code>.</p>
   *
   * @return a {@link State} object.
   */
  @Nonnull
  public State<T> getSource() {
    return source;
  }

  /**
   * <p>Getter for the field <code>target</code>.</p>
   *
   * @return a {@link State} object.
   */
  @Nonnull
  public State<T> getTarget() {
    return target;
  }

  /**
   * Returns the actions
   *
   * @return the action (if set)
   */
  @Nonnull
  public List<? extends Action<T>> getActions() {
    return Collections.unmodifiableList( actions );
  }

  /**
   * <p>getFirstAction</p>
   *
   * @return a {@link Action} object.
   */
  @Deprecated
  @Nonnull
  public Action<T> getFirstAction() {
    return actions.get( 0 );
  }
}
