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

package com.cedarsoft.crypt;

import org.apache.commons.io.IOUtils;

import javax.annotation.Nonnull;

import javax.annotation.Nullable;
import javax.crypto.Cipher;
import javax.inject.Inject;
import java.io.DataInputStream;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.Signature;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

/**
 * X509 Support
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class X509Support {
  @Nonnull
  private static final String RSA = "RSA";
  @Nonnull
  private static final String SHA_256_WITH_RSA = "SHA256withRSA";
  @Nonnull
  private static final String X_509_CERTIFICATE_TYPE = "X.509";

  @Nonnull
  private final X509Certificate certificate;
  @Nullable
  private final RSAPrivateKey privateKey;

  /**
   * Creates a new X509 support without any signing capabilities
   *
   * @param certificate the certificate
   * @throws IOException if any.
   * @throws GeneralSecurityException
   *                             if any.
   */
  public X509Support( @Nonnull URL certificate ) throws IOException, GeneralSecurityException {
    this( certificate, null );
  }

  /**
   * Creates a new X509 support
   *
   * @param certificate the certificate
   * @param privateKey  the private key (if available)
   * @throws IOException if any.
   * @throws GeneralSecurityException
   *                             if any.
   */
  @Inject
  public X509Support( @CertificateUrl @Nonnull URL certificate, @PrivateKeyUrl @Nullable URL privateKey ) throws IOException, GeneralSecurityException {
    this( readCertificate( certificate ), readPrivateKey( privateKey ) );
  }

  /**
   * Creates a new x509 support
   *
   * @param certificate the certificate
   */
  public X509Support( @Nonnull X509Certificate certificate ) {
    this( certificate, null );
  }

  /**
   * Creates a new x509 support
   *
   * @param certificate the certificate
   * @param privateKey  the (optional) private key
   */
  public X509Support( @Nonnull X509Certificate certificate, @Nullable RSAPrivateKey privateKey ) {
    this.certificate = certificate;
    this.privateKey = privateKey;
  }

  /**
   * Returns whether the private key is available
   *
   * @return whether the private key is available
   */
  public boolean isPrivateKeyAvailable() {
    return privateKey != null;
  }

  /**
   * <p>cipher</p>
   *
   * @param plainText an array of byte.
   * @return an array of byte.
   *
   * @throws GeneralSecurityException
   *          if any.
   */
  @Nonnull
  public byte[] cipher( @Nonnull byte[] plainText ) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance( RSA );
    cipher.init( Cipher.ENCRYPT_MODE, getPrivateKey() );
    return cipher.doFinal( plainText );
  }

  /**
   * <p>decipher</p>
   *
   * @param bytes an array of byte.
   * @return an array of byte.
   *
   * @throws GeneralSecurityException
   *          if any.
   */
  @Nonnull
  public byte[] decipher( @Nonnull byte[] bytes ) throws GeneralSecurityException {
    Cipher cipher = Cipher.getInstance( RSA );
    cipher.init( Cipher.DECRYPT_MODE, certificate );
    return cipher.doFinal( bytes );
  }

  /**
   * <p>sign</p>
   *
   * @param plainText an array of byte.
   * @return a {@link com.cedarsoft.crypt.Signature} object.
   *
   * @throws GeneralSecurityException
   *          if any.
   */
  @Nonnull
  public com.cedarsoft.crypt.Signature sign( @Nonnull byte[] plainText ) throws GeneralSecurityException {
    Signature signature = Signature.getInstance( SHA_256_WITH_RSA );
    signature.initSign( getPrivateKey() );

    signature.update( plainText );
    return new com.cedarsoft.crypt.Signature( signature.sign() );
  }

  /**
   * <p>verifySignature</p>
   *
   * @param plainText an array of byte.
   * @param signature a {@link com.cedarsoft.crypt.Signature} object.
   * @return a boolean.
   *
   * @throws GeneralSecurityException
   *          if any.
   */
  public boolean verifySignature( @Nonnull byte[] plainText, @Nonnull com.cedarsoft.crypt.Signature signature ) throws GeneralSecurityException {
    Signature sign = Signature.getInstance( SHA_256_WITH_RSA );
    sign.initVerify( certificate );
    sign.update( plainText );
    return sign.verify( signature.getBytes() );
  }

  /**
   * Returns the certificate
   *
   * @return the certificate
   */
  @Nonnull
  public X509Certificate getCertificate() {
    return certificate;
  }

  /**
   * Returns the private key (if there is one)
   *
   * @return the private key
   */
  @Nonnull
  public RSAPrivateKey getPrivateKey() {
    if ( privateKey == null ) {
      throw new IllegalStateException( "Private key not avaible" );
    }
    return privateKey;
  }

  /**
   * Reads a private key form a url
   *
   * @param privateKeyUrl the url containing the private key
   * @return the read private key
   *
   * @throws IOException if any.
   * @throws GeneralSecurityException
   *                             if any.
   */
  @Nullable
  public static RSAPrivateKey readPrivateKey( @Nullable URL privateKeyUrl ) throws IOException, GeneralSecurityException {
    //If a null url is given - just return null
    if ( privateKeyUrl == null ) {
      return null;
    }

    //We have an url --> return it
    DataInputStream in = new DataInputStream( privateKeyUrl.openStream() );
    try {
      byte[] keyBytes = IOUtils.toByteArray( in );
      KeyFactory keyFactory = KeyFactory.getInstance( RSA );

      PKCS8EncodedKeySpec privSpec = new PKCS8EncodedKeySpec( keyBytes );
      return ( RSAPrivateKey ) keyFactory.generatePrivate( privSpec );
    } finally {
      in.close();
    }
  }

  /**
   * Reads the x509 certificate from the given url
   *
   * @param certificateUrl the certificate url
   * @return the certificate
   *
   * @throws IOException if any.
   * @throws GeneralSecurityException
   *                             if any.
   */
  @Nonnull
  public static X509Certificate readCertificate( @Nonnull URL certificateUrl ) throws IOException, GeneralSecurityException {
    //Read the cert
    DataInputStream in = new DataInputStream( certificateUrl.openStream() );
    try {
      CertificateFactory cf = CertificateFactory.getInstance( X_509_CERTIFICATE_TYPE );
      X509Certificate certificate = ( X509Certificate ) cf.generateCertificate( in );
      certificate.checkValidity();
      return certificate;
    } finally {
      in.close();
    }
  }
}
