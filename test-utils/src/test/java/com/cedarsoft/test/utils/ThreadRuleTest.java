package com.cedarsoft.test.utils;

import org.junit.*;

import static org.fest.assertions.Fail.fail;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
@Ignore
public class ThreadRuleTest {
  @Rule
  public ThreadRule threadRule = new ThreadRule();

  @Test
  public void testThreadRule() throws Exception {
    new Thread( new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep( 100 );
        } catch ( InterruptedException e ) {
          throw new RuntimeException( e );
        }
      }
    } ).start();
  }

  @Test
  public void testMulti() throws Exception {
    new Thread( new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep( 100 );
        } catch ( InterruptedException e ) {
          throw new RuntimeException( e );
        }
      }
    } ).start();
    new Thread( new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep( 100 );
        } catch ( InterruptedException e ) {
          throw new RuntimeException( e );
        }
      }
    } ).start();
  }

  @Test
  public void testFailing() throws Exception {
    new Thread( new Runnable() {
      @Override
      public void run() {
        try {
          Thread.sleep( 100 );
        } catch ( InterruptedException e ) {
          throw new RuntimeException( e );
        }
      }
    } ).start();

    fail( "failing..." );
  }
}
