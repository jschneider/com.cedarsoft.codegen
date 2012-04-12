package com.cedarsoft.test.utils;

import com.google.common.base.Joiner;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import org.junit.rules.*;
import org.junit.runner.*;
import org.junit.runners.model.*;


public class ThreadRule implements TestRule {

  public static final String STACK_TRACE_ELEMENT_SEPARATOR = "\n\tat ";

  @Nullable
  private final ThreadMatcher ignoredThreadMatcher;

  public ThreadRule() {
    this( new DefaultThreadMatcher() );
  }

  public ThreadRule( @Nullable ThreadMatcher ignoredThreadMatcher ) {
    this.ignoredThreadMatcher = ignoredThreadMatcher;
  }

  @Override
  public Statement apply( final Statement base, Description description ) {
    return new Statement() {
      @Override
      public void evaluate() throws Throwable {
        before();
        try {
          base.evaluate();
        } catch ( Throwable t ) {
          afterFailing();
          throw t;
        }
        after();
      }
    };
  }

  private Collection<Thread> initialThreads;

  private void before() {
    if ( initialThreads != null ) {
      throw new IllegalStateException( "???" );
    }

    initialThreads = Thread.getAllStackTraces().keySet();
  }

  @Nonnull
  public Collection<? extends Thread> getInitialThreads() {
    if ( initialThreads == null ) {
      throw new IllegalStateException( "not initialized yet" );
    }
    return Collections.unmodifiableCollection( initialThreads );
  }

  private void afterFailing() {
    Set<? extends Thread> remainingThreads = getRemainingThreads();
    if ( !remainingThreads.isEmpty() ) {
      System.err.print( "Some threads have been left:\n" + buildMessage( remainingThreads ) );
    }
  }

  private void after() {
    Set<? extends Thread> remainingThreads = getRemainingThreads();
    if ( !remainingThreads.isEmpty() ) {
      throw new IllegalStateException( "Some threads have been left:\n" + buildMessage( remainingThreads ) );
    }
  }

  @Nonnull
  private Set<? extends Thread> getRemainingThreads() {
    Collection<Thread> threadsNow = Thread.getAllStackTraces().keySet();

    Set<Thread> remainingThreads = new HashSet<Thread>( threadsNow );
    remainingThreads.removeAll( initialThreads );

    for ( Iterator<Thread> iterator = remainingThreads.iterator(); iterator.hasNext(); ) {
      Thread remainingThread = iterator.next();
      if ( !remainingThread.isAlive() ) {
        iterator.remove();
        continue;
      }

      //Ignore the threads
      if ( this.ignoredThreadMatcher != null && ignoredThreadMatcher.shallIgnore( remainingThread ) ) {
        iterator.remove();
        continue;
      }

      //Give the thread a very(!) short time to die off
      try {
        Thread.sleep( 10 );
      } catch ( InterruptedException ignore ) {
      }

      //Second try
      if ( !remainingThread.isAlive() ) {
        iterator.remove();
      }
    }
    return remainingThreads;
  }

  @Nonnull
  private String buildMessage( @Nonnull Set<? extends Thread> remainingThreads ) {
    StringBuilder builder = new StringBuilder();

    builder.append( "// Remaining Threads:" ).append( "\n" );
    builder.append( "-----------------------" ).append( "\n" );
    for ( Thread remainingThread : remainingThreads ) {
      builder.append( "---" );
      builder.append( "\n" );
      builder.append( remainingThread );
      builder.append( STACK_TRACE_ELEMENT_SEPARATOR );
      builder.append( Joiner.on( STACK_TRACE_ELEMENT_SEPARATOR ).join( remainingThread.getStackTrace() ) );
      builder.append( "\n" );
    }
    builder.append( "-----------------------" ).append( "\n" );

    return builder.toString();
  }

  public interface ThreadMatcher {
    boolean shallIgnore( @Nonnull Thread remainingThread );
  }

  /**
   * Default implementation that ignore several known threads.
   */
  public static class DefaultThreadMatcher implements ThreadMatcher {
    @Override
    public boolean shallIgnore( @Nonnull Thread remainingThread ) {
      return remainingThread.getThreadGroup().getName().equals( "system" ) &&
        remainingThread.getName().equals( "Keep-Alive-Timer" );
    }
  }
}
