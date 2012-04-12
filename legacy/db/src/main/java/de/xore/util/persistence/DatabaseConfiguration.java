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

import java.util.ResourceBundle;

/**
 * Configuration for a specific database
 * <p/>
 * Date: 18.05.2005<br>
 * Time: 23:43:33<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class DatabaseConfiguration {
  public static DatabaseConfiguration createConfiguration( @Nonnull String bundlename ) {
    ResourceBundle resourceBundle = ResourceBundle.getBundle( bundlename );
    if ( resourceBundle == null ) {
      throw new DatabaseException( "ResourceBundle not found for bundlename " + bundlename );
    }
    String url = resourceBundle.getString( "url" );
    if ( url == null || url.length() == 0 ) {
      throw new DatabaseException( "invalid url " + url );
    }
    String user = resourceBundle.getString( "user" );
    if ( user == null || user.length() == 0 ) {
      throw new DatabaseException( "invalid user " + user );
    }
    String pass = resourceBundle.getString( "password" );
    if ( pass == null || pass.length() == 0 ) {
      throw new DatabaseException( "invalid password " + pass );
    }
    String databaseTypeAsString = resourceBundle.getString( "type" );
    if ( databaseTypeAsString == null || databaseTypeAsString.length() == 0 ) {
      throw new DatabaseException( "invalid type " + databaseTypeAsString );
    }
    DatabaseType databaseType = DatabaseType.valueOf( databaseTypeAsString );
    return new DatabaseConfiguration( url, user, pass, databaseType );
  }

  @Nonnull
  private DatabaseType databaseTypeType = DatabaseType.MYSQL;
  @Nonnull
  private String url = "";
  @Nonnull
  private String user = "";
  @Nonnull
  private String pass = "";

  public DatabaseConfiguration( @Nonnull String url, @Nonnull String user, @Nonnull String pass, @Nonnull DatabaseType databaseTypeType ) {
    this.url = url;
    this.user = user;
    this.pass = pass;
    this.databaseTypeType = databaseTypeType;
  }

  @Nonnull
  public DatabaseType getDatabaseType() {
    return databaseTypeType;
  }

  @Nonnull
  public String getDriverClassName() {
    return databaseTypeType.getDriverClassName();
  }

  public boolean isValid() {
    return databaseTypeType != null && url != null && user != null && pass != null;
  }

  public void setDatabaseType( @Nonnull DatabaseType databaseTypeType ) {
    this.databaseTypeType = databaseTypeType;
  }

  @Nonnull
  public String getPass() {
    return pass;
  }

  public void setPass( @Nonnull String pass ) {
    this.pass = pass;
  }

  @Nonnull
  public String getUrl() {
    return url;
  }

  public void setUrl( @Nonnull String url ) {
    this.url = url;
  }

  @Nonnull
  public String getUser() {
    return user;
  }

  public void setUser( @Nonnull String user ) {
    this.user = user;
  }

  @Override
  @Nonnull
  @Override
  public String toString() {
    return "DatabaseConfiguration{" +
      "databaseTypeType=" + databaseTypeType +
      ", url='" + url + '\'' +
      ", user='" + user + '\'' +
      '}';
  }

  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( o == null || getClass() != o.getClass() ) return false;

    final DatabaseConfiguration databaseConfiguration = ( DatabaseConfiguration ) o;

    if ( databaseTypeType != databaseConfiguration.databaseTypeType ) return false;
    if ( pass != null ? !pass.equals( databaseConfiguration.pass ) : databaseConfiguration.pass != null ) return false;
    if ( url != null ? !url.equals( databaseConfiguration.url ) : databaseConfiguration.url != null ) return false;
    if ( user != null ? !user.equals( databaseConfiguration.user ) : databaseConfiguration.user != null ) return false;

    return true;
  }

  @Override
  public int hashCode() {
    int result;
    result = ( databaseTypeType != null ? databaseTypeType.hashCode() : 0 );
    result = 29 * result + ( url != null ? url.hashCode() : 0 );
    result = 29 * result + ( user != null ? user.hashCode() : 0 );
    result = 29 * result + ( pass != null ? pass.hashCode() : 0 );
    return result;
  }
}

