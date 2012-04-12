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

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import static org.junit.Assert.*;

/**
 *
 */
public class XmlSignatureSupportTest {
  private X509Support x509Support;
  private XmlSignatureSupport signatureSupport;

  @Before
  public void setUp() throws Exception {
    x509Support = new X509Support( getClass().getResource( "/test.crt" ), getClass().getResource( "/test.der" ) );
    signatureSupport = new XmlSignatureSupport( x509Support );
  }

  @Test
  public void testIt() {

  }

  @Test
  public void testSign() throws Exception {
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    builderFactory.setNamespaceAware( true );
    Document doc = builderFactory.newDocumentBuilder().parse( new ByteArrayInputStream( XmlSignTest.UNSIGNED.getBytes() ) );

    Document signedDocument = signatureSupport.sign( doc );

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    TransformerFactory.newInstance().newTransformer().transform( new DOMSource( signedDocument ), new StreamResult( out ) );

    assertEquals( XmlSignTest.SIGNED, new String( out.toByteArray() ) );
    assertEquals( XmlSignTest.SIGNED, out.toString() );
  }

  @Test
  public void testVerify() throws Exception {
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    builderFactory.setNamespaceAware( true );
    Document doc = builderFactory.newDocumentBuilder().parse( new ByteArrayInputStream( XmlSignTest.SIGNED.getBytes() ) );
    assertTrue( signatureSupport.hasValidSignature( doc ) );
  }

  @Test
  public void testGetDocument() throws Exception {
    DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
    builderFactory.setNamespaceAware( true );

    Document doc = builderFactory.newDocumentBuilder().parse( new ByteArrayInputStream( XmlSignTest.SIGNED.getBytes() ) );

    Node originalNode = signatureSupport.getOriginalNode( doc );
    assertNotNull( originalNode );
    assertEquals( "invoice", originalNode.getNodeName() );

    Document original = signatureSupport.getOriginalDocument( doc );
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    TransformerFactory.newInstance().newTransformer().transform( new DOMSource( original ), new StreamResult( out ) );

    assertEquals( NEW_UNSINGED, new String( out.toByteArray() ) );
  }

  public static final String NEW_UNSINGED = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><invoice xmlns=\"http://www.company.com/accounting\">\n" +
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
    "</invoice>";
}
