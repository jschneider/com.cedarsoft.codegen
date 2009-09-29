package com.cedarsoft.history;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Contains a collection of entries
 *
 * @param <E> the type
 */
public class ClusteredElementsCollection<E> implements ClusteredObservableObjectAccess<E> {
  @NotNull
  @NonNls
  public static final String PROPERTY_ELEMENTS = "elements";

  private Long id;
  @NotNull
  protected final List<E> elements = new ArrayList<E>();

  @NotNull
  protected final ClusteredCollectionSupport<E> collectionSupport = new ClusteredCollectionSupport<E>( this );

  @NotNull
  protected final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

  public ClusteredElementsCollection() {
  }

  public ClusteredElementsCollection( @NotNull Collection<? extends E> elements ) {
    this.elements.addAll( elements );
  }

  public final void add( @NotNull E element ) {
    addElement( element );
  }

  @Deprecated
  @NotNull
  public ClusteredCollectionSupport<E> getCollectionSupport() {
    return collectionSupport;
  }

  /**
   * Adds a new entry
   *
   * @param element the entry that is added
   */
  public void addElement( @NotNull E element ) {
    lock.writeLock().lock();
    int index;
    try {
      elements.add( element );
      index = elements.indexOf( element );
    } finally {
      lock.writeLock().unlock();
    }
    collectionSupport.elementAdded( element, index );
  }

  public void commit( @NotNull E element ) {

    lock.readLock().lock();
    int index;
    try {
      index = elements.indexOf( element );
    } finally {
      lock.readLock().unlock();
    }

    collectionSupport.elementChanged( element, index );
  }

  /**
   * Whether this contains any entries
   *
   * @return whether this  contains any entries
   */
  public boolean hasElements() {
    lock.readLock().lock();
    try {
      return !elements.isEmpty();
    } finally {
      lock.readLock().unlock();
    }
  }

  public boolean isEmpty() {
    return !hasElements();
  }

  public void addAll( @NotNull List<? extends E> additionalElements ) {
    for ( E element : additionalElements ) {
      add( element );
    }
  }

  public int size() {
    lock.readLock().lock();
    try {
      return elements.size();
    } finally {
      lock.readLock().unlock();
    }
  }

  /**
   * Returns all cessions
   *
   * @return the cessions
   */
  @NotNull
  public List<? extends E> getElements() {
    lock.readLock().lock();
    try {
      return Collections.unmodifiableList( elements );
    } finally {
      lock.readLock().unlock();
    }
  }

  public void setElements( @NotNull List<? extends E> elements ) {
    List<E> newElements = new ArrayList<E>( elements );

    lock.writeLock().lock();
    try {
      for ( E element : new ArrayList<E>( this.elements ) ) {
        if ( !newElements.remove( element ) ) {
          remove( element );
        }
      }

      for ( E newElement : newElements ) {
        add( newElement );
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  public final void remove( @NotNull E element ) {
    removeEntry( element );
  }

  public boolean removeEntry( @NotNull E element ) {
    lock.writeLock().lock();
    boolean removed;
    int index;
    try {
      index = elements.indexOf( element );
      removed = elements.remove( element );
    } finally {
      lock.writeLock().unlock();
    }

    collectionSupport.elementDeleted( element, index );
    return removed;
  }

  public void addElementListener( @NotNull ElementsListener<? super E> listener ) {
    addElementListener( listener, true );
  }

  @NotNull
  public List<? extends ElementsListener<? super E>> getTransientElementListeners() {
    return collectionSupport.getTransientElementListeners();
  }

  public void addElementListener( @NotNull ElementsListener<? super E> listener, boolean isTransient ) {
    collectionSupport.addElementListener( listener, isTransient );
  }

  public void removeElementListener( @NotNull ElementsListener<? super E> listener ) {
    collectionSupport.removeElementListener( listener );
  }

  @NotNull
  public List<? extends E> findElements( @NotNull ElementVisitor<? super E> visitor ) {
    List<E> found = new ArrayList<E>();

    lock.readLock().lock();
    try {
      for ( E element : elements ) {
        if ( visitor.fits( element ) ) {
          found.add( element );
        }
      }
    } finally {
      lock.readLock().unlock();
    }

    return Collections.unmodifiableList( found );
  }

  /**
   * Returns the first entry that matches the visistor
   *
   * @param visitor the visitor that identifies the entries
   * @return the first entry
   *
   * @throws com.cedarsoft.history.NoElementFoundException
   *          if no entry has been found
   */
  @NotNull
  public E findFirstElement( @NotNull ElementVisitor<? super E> visitor ) throws NoElementFoundException {
    E found = findFirstElementNullable( visitor );
    if ( found == null ) {
      throw new NoElementFoundException( "No element found for " + visitor.getIdentifier() );
    }
    return found;
  }

  @Nullable
  public E findFirstElementNullable( @NotNull ElementVisitor<? super E> visitor ) {
    lock.readLock().lock();
    try {
      for ( E element : elements ) {
        if ( visitor.fits( element ) ) {
          return element;
        }
      }
    } finally {
      lock.readLock().unlock();
    }
    return null;
  }

  public boolean contains( @NotNull E element ) {
    lock.readLock().lock();
    try {
      return elements.contains( element );
    } finally {
      lock.readLock().unlock();
    }
  }

  public boolean contains( @NotNull ElementVisitor<? super E> visitor ) throws NoElementFoundException {
    lock.readLock().lock();
    try {
      for ( E element : elements ) {
        if ( visitor.fits( element ) ) {
          return true;
        }
      }
    } finally {
      lock.readLock().unlock();
    }
    return false;
  }

  /**
   * Removes the entries
   *
   * @param visitor the visitor that describes the entries
   */
  @NotNull
  public List<? extends E> removeElements( @NotNull ElementVisitor<? super E> visitor ) {
    List<E> removed = new ArrayList<E>();
    lock.writeLock().lock();
    try {
      for ( Iterator<E> it = elements.iterator(); it.hasNext(); ) {
        E element = it.next();
        if ( visitor.fits( element ) ) {
          it.remove();
          removed.add( element );
        }
      }
    } finally {
      lock.writeLock().unlock();
    }

    return removed;
  }

  public void clear() {
    if ( !collectionSupport.hasListeners() ) {
      lock.writeLock().lock();
      try {
        elements.clear();
      } finally {
        lock.writeLock().unlock();
      }
    }

    lock.writeLock().lock();
    try {
      for ( Iterator<E> it = elements.iterator(); it.hasNext(); ) {
        E element = it.next();
        it.remove();
        collectionSupport.elementDeleted( element, 0 );
      }
    } finally {
      lock.writeLock().unlock();
    }
  }

  @NotNull
  public ReentrantReadWriteLock getLock() {
    return lock;
  }
}