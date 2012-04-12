package com.cedarsoft.test.utils;

import org.junit.*;

import static org.fest.assertions.Assertions.assertThat;
import static org.junit.Assert.assertFalse;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Ignore
public class CatchAllExceptionsRuleTest {
  @Rule
  public CatchAllExceptionsRule catchAllExceptionsRule = new CatchAllExceptionsRule();

  @Test
  public void successfulTest() throws Exception {
    Thread thread = new Thread( new Runnable() {
      @Override
      public void run() {
        throw new RuntimeException( "This one is ignored by JUnit!" );
      }
    } );
    thread.start();
    thread.join();
  }

  @Test
  public void ignoredAssertion() throws Exception {
    Thread thread = new Thread( new Runnable() {
      @Override
      public void run() {
        assertFalse(true); //will not be reported!
        throw new RuntimeException( "This one is ignored by JUnit!" );
      }
    } );
    thread.start();
    thread.join();
  }

  @Test
  public void testIt() throws Exception {
    Thread thread = new Thread( new Runnable() {
      @Override
      public void run() {
        throw new IllegalStateException( "Hey, are u visible?" );
      }
    } );

    assertThat( thread.isAlive() ).isFalse();
    thread.start();
    thread.join();

    Thread thread2 = new Thread( new Runnable() {
      @Override
      public void run() {
        throw new IllegalArgumentException( "Exception2" );
      }
    } );

    assertThat( thread2.isAlive() ).isFalse();
    thread2.start();
    thread2.join();
    assertThat( thread2.isAlive() ).isFalse();
  }

  @Test
  public void testFailing() throws Exception {
    Thread thread = new Thread( new Runnable() {
      @Override
      public void run() {
        throw new IllegalStateException( "Hey, are u visible?" );
      }
    } );

    assertThat( thread.isAlive() ).isFalse();
    thread.start();
    thread.join();

    Thread thread2 = new Thread( new Runnable() {
      @Override
      public void run() {
        throw new IllegalArgumentException( "Exception2" );
      }
    } );

    assertThat( thread2.isAlive() ).isFalse();
    thread2.start();
    thread2.join();
    assertThat( thread2.isAlive() ).isFalse();

    throw new UnsupportedOperationException( "asdf" );
  }
}
