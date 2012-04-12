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

package de.xore.util.persistence;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.List;
import java.util.Properties;

/**
 * Soll/kann von den einzelnen Modulen ueberschrieben werden - mit den entsprechenden Queries <p>
 * Date: 14.04.2005<br>
 * Time: 14:38:17<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public abstract class QueryManager<T> {
  protected DatabaseConnector<T> connector;

  protected QueryManager( @Nonnull DatabaseConnector<T> connector ) {
    this.connector = connector;
  }

  public DatabaseConnector<T> getConnector() {
    return connector;
  }

  public void refresh( Object obj ) {
    connector.beginTransaction();
    connector.refresh( obj );
    connector.commit();
  }

  /**
   * Update the database
   *
   * @param obj the object that is saved
   */
  public void update( Object obj ) {
    connector.beginTransaction();
    connector.update( obj );
    connector.commit();
  }

  public void save( @Nonnull Object object ) {
    connector.beginTransaction();
    connector.save( object );
    connector.commit(); //todo wirklich automatisch committen??
  }

  public void saveOrUpdate( @Nonnull Object object ) {
    connector.beginTransaction();
    connector.saveOrUpdate( object );
    connector.commit(); //todo wirklich automatisch committen??
  }

  public void delete( @Nonnull Object object ) {
    connector.beginTransaction();
    connector.delete( object );
    connector.commit();
  }

  @Nullable
  public <T> T getFirstQueryResult( String queryString ) throws DatabaseException {
    List<T> queryResult = getQueryResult( queryString );
    if ( !queryResult.isEmpty() ) {
      return queryResult.get( 0 );
    }
    return null;
  }

  @Nullable
  public <T> T getFirstQueryResult( String queryString, Properties params ) throws DatabaseException {
    List<T> queryResult = getQueryResult( queryString, params );
    if ( !queryResult.isEmpty() ) {
      return queryResult.get( 0 );
    }
    return null;
  }

  @Nullable
  public <T> T getFirstQueryResult( String queryString, String name, String val ) throws DatabaseException {
    List<T> queryResult = getQueryResult( queryString, name, val );
    if ( !queryResult.isEmpty() ) {
      return queryResult.get( 0 );
    }
    return null;
  }

  public abstract <T> List<T> getQueryResult( String queryString ) throws DatabaseException;

  public abstract <T> List<T> getQueryResult( String queryString, Properties params ) throws DatabaseException;

  public abstract <T> List<T> getQueryResult( String queryString, String property, String value ) throws DatabaseException;

  public void dispose() {
    getConnector().dispose();
  }
}


