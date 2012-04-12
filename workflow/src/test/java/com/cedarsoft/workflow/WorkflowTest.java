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

import junit.framework.Assert;
import javax.annotation.Nonnull;
import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 */
public class WorkflowTest {
  private WorkflowDefinition<MyObject> workflowDefinition;
  private Workflow<MyObject> workflow;

  @Before
  public void setUp() throws Exception {
    workflowDefinition = new WorkflowDefinition<MyObject>( new State<MyObject>( "initialState" ) );
    workflow = workflowDefinition.createWorkflow( new MyObject() );

    workflowDefinition.getInitialState().createTransition( new State<MyObject>( "target1" ), new Action<MyObject>() {
      @Override
      public void execute( @Nonnull MyObject object, @Nonnull TransitionDefinition<MyObject> definition ) {
        object.setMessage( "1" );
      }
    } );
    workflowDefinition.getInitialState().createTransition( new State<MyObject>( "target2" ), new Action<MyObject>() {
      @Override
      public void execute( @Nonnull MyObject object, @Nonnull TransitionDefinition<MyObject> definition ) {
        object.setMessage( "first" );
      }
    }, new Action<MyObject>() {
      @Override
      public void execute( @Nonnull MyObject object, @Nonnull TransitionDefinition<MyObject> definition ) {
        object.setMessage( "2" );
      }
    } );
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testTransitions1() {
    assertNotNull( workflow );
    Assert.assertEquals( 2, workflow.getPossibleTransitionDefinitions().size() );
    Assert.assertEquals( 2, workflow.getPossibleTransition().size() );

    workflow.getPossibleTransition().get( 0 ).transit();
    Assert.assertEquals( "target1", workflow.getCurrentState().getId() );
    Assert.assertEquals( "1", workflow.getBean().getMessage() );
  }

  @Test
  public void testTransitions2() {
    assertNotNull( workflow );
    Assert.assertEquals( 2, workflow.getPossibleTransitionDefinitions().size() );
    Assert.assertEquals( 2, workflow.getPossibleTransition().size() );

    workflow.getPossibleTransition().get( 1 ).transit();
    Assert.assertEquals( "target2", workflow.getCurrentState().getId() );
    Assert.assertEquals( "2", workflow.getBean().getMessage() );
  }

  public static class MyObject {
    private String message = "";

    public String getMessage() {
      return message;
    }

    public void setMessage( String message ) {
      this.message = message;
    }
  }

}
