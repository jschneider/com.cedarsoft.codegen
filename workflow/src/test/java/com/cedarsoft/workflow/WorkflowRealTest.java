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
import org.junit.*;

import static org.junit.Assert.*;

/**
 *
 */
public class WorkflowRealTest {
  private WorkflowDefinition<MyObject> workflowDefinition;

  @Before
  public void setUp() throws Exception {
    State<MyObject> input = new State<MyObject>( "input" );
    workflowDefinition = new WorkflowDefinition<MyObject>( input );

    State<MyObject> audit = new State<MyObject>( "audit" );
    input.createTransition( audit, new Action<MyObject>() {
      @Override
      public void execute( @Nonnull MyObject object, @Nonnull TransitionDefinition<MyObject> definition ) {
        object.setMessage( "1" );
      }
    } );

    State<MyObject> bidding = new State<MyObject>( "bidding" );
    audit.createTransition( bidding );

    //Kauf
    State<MyObject> acquisition = new State<MyObject>( "acquisition" );
    bidding.createTransition( acquisition );

    State<MyObject> holding = new State<MyObject>( "holding" );
    acquisition.createTransition( holding );

    State<MyObject> selling = new State<MyObject>( "selling" );
    holding.createTransition( selling );

    State<MyObject> managing = new State<MyObject>( "managing" );
    selling.createTransition( managing );

    //Ablehnung
    bidding.createTransition( new State<MyObject>( "discarded" ) );
  }

  @After
  public void tearDown() throws Exception {

  }

  @Test
  public void testCreate() {
    assertEquals( 1, workflowDefinition.getInitialState().getTransitions().size() );
  }

  @Test
  public void testWorkflow() {
    MyObject bean = new MyObject();

    Workflow<MyObject> workflow = new Workflow<MyObject>( bean, workflowDefinition );
    assertEquals( "input", workflow.getCurrentState().getId() );
    assertEquals( 1, workflow.getPossibleTransitionDefinitions().size() );

    workflow.getPossibleTransition().get( 0 ).transit();
    assertEquals( "audit", workflow.getCurrentState().getId() );

    workflow.getPossibleTransition().get( 0 ).transit();
    assertEquals( "bidding", workflow.getCurrentState().getId() );

    workflow.getPossibleTransition().get( 0 ).transit();
    assertEquals( "acquisition", workflow.getCurrentState().getId() );
  }

  @Test
  public void testActions() {
    MyObject object = new MyObject();
    Workflow<MyObject> workflow = workflowDefinition.createWorkflow( object );
    assertEquals( "input", workflow.getCurrentState().getId() );
    workflow.getPossibleTransition().get( 0 ).transit();

    assertEquals( "audit", workflow.getCurrentState().getId() );

    assertEquals( "1", object.getMessage() );
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
