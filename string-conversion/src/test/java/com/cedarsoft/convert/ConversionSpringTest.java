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

package com.cedarsoft.convert;

import javax.annotation.Nonnull;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.junit.*;

import static org.junit.Assert.*;

/**
 * <p/>
 * Date: 23.01.2007<br>
 * Time: 11:26:52<br>
 */
public class ConversionSpringTest {
  @Test
  public void testIt() {
    ClassPathResource resource = new ClassPathResource( "/com/cedarsoft/convert/converter.spr.xml" );

    BeanFactory factory = new XmlBeanFactory( resource );
    StringConverterManager manager = ( StringConverterManager ) factory.getBean( "defaultStringConverterManager", StringConverterManager.class );
    assertNotNull( manager );
    assertEquals( 6, manager.getConverterMap().size() );

    assertEquals( "java.lang.String", manager.serialize( String.class ) );
  }

  @Test
  public void testOverride() {
    ApplicationContext applicationContext = new ClassPathXmlApplicationContext( new String[]{
      "/com/cedarsoft/convert/converter.spr.xml",
      "/com/cedarsoft/convert/converter_test.spr.xml"
    } );
    StringConverterManager manager = ( StringConverterManager ) applicationContext.getBean( "customStringConverterManager", StringConverterManager.class );
    assertNotNull( manager );
    assertEquals( 7, manager.getConverterMap().size() );

    assertEquals( "faafdfsd", manager.serialize( new StringBuilder( "faafdfsd" ) ) );
  }

  public static class AnObjectConverter implements StringConverter<StringBuilder> {
    @Override
    @Nonnull
    public String createRepresentation( @Nonnull StringBuilder object ) {
      return object.toString();
    }

    @Override
    @Nonnull
    public StringBuilder createObject( @Nonnull String representation ) {
      return new StringBuilder( representation );
    }
  }

}
