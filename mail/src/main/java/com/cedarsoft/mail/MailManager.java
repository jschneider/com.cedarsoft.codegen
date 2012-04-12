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

package com.cedarsoft.mail;

import org.apache.log4j.Category;
import org.apache.log4j.Logger;

import javax.annotation.Nonnull;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * <p/>
 * Date: 05.05.2004<br> Time: 20:59:09<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public class MailManager {
  @Nonnull
  private static final Category CAT = Logger.getLogger( MailManager.class );
  @Nonnull
  private static final String MAIL_HOST = "mail.host";
  @Nonnull
  private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";


  private Session session;

  private MailConfiguration configuration;

  /**
   * <p>Constructor for MailManager.</p>
   *
   * @param mailConfiguration a {@link MailConfiguration} object.
   */
  public MailManager( @Nonnull MailConfiguration mailConfiguration ) {
    this.configuration = mailConfiguration;
    session = getSession();
  }

  /**
   * Returns the session
   *
   * @return the session
   */
  @Nonnull
  protected final Session getSession() {
    return Session.getInstance( getProperties(), getAuthenticator() );
  }

  /**
   * <p>getAuthenticator</p>
   *
   * @return a {@link Authenticator} object.
   */
  @Nonnull
  public Authenticator getAuthenticator() {
    return new Authenticator() {
      @Override
      protected PasswordAuthentication getPasswordAuthentication() {
        return new PasswordAuthentication( configuration.getSmtpUser(), configuration.getSmtpPass() );
      }
    };
  }

  /**
   * Erstellt eine Default-Nachricht mitentsprechendem Absender.
   *
   * @return a new mime message
   */
  @Nonnull
  public MimeMessage createMessage() {
    MimeMessage message = new MimeMessage( session );
    try {
      InternetAddress from = new InternetAddress( configuration.getMailFrom() );
      from.setPersonal( configuration.getMailPersonal() );
      message.setFrom( from );
    } catch ( Exception e ) {
      CAT.error( "An error occured while creating message", e );
    }
    return message;
  }


  /**
   * <p>sendMail</p>
   *
   * @param message a {@link Message} object.
   */
  public static void sendMail( @Nonnull Message message ) {
    new Thread( new MailWorker( message ) ).start();
  }

  @Nonnull
  private Properties getProperties() {
    Properties properties = new Properties();
    properties.setProperty( MAIL_HOST, configuration.getMailHost() );
    properties.setProperty( MAIL_SMTP_AUTH, configuration.getMailSmtpAuth() );
    return properties;
  }

  /**
   * The mail worker
   */
  private static class MailWorker implements Runnable {
    private Message message;

    protected MailWorker( @Nonnull Message message ) {
      this.message = message;
    }

    @Override
    public void run() {
      try {
        Transport.send( message );
      } catch ( Exception e ) {
        CAT.error( "Error occured on message sending", e );
      }
    }
  }
}

