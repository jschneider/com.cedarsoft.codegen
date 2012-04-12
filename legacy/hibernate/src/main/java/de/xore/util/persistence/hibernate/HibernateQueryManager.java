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

package de.xore.util.persistence.hibernate;


import de.xore.util.persistence.DatabaseException;
import de.xore.util.persistence.QueryManager;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

/*
 * Created by XoreSystems (Johannes Schneider).
 * User: Johannes
 * Date: 21.01.2004
 * Time: 00:27:29
 *
 */

/**
 * <p/>
 * Date: 21.01.2004<br> Time: 00:27:29<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public class HibernateQueryManager extends QueryManager<Session> {
  public HibernateQueryManager( @Nonnull HibernateDatabaseConnector connector ) {
    super( connector );
  }

  @Override
  @Nonnull
  public <T> List<T> getQueryResult( @Nonnull String queryString ) throws DatabaseException {
    return getQueryResult( queryString, null );
  }

  @Override
  @Nonnull
  public <T> List<T> getQueryResult( @Nonnull String queryString, @Nullable Properties params ) throws DatabaseException {
    Session session = connector.getSession();
    Transaction tx = session.beginTransaction();

    Query query = session.createQuery( queryString );
    setParams( query, params );

    List<T> items = query.list();

    tx.commit();
    return items;
  }

  @Override
  @Nonnull
  public <T> List<T> getQueryResult( @Nonnull String queryString, @Nonnull String property, @Nonnull String value ) throws DatabaseException {
    Properties properties = new Properties();
    properties.setProperty( property, value );
    return getQueryResult( queryString, properties );
  }

  private static void setParams( @Nonnull Query query, @Nullable Properties params ) {
    if ( params == null ) {
      return;
    }
    Enumeration<?> enumeration = params.propertyNames();
    while ( enumeration.hasMoreElements() ) {
      String propertyName = ( String ) enumeration.nextElement();
      String value = params.getProperty( propertyName );
      query.setString( propertyName, value );
    }
  }
}
