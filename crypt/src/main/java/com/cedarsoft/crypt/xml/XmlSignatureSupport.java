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

package com.cedarsoft.crypt.xml;

import com.cedarsoft.crypt.X509Support;
import javax.inject.Inject;

import javax.annotation.Nonnull;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.SignatureMethod;
import javax.xml.crypto.dsig.SignedInfo;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMSignContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.Collections;

/**
 * Easy support for xml signatures.
 * Be carefull! The {@link DocumentBuilderFactory} has to be namespace aware
 * ({@link DocumentBuilderFactory#setNamespaceAware(boolean)}).
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class XmlSignatureSupport {
  @Nonnull
  private static final XMLSignatureFactory SIGNATURE_FACTORY = XMLSignatureFactory.getInstance( "DOM" );

  @Nonnull
  private final X509Support x509Support;

  /**
   * <p>Constructor for XmlSignatureSupport.</p>
   *
   * @param x509Support a {@link X509Support} object.
   */
  @Inject
  public XmlSignatureSupport( @Nonnull X509Support x509Support ) {
    this.x509Support = x509Support;
  }

  /**
   * <p>sign</p>
   *
   * @param xmlDocument a {@link Document} object.
   * @return a {@link Document} object.
   */
  public Document sign( @Nonnull Document xmlDocument ) {
    try {
      @Nonnull String elementName = xmlDocument.getFirstChild().getNodeName();
      Reference ref = SIGNATURE_FACTORY.newReference( '#' + elementName, SIGNATURE_FACTORY.newDigestMethod( DigestMethod.SHA256, null ) );

      Node invoice = xmlDocument.getDocumentElement();
      XMLStructure content = new DOMStructure( invoice );
      XMLObject obj = SIGNATURE_FACTORY.newXMLObject( Collections.singletonList( content ), elementName, null, null );

      SignedInfo si = SIGNATURE_FACTORY.newSignedInfo( SIGNATURE_FACTORY.newCanonicalizationMethod( CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS, ( C14NMethodParameterSpec ) null ),
                                                       SIGNATURE_FACTORY.newSignatureMethod( SignatureMethod.RSA_SHA1, null ), Collections.singletonList( ref ) );

      XMLSignature signature = SIGNATURE_FACTORY.newXMLSignature( si, null, Collections.singletonList( obj ), null, null );

      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware( true );
      Document signedDoc = documentBuilderFactory.newDocumentBuilder().newDocument();
      DOMSignContext dsc = new DOMSignContext( x509Support.getPrivateKey(), signedDoc );

      signature.sign( dsc );

      return signedDoc;
    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
  }

  /**
   * <p>hasValidSignature</p>
   *
   * @param doc a {@link Document} object.
   * @return a boolean.
   *
   * @throws Exception if any.
   */
  public boolean hasValidSignature( @Nonnull Document doc ) throws Exception {
    NodeList nl = doc.getElementsByTagNameNS( XMLSignature.XMLNS, "Signature" );
    if ( nl.getLength() == 0 ) {
      throw new IllegalStateException( "Cannot find Signature element!" );
    }

    DOMValidateContext valContext = new DOMValidateContext( x509Support.getCertificate().getPublicKey(), nl.item( 0 ) );
    XMLSignature signature = SIGNATURE_FACTORY.unmarshalXMLSignature( valContext );
    return signature.validate( valContext );

    //    if ( signature.validate( valContext ) ) {
    //      System.out.println( "Signature passed core validation!" );
    //    } else {
    //      System.err.println( "Signature failed core validation!" );
    //      boolean sv = signature.getSignatureValue().validate( valContext );
    //      System.out.println( "Signature validation status: " + sv );
    //      // Check the validation status of each Reference
    //      Iterator<?> i = signature.getSignedInfo().getReferences().iterator();
    //      int j = 0;
    //
    //      while ( i.hasNext() ) {
    //        boolean refValid = ( ( Reference ) i.next() ).validate( valContext );
    //        System.out.println( "Reference (" + j + ") validation status: " + refValid );
    //        j++;
    //      }
    //    }
  }

  /**
   * Returns the node that is the root node of the original signedDocument
   *
   * @param signedDocument the signed signedDocument
   * @return the root node of the original signedDocument
   */
  @Nonnull
  public Node getOriginalNode( @Nonnull Document signedDocument ) {
    NodeList nl = signedDocument.getElementsByTagNameNS( XMLSignature.XMLNS, "Object" );
    if ( nl.getLength() == 0 ) {
      throw new IllegalStateException( "Cannot find Object element!" );
    }

    Node objectNode = nl.item( 0 );
    return objectNode.getFirstChild();
  }

  /**
   * <p>getOriginalDocument</p>
   *
   * @param signedDocument a {@link Document} object.
   * @return a {@link Document} object.
   */
  @Nonnull
  public Document getOriginalDocument( @Nonnull Document signedDocument ) {
    try {
      Node node = getOriginalNode( signedDocument );

      DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
      documentBuilderFactory.setNamespaceAware( true );
      Document originalDoc = documentBuilderFactory.newDocumentBuilder().newDocument();
      originalDoc.appendChild( originalDoc.adoptNode( node ) );
      return originalDoc;
    } catch ( ParserConfigurationException e ) {
      throw new RuntimeException( e );
    }
  }
}
