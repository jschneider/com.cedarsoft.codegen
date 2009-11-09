package com.cedarsoft.utils.event;

import org.jetbrains.annotations.NotNull;
import static org.testng.Assert.*;
import org.testng.annotations.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class ClusteredPropertyChangeSupportTest {
  private ClusteredPropertyChangeSupport pcs;

  @BeforeMethod
  protected void setUp() throws Exception {
    pcs = new ClusteredPropertyChangeSupport( "asdf" );
  }

  @AfterMethod
  protected void tearDown() throws Exception {

  }

  @Test
  public void testIt() {
    assertEquals( 0, pcs.getTransientSupport().getPropertyChangeListeners().length );
    assertEquals( 0, pcs.getNonTransientListeners().size() );

    pcs.addPropertyChangeListener( new PropertyChangeListener() {
      @java.lang.Override
      public void propertyChange( PropertyChangeEvent evt ) {
      }
    }, true );

    assertEquals( 1, pcs.getTransientSupport().getPropertyChangeListeners().length );
    assertEquals( 0, pcs.getNonTransientListeners().size() );

    pcs.addPropertyChangeListener( new PropertyChangeListener() {
      @java.lang.Override
      public void propertyChange( PropertyChangeEvent evt ) {
      }
    }, false );

    assertEquals( 1, pcs.getTransientSupport().getPropertyChangeListeners().length );
    assertEquals( 1, pcs.getNonTransientListeners().size() );
  }

  @Test
  public void testWIthPropName() {
    @NotNull
    final List<PropertyChangeEvent> events = new ArrayList<PropertyChangeEvent>();

    PropertyChangeListener listener = new PropertyChangeListener() {
      @java.lang.Override
      public void propertyChange( PropertyChangeEvent evt ) {
        events.add( evt );
      }
    };
    pcs.addPropertyChangeListener( "asdf", listener, false );

    pcs.firePropertyChange( "asdf", "old", "new" );

    assertEquals( 1, events.size() );
    assertEquals( "new", events.get( 0 ).getNewValue() );

    events.clear();

    pcs.removePropertyChangeListener( "asdf", listener );
    pcs.firePropertyChange( "asdf", "old", "new2" );

    assertEquals( 0, events.size() );
  }
}
