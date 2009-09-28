package com.cedarsoft.utils;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @param <T> the type that is stored within this registry
 */
public class DefaultRegistry<T> implements Registry<T> {
  @NotNull
  @NonNls
  protected final List<T> storedObjects = new ArrayList<T>();
  @NotNull
  protected final ReadWriteLock lock = new ReentrantReadWriteLock();

  /**
   * This comparator may optionally be set to ensure the registry only contains unique values
   */
  @Nullable
  protected final Comparator<T> comparator;

  /**
   * Creates an empty registry
   */
  public DefaultRegistry() {
    comparator = null;
  }

  /**
   * Creates a registry with containing the given objects.
   * No comparator is set
   *
   * @param storedObjects the stored objects
   */
  public DefaultRegistry( @NotNull Collection<? extends T> storedObjects ) {
    this( storedObjects, null );
  }

  /**
   * Creates an empty registry with the given (optional) comparator
   *
   * @param comparator the comparator
   */
  public DefaultRegistry( @Nullable Comparator<T> comparator ) {
    this.comparator = comparator;
  }

  /**
   * Creates a new registry
   *
   * @param storedObjects the initially stored objects
   * @param comparator    the (optional) comparator
   */
  public DefaultRegistry( @NotNull Collection<? extends T> storedObjects, @Nullable Comparator<T> comparator ) throws StillContainedException{
    this.comparator = comparator;

    if ( comparator != null ) {
      Set<T> set = new TreeSet<T>( comparator );
      set.addAll( storedObjects );
      if ( storedObjects.size() != set.size() ) {
        throw new StillContainedException( "The stored objects collections contains duplicate entries" );
      }
    } else {
      this.storedObjects.addAll( storedObjects );
    }

  }

  @NotNull
  public List<? extends T> getStoredObjects() {
    lock.readLock().lock();
    try {
      return Collections.unmodifiableList( storedObjects );
    } finally {
      lock.readLock().unlock();
    }
  }

  @Nullable
  public T findStoredObject( @NotNull @NonNls Matcher<T> matcher ) {
    lock.readLock().lock();
    try {
      for ( T object : storedObjects ) {
        if ( matcher.matches( object ) ) {
          return object;
        }
      }

      return null;
    } finally {
      lock.readLock().unlock();
    }
  }

  @NotNull
  public List<? extends T> findStoredObjects( @NotNull @NonNls Matcher<T> matcher ) {
    lock.readLock().lock();
    try {
      List<T> found = new ArrayList<T>();
      for ( T object : storedObjects ) {
        if ( matcher.matches( object ) ) {
          found.add( object );
        }
      }

      return found;
    } finally {
      lock.readLock().unlock();
    }
  }

  @NotNull
  public <C> List<? extends C> findStoredObjects( @NotNull @NonNls Matcher<T> matcher, @NotNull Converter<T, C> converter ) {
    lock.readLock().lock();
    try {
      List<C> found = new ArrayList<C>();
      for ( T object : storedObjects ) {
        if ( matcher.matches( object ) ) {
          found.add( converter.convert( object ) );
        }
      }

      return found;
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Stores the object within the registry
   *
   * @param object the object
   * @throws StillContainedException if a comparator is set and the object still exists within this registry
   */
  public void store( @NotNull T object ) throws StillContainedException {
    lock.writeLock().lock();
    try {
      if ( comparator != null ) {
        for ( T storedObject : storedObjects ) {
          if ( comparator.compare( storedObject, object ) == 0 ) {
            throw new StillContainedException( object );
          }
        }
      }

      storedObjects.add( object );
    } finally {
      lock.writeLock().unlock();
    }

    listenersLock.readLock().lock();
    try {
      for ( Listener<T> listener : listeners ) {
        listener.objectAdded( object );
      }
    } finally {
      listenersLock.readLock().unlock();
    }
  }

  @Nullable
  public Comparator<T> getComparator() {
    return comparator;
  }

  public boolean containsOnlyUniqueElements() {
    return comparator != null;
  }

  @NotNull
  protected final ReadWriteLock listenersLock = new ReentrantReadWriteLock();

  @NotNull
  protected final List<Listener<T>> listeners = new ArrayList<Listener<T>>();

  public void addListener( @NotNull Listener<T> listener ) {
    listenersLock.writeLock().lock();
    try {
      this.listeners.add( listener );
    } finally {
      listenersLock.writeLock().unlock();
    }
  }

  public void removeListener( @NotNull Listener<T> listener ) {
    listenersLock.writeLock().lock();
    try {
      this.listeners.remove( listener );
    } finally {
      listenersLock.writeLock().unlock();
    }
  }
}
