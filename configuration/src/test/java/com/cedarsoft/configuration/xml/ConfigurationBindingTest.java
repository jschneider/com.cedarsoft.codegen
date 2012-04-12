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

import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;

import javax.annotation.Nonnull;
import org.junit.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: Jul 2, 2007<br>
 * Time: 5:26:34 PM<br>
 */
public class ConfigurationBindingTest {
  private Configuration configuration;

  @Before
  public void setUp() throws Exception {
    configuration = new BaseConfiguration();
  }

  @Test
  public void testMapping() {
    MyBean bean = new MyBean();
    BeanAdapter<MyBean> beanAdapter = new BeanAdapter<MyBean>( bean, true );
    ValueModel valueModel = beanAdapter.getValueModel( "value" );

    ConfigurationAccess<String> configurationAccess = new ConfigurationAccess<String>( configuration, String.class, "valueKey", "theDefaultValue" );

    //manually
    ConfigurationConnector<String> connector = new ConfigurationConnector<String>( valueModel, configurationAccess );
    assertEquals( "", bean.getValue() );
    connector.readFromConfiguration();
    assertEquals( "theDefaultValue", bean.getValue() );
    assertEquals( "theDefaultValue", configuration.getString( "valueKey" ) );

    bean.setValue( "newValue" );
    assertEquals( "newValue", bean.getValue() );
    assertEquals( "newValue", configuration.getString( "valueKey" ) );
  }

  @Test
  public void testMapping2() {
    MyBean bean = new MyBean();
    BeanAdapter<MyBean> beanAdapter = new BeanAdapter<MyBean>( bean, true );

    ConfigurationAccess<String> configurationAccess = new ConfigurationAccess<String>( configuration, String.class, "valueKey", "theDefaultValue" );

    //manually
    ConfigurationBinding.bind( configurationAccess, beanAdapter.getValueModel( "value" ) );
    assertEquals( "theDefaultValue", bean.getValue() );
    assertEquals( "theDefaultValue", configuration.getString( "valueKey" ) );

    bean.setValue( "newValue" );
    assertEquals( "newValue", bean.getValue() );
    assertEquals( "newValue", configuration.getString( "valueKey" ) );
  }

  @Test
  public void testMappingList() {
    MyBean bean = new MyBean();
    assertEquals( 0, bean.getValues().size() );
    BeanAdapter<MyBean> beanAdapter = new BeanAdapter<MyBean>( bean, true );

    ConfigurationAccess<List> configurationAccess = new ConfigurationAccess<List>( configuration, List.class, "values", Arrays.asList( "a", "b" ) );
    ConfigurationBinding.bind( configurationAccess, beanAdapter.getValueModel( "values" ) );

    assertEquals( 2, bean.getValues().size() );
    assertEquals( "a", bean.getValues().get( 0 ) );
    assertEquals( "b", bean.getValues().get( 1 ) );

    assertEquals( 2, configuration.getList( "values" ).size() );
    assertEquals( "a", configuration.getList( "values" ).get( 0 ) );
    assertEquals( "b", configuration.getList( "values" ).get( 1 ) );

    bean.addValue( "c" );

    assertEquals( 3, bean.getValues().size() );
    assertEquals( "a", bean.getValues().get( 0 ) );
    assertEquals( "b", bean.getValues().get( 1 ) );
    assertEquals( "c", bean.getValues().get( 2 ) );

    assertEquals( 3, configuration.getList( "values" ).size() );
    assertEquals( "a", configuration.getList( "values" ).get( 0 ) );
    assertEquals( "b", configuration.getList( "values" ).get( 1 ) );
    assertEquals( "c", configuration.getList( "values" ).get( 2 ) );

    bean.removeValue( "b" );

    assertEquals( 2, bean.getValues().size() );
    assertEquals( "a", bean.getValues().get( 0 ) );
    assertEquals( "c", bean.getValues().get( 1 ) );

    assertEquals( 2, configuration.getList( "values" ).size() );
    assertEquals( "a", configuration.getList( "values" ).get( 0 ) );
    assertEquals( "c", configuration.getList( "values" ).get( 1 ) );
  }

  public static class MyBean {
    private final PropertyChangeSupport pcs = new PropertyChangeSupport( this );
    @Nonnull
    private String value = "";
    private final List<String> values = new ArrayList<String>();

    @Nonnull
    public List<String> getValues() {
      return Collections.unmodifiableList( values );
    }

    public void setValues( @Nonnull List<String> values ) {
      List<String> old = new ArrayList<String>( values );
      this.values.clear();
      this.values.addAll( values );
      pcs.firePropertyChange( "values", old, Collections.unmodifiableList( this.values ) );
    }

    public void addValue( @Nonnull String otherValue ) {
      List<String> old = new ArrayList<String>( values );
      this.values.add( otherValue );
      pcs.firePropertyChange( "values", old, Collections.unmodifiableList( this.values ) );
    }

    public void removeValue( @Nonnull String otherValue ) {
      List<String> old = new ArrayList<String>( values );
      this.values.remove( otherValue );
      pcs.firePropertyChange( "values", old, Collections.unmodifiableList( this.values ) );
    }

    @Nonnull
    public String getValue() {
      return value;
    }

    public void setValue( @Nonnull String value ) {
      pcs.firePropertyChange( "value", this.value, this.value = value );
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
