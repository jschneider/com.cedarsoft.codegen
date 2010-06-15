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

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

/**
 * <p/>
 * Date: 21.06.2006<br>
 * Time: 23:19:52<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class MailConfiguration {
  @NotNull
  @NonNls
  private final String mailFrom;
  @NotNull
  @NonNls
  private final String mailHost;

  @NotNull
  @NonNls
  private final String smtpUser;
  @NotNull
  @NonNls
  private final String smtpPass;

  @NotNull
  @NonNls
  private final String mailPersonal;
  @NotNull
  @NonNls
  private final String mailSmtpAuth;


  /**
   * <p>Constructor for MailConfiguration.</p>
   *
   * @param mailHost a {@link java.lang.String} object.
   * @param mailFrom a {@link java.lang.String} object.
   * @param mailPersonal a {@link java.lang.String} object.
   * @param smtpUser a {@link java.lang.String} object.
   * @param smtpPass a {@link java.lang.String} object.
   * @param mailSmtpAuth a {@link java.lang.String} object.
   */
  public MailConfiguration( @NonNls @NotNull String mailHost, @NonNls @NotNull String mailFrom, @NonNls @NotNull String mailPersonal, @NonNls @NotNull String smtpUser, @NonNls @NotNull String smtpPass, @NonNls @NotNull String mailSmtpAuth ) {
    this.mailHost = mailHost;
    this.mailFrom = mailFrom;
    this.mailPersonal = mailPersonal;
    this.smtpUser = smtpUser;
    this.smtpPass = smtpPass;
    this.mailSmtpAuth = mailSmtpAuth;
  }

  /**
   * <p>Getter for the field <code>mailFrom</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  @NonNls
  @NotNull
  public String getMailFrom() {
    return mailFrom;
  }

  /**
   * <p>Getter for the field <code>mailHost</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  @NonNls
  @NotNull
  public String getMailHost() {
    return mailHost;
  }

  /**
   * <p>Getter for the field <code>smtpUser</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  @NonNls
  @NotNull
  public String getSmtpUser() {
    return smtpUser;
  }

  /**
   * <p>Getter for the field <code>smtpPass</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  @NonNls
  @NotNull
  public String getSmtpPass() {
    return smtpPass;
  }

  /**
   * <p>Getter for the field <code>mailPersonal</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  @NonNls
  @NotNull
  public String getMailPersonal() {
    return mailPersonal;
  }

  /**
   * <p>Getter for the field <code>mailSmtpAuth</code>.</p>
   *
   * @return a {@link java.lang.String} object.
   */
  @NonNls
  @NotNull
  public String getMailSmtpAuth() {
    return mailSmtpAuth;
  }
}
