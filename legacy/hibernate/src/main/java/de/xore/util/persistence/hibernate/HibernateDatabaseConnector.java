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

import de.xore.util.persistence.DatabaseConfiguration;
import de.xore.util.persistence.DatabaseConnector;
import de.xore.util.persistence.DatabaseException;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.AnnotationConfiguration;
import org.hibernate.cfg.Environment;
import org.hibernate.dialect.Dialect;
import org.hibernate.dialect.FirebirdDialect;
import org.hibernate.dialect.MySQLDialect;
import javax.annotation.Nonnull;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

/**
 * Diese Klasse dient dazu, die Verbindungen zu der Datenbank entsprechend zu kapseln. Es sollte moeglichst wenig
 * Hibernate-Spezifisches Zeugs in den Klassen zu finden sein.
 * <p/>
 * <p> Date: 19.04.2005<br> Time: 22:34:09<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public class HibernateDatabaseConnector implements DatabaseConnector<Session> {
  @Nonnull
  public static Class<? extends Dialect> getDialect( @Nonnull DatabaseConfiguration configuration ) {
    switch ( configuration.getDatabaseType() ) {
      case MYSQL:
        return MySQLDialect.class;
      case FIREBIRD:
        return FirebirdDialect.class;
    }
    throw new IllegalArgumentException( "Did not find a valid Dialect for type " + configuration.getDatabaseType() );
  }

  private SessionFactory sessionFactory;
  private AnnotationConfiguration annotationConfiguration;
  private DatabaseConfiguration configuration;
  private Transaction transaction;
  private Session session;

  public HibernateDatabaseConnector( DatabaseConfiguration configuration ) {
    this.configuration = configuration;
    if ( !configuration.isValid() ) {
      throw new IllegalArgumentException( "DatabaseConfiguration is not valid: " + configuration );
    }
    annotationConfiguration = new AnnotationConfiguration();

    annotationConfiguration.setProperty( Environment.USER, configuration.getUser() );
    annotationConfiguration.setProperty( Environment.PASS, configuration.getPass() );
    annotationConfiguration.setProperty( Environment.URL, configuration.getUrl() );
    annotationConfiguration.setProperty( Environment.DRIVER, configuration.getDriverClassName() );
    //annotationConfiguration.setProperty( Environment.SHOW_SQL, "false" );

    annotationConfiguration.setProperty( Environment.DIALECT, getDialect( configuration ).getName() );
  }

  public void beginTransaction() throws DatabaseException {
    transaction = getSession().beginTransaction();
  }

  public void closeSession() {
    if ( session == null ) {
      return;
    }
    session.close();
    //noinspection AssignmentToNull
    session = null;
  }

  public void commit() {
    if ( transaction == null || !transaction.isActive() ) {
      throw new IllegalStateException( "Start transaction first" );
    }
    transaction.commit();
  }

  public void delete( @Nonnull Object object ) {
    getSession().delete( object );
  }

  @Nonnull
  public <T> T load( @Nonnull Class<T> aClass, Serializable id ) {
    return aClass.cast( getSession().load( aClass, id ) );
  }

  public void rollback() {
    if ( transaction != null ) {
      transaction.rollback();
    }
  }

  public void update( @Nonnull Object object ) {
    getSession().update( object );
  }

  public void saveOrUpdate( @Nonnull Object object ) {
    getSession().saveOrUpdate( object );
  }

  public void save( @Nonnull Object object ) {
    getSession().save( object );
  }

  public void saveChanges( @Nonnull Object object ) {
    getSession().update( object );
  }

  public boolean isSessionActive() {
    return session != null && session.isOpen();
  }

  public void dispose() {
    if ( session != null ) {
      session.close();
    }
    if ( sessionFactory != null ) {
      sessionFactory.close();
    }
  }

  @Nonnull
  public Session getSession() {
    if ( session == null ) {
      initSession();
    }
    return session;
  }

  public void initSession() {
    openSession();
  }

  public void refresh( @Nonnull Object object ) {
    getSession().refresh( object );
  }

  public void addAllAnnotdedClass( @Nonnull List<Class<?>> persistentClasses ) {
    for ( Class<?> aClass : persistentClasses ) {
      addAnnotadedClass( aClass );
    }
  }

  public void addAnnotadedClass( @Nonnull Class<?> annClass ) {
    annotationConfiguration.addAnnotatedClass( annClass );
  }

  @Nonnull
  public Session openSession() throws HibernateException {
    if ( sessionFactory == null ) {
      sessionFactory = annotationConfiguration.buildSessionFactory();
    }

    session = sessionFactory.openSession();
    if ( session == null ) {
      throw new DatabaseException( "could not create session" );
    }
    return session;
  }

  public void recreateDb() {
    try {
      dropTables();
      createTables();
    } catch ( Exception e ) {
      throw new RuntimeException( "Could not recreate db/tables due to: " + e.getMessage(), e );
    }
  }

  private void dropTables() throws SQLException, ClassNotFoundException {
    Class.forName( configuration.getDriverClassName() );
    Connection connection = null;
    try {
      connection = DriverManager.getConnection( configuration.getUrl(), configuration.getUser(), configuration.getPass() );

      for ( String sql : getDrop() ) {
        Statement statement = connection.createStatement();
        //noinspection UnusedCatchParameter,EmptyCatchBlock
        try {
          statement.execute( sql );
        } catch ( SQLException e ) {
        }
        statement.close();
      }
    } finally {
      if ( connection != null ) {
        connection.close();
      }
    }
  }

  public String[] getDrop() {
    return annotationConfiguration.generateDropSchemaScript( new MySQLDialect() );
  }

  private void createTables() throws ClassNotFoundException, SQLException {
    Class.forName( configuration.getDriverClassName() );
    Connection connection = null;
    try {
      connection = DriverManager.getConnection( configuration.getUrl(), configuration.getUser(), configuration.getPass() );

      for ( String sql : getCreation() ) {
        Statement statement = connection.createStatement();

        //noinspection JDBCExecuteWithNonConstantString
        statement.execute( sql );
        statement.close();
      }
    } finally {
      if ( connection != null ) {
        connection.close();
      }
    }
  }

  public String[] getCreation() {
    try {
      return annotationConfiguration.generateSchemaCreationScript( getDialect( configuration ).newInstance() );
    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  public boolean verifyConnection() throws SQLException {
    Connection connection = null;

    try {
      Class.forName( configuration.getDriverClassName() );
      connection = DriverManager.getConnection( configuration.getUrl(), configuration.getUser(), configuration.getPass() );
      return true;
    } catch ( Exception e ) {
      e.printStackTrace();
      return false;
    } finally {
      if ( connection != null ) {
        connection.close();
      }
    }
  }


}

