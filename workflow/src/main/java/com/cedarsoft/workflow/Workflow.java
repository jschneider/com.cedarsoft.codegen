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
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>Workflow class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class Workflow<T> {
  @Nonnull
  private final WorkflowDefinition<T> workflowDefinition;
  @Nonnull
  private final T bean;

  @Nonnull
  private State<T> currentState;

  @Nonnull
  private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  /**
   * Creates a workflow for the given bean
   *
   * @param bean               the bean the workflow is created for
   * @param workflowDefinition the definition
   */
  public Workflow( @Nonnull T bean, @Nonnull WorkflowDefinition<T> workflowDefinition ) {
    this.workflowDefinition = workflowDefinition;
    this.bean = bean;
    currentState = workflowDefinition.getInitialState();
  }

  /**
   * Returns the bean of that workflow
   *
   * @return the bean
   */
  @Nonnull
  public T getBean() {
    return bean;
  }

  /**
   * Returns the workflow definition this workflow is based on
   *
   * @return the workflow definition
   */
  @Nonnull
  public WorkflowDefinition<T> getWorkflowDefinition() {
    return workflowDefinition;
  }

  /**
   * Returns the current state
   *
   * @return the current state
   */
  @Nonnull
  public State<T> getCurrentState() {
    lock.readLock().lock();
    try {
      return currentState;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Returns the possible transitions.
   *
   * @return the possible transitions
   */
  @Nonnull
  public List<? extends Transition<T>> getPossibleTransition() {
    List<Transition<T>> list = new ArrayList<Transition<T>>();
    for ( TransitionDefinition<T> definition : getCurrentState().getTransitions() ) {
      list.add( createTransition( definition ) );
    }
    return list;
  }

  /**
   * Returns the possible transition definitions
   *
   * @return the currently possible transition definitions
   */
  @Nonnull
  @Deprecated
  public List<? extends TransitionDefinition<T>> getPossibleTransitionDefinitions() {
    return getCurrentState().getTransitions();
  }

  @Nonnull
  private Transition<T> createTransition( @Nonnull final TransitionDefinition<T> definition ) {
    return new Transition<T>() {
      @Override
      public void transit() {
        lock.writeLock().lock();
        try {
          //Now execute the actions
          for ( Action<T> action : definition.getActions() ) {
            action.execute( bean, definition );
          }
          currentState = definition.getTarget();
        } finally {
          lock.writeLock().unlock();
        }
      }
    };
  }
}
