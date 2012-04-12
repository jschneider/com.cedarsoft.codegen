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
import org.junit.*;
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
import javax.xml.crypto.dsig.keyinfo.KeyInfo;
import javax.xml.crypto.dsig.spec.C14NMethodParameterSpec;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.security.PrivateKey;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.*;


/**
 *
 */
public class XmlSignTest {
  private X509Support x509Support;

  @Before
  public void setUp() throws Exception {
    x509Support = new X509Support( getClass().getResource( "/test.crt" ), getClass().getResource( "/test.der" ) );
  }

  @Test
  public void testSign() throws Exception {
    // Prepare
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware( true );

    XMLSignatureFactory fac = XMLSignatureFactory.getInstance( "DOM" );

    // Step 2
    Reference ref = fac.newReference( "#invoice", fac.newDigestMethod( DigestMethod.SHA256, null ) );

    // Step 3
    Document xmlDocument = dbf.newDocumentBuilder().parse( new ByteArrayInputStream( UNSIGNED.getBytes() ) );
    Node invoice = xmlDocument.getDocumentElement();
    XMLStructure content = new DOMStructure( invoice );
    XMLObject xmlObject = fac.newXMLObject( Collections.singletonList( content ), "invoice", null, null );

    // Step 4
    SignedInfo signedInfo = fac.newSignedInfo( fac.newCanonicalizationMethod( CanonicalizationMethod.INCLUSIVE_WITH_COMMENTS, ( C14NMethodParameterSpec ) null ),
                                               fac.newSignatureMethod( SignatureMethod.RSA_SHA1, null ), Collections.singletonList( ref ) );

    PrivateKey privateKey = x509Support.getPrivateKey();

    KeyInfo keyInfo = null;
    XMLSignature signature = fac.newXMLSignature( signedInfo, keyInfo, Collections.singletonList( xmlObject ), null, null );

    // Step 8
    Document doc = dbf.newDocumentBuilder().newDocument();
    DOMSignContext signContext = new DOMSignContext( privateKey, doc );

    // Step 9
    signature.sign( signContext );

    // Materialize into an xml document
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    TransformerFactory.newInstance().newTransformer().transform( new DOMSource( doc ), new StreamResult( out ) );

    assertEquals( SIGNED, new String( out.toByteArray() ) );
  }

  @Test
  public void testValidate() throws Exception {
    // Step 1
    XMLSignatureFactory fac = XMLSignatureFactory.getInstance( "DOM" );

    // Step 2
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    dbf.setNamespaceAware( true );
    Document doc = dbf.newDocumentBuilder().parse( new ByteArrayInputStream( SIGNED.getBytes() ) );

    // Step 3
    NodeList nl = doc.getElementsByTagNameNS( XMLSignature.XMLNS, "Signature" );
    if ( nl.getLength() == 0 ) {
      throw new Exception( "Cannot find Signature element!" );
    }

    // Scenario 4.0
    DOMValidateContext valContext = new DOMValidateContext( x509Support.getCertificate().getPublicKey(), nl.item( 0 ) );

    XMLSignature signature = fac.unmarshalXMLSignature( valContext );
    //XMLSignature signature = fac.unmarshalXMLSignature(new DOMStructure(nl.item(0)));

    // Step 6
    // Check core validation status
    if ( signature.validate( valContext ) ) {
      System.out.println( "Signature passed core validation!" );
    } else {
      System.err.println( "Signature failed core validation!" );
      boolean sv = signature.getSignatureValue().validate( valContext );
      System.out.println( "Signature validation status: " + sv );
      // Check the validation status of each Reference
      Iterator<?> i = signature.getSignedInfo().getReferences().iterator();
      int j = 0;

      while ( i.hasNext() ) {
        boolean refValid = ( ( Reference ) i.next() ).validate( valContext );
        System.out.println( "Reference (" + j + ") validation status: " + refValid );
        j++;
      }
    }

    assertTrue( signature.validate( valContext ) );
  }

  public static final String SIGNED = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><Signature xmlns=\"http://www.w3.org/2000/09/xmldsig#\"><SignedInfo><CanonicalizationMethod Algorithm=\"http://www.w3.org/TR/2001/REC-xml-c14n-20010315#WithComments\"/><SignatureMethod Algorithm=\"http://www.w3.org/2000/09/xmldsig#rsa-sha1\"/><Reference URI=\"#invoice\"><DigestMethod Algorithm=\"http://www.w3.org/2001/04/xmlenc#sha256\"/><DigestValue>FQjnB+kEsz8RPrCiQG/yHrClkcRcMr7e3bLUYlEVqY8=</DigestValue></Reference></SignedInfo><SignatureValue>bjAeHa8fX+xXmlb6DVya8luyRRLF/G2j0h3mWnGeNSZFp+qSchDbe5r9vdJBFQSNs1qthOqi1ree\n" +
    "5XtQdddstxgj4Z6+EVNKTXSTeNA0QzRRY+U3izrFjAB3QgzHL/E+LaB6wF7nJ9TzOpb223bgmQAV\n" +
    "mkdbqqwtCEqmXb1REP4=</SignatureValue><Object Id=\"invoice\"><invoice xmlns=\"http://www.company.com/accounting\">\n" +
    "  <items>\n" +
    "    <item>\n" +
    "      <desc>Applied Cryptography</desc>\n" +
    "      <type>book</type>\n" +
    "      <unitprice>44.50</unitprice>\n" +
    "      <quantity>1</quantity>\n" +
    "    </item>\n" +
    "  </items>\n" +
    "  <creditcard>\n" +
    "    <number>123456789</number>\n" +
    "    <expiry>10/20/2009</expiry>\n" +
    "    <lastname>John</lastname>\n" +
    "    <firstname>Smith</firstname>\n" +
    "  </creditcard>\n" +
    "</invoice></Object></Signature>";

  public static final String UNSIGNED = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
    "<invoice xmlns=\"http://www.company.com/accounting\">\n" +
    "  <items>\n" +
    "    <item>\n" +
    "      <desc>Applied Cryptography</desc>\n" +
    "      <type>book</type>\n" +
    "      <unitprice>44.50</unitprice>\n" +
    "      <quantity>1</quantity>\n" +
    "    </item>\n" +
    "  </items>\n" +
    "  <creditcard>\n" +
    "    <number>123456789</number>\n" +
    "    <expiry>10/20/2009</expiry>\n" +
    "    <lastname>John</lastname>\n" +
    "    <firstname>Smith</firstname>\n" +
    "  </creditcard>\n" +
    "</invoice>\n";
}
