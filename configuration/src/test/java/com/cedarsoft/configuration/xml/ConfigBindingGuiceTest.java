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

package com.cedarsoft.configuration.xml;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import javax.inject.Inject;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Scopes;
import com.jgoodies.binding.beans.BeanAdapter;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

import javax.annotation.Nonnull;
import org.junit.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import static org.junit.Assert.*;

/**
 *
 */
public class ConfigBindingGuiceTest {
  private Injector injector;

  @Before
  public void setUp() throws Exception {

    injector = Guice.createInjector( new AbstractModule() {
      @Override
      protected void configure() {
        bind( Configuration.class ).toInstance( new BaseConfiguration() );

        bind( MyBean.class ).toProvider( new Provider<MyBean>() {
          @Inject
          Configuration configuration;

          @Override
          @Nonnull
          public MyBean get() {
            MyBean myBean = new MyBean();
            BeanAdapter<MyBean> beanAdapter = new BeanAdapter<MyBean>( myBean, true );
            ConfigurationAccess<String> configurationAccess = new ConfigurationAccess<String>( configuration, String.class, MyBean.PROPERTY_VALUE, "theDefaultValue" );
            ConfigurationBinding.bind( configurationAccess, beanAdapter.getValueModel( MyBean.PROPERTY_VALUE ) );
            return myBean;
          }
        } ).in( Scopes.SINGLETON );
      }
    } );
  }

  @Test
  public void testGetConnector() {
    assertEquals( "theDefaultValue", injector.getInstance( MyBean.class ).getValue() );
    injector.getInstance( MyBean.class ).setValue( "asdf" );
    assertEquals( "asdf", injector.getInstance( Configuration.class ).getString( MyBean.PROPERTY_VALUE ) );
  }

  public static class MyBean {
    @Nonnull
    static final String PROPERTY_VALUE = "value";

    private PropertyChangeSupport pcs = new PropertyChangeSupport( this );
    private String value;

    public String getValue() {
      return value;
    }

    public void setValue( String value ) {
      pcs.firePropertyChange( PROPERTY_VALUE, this.value, this.value = value );
    }

    public void addPropertyChangeListener( PropertyChangeListener listener ) {
      pcs.addPropertyChangeListener( listener );
    }

    public void removePropertyChangeListener( PropertyChangeListener listener ) {
      pcs.removePropertyChangeListener( listener );
    }

    public void addPropertyChangeListener( String propertyName, PropertyChangeListener listener ) {
      pcs.addPropertyChangeListener( propertyName, listener );
    }

    public void removePropertyChangeListener( String propertyName, PropertyChangeListener listener ) {
      pcs.removePropertyChangeListener( propertyName, listener );
    }
  }
}
