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

package com.cedarsoft.xml;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import javax.annotation.Nonnull;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;

/**
 * Common xml methods
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class XmlCommons {
  private XmlCommons() {
  }

  /**
   * <p>format</p>
   *
   * @param xml a {@link String} object.
   * @return a {@link String} object.
   */
  @Nonnull
  public static String format( @Nonnull String xml ) {
    if (xml.trim().isEmpty()) {
      return "";
    }

    try {
      Source xmlInput = new StreamSource(new StringReader(xml));
      StringWriter stringWriter = new StringWriter();

      createTransformer().transform( xmlInput, new StreamResult( stringWriter ) );
      return stringWriter.toString();
    } catch (TransformerConfigurationException e) {
      throw new RuntimeException(e);
    } catch (TransformerException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * <p>getDocumentBuilder</p>
   *
   * @return a {@link DocumentBuilder} object.
   */
  @Nonnull
  public static DocumentBuilder getDocumentBuilder() {
    try {
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      factory.setNamespaceAware( true );
      return factory.newDocumentBuilder();
    } catch ( ParserConfigurationException e ) {
      throw new RuntimeException( e );
    }
  }

  /**
   * <p>out</p>
   *
   * @param document a {@link Document} object.
   * @param out      a {@link OutputStream} object.
   */
  public static void out( @Nonnull Document document, @Nonnull OutputStream out ) {
    try {
      createTransformer().transform( new DOMSource( document ), new StreamResult( out ) );
    } catch ( TransformerException e ) {
      throw new RuntimeException( e );
    }
  }

  /**
   * <p>out</p>
   *
   * @param document a {@link Document} object.
   * @param out      a {@link Writer} object.
   */
  public static void out( @Nonnull Document document, @Nonnull Writer out ) {
    try {
      createTransformer().transform( new DOMSource( document ), new StreamResult( out ) );
    } catch ( TransformerException e ) {
      throw new RuntimeException( e );
    }
  }

  @Nonnull
  public static Transformer createTransformer() throws TransformerConfigurationException {
    TransformerFactory transformerFactory = TransformerFactory.newInstance();
    transformerFactory.setAttribute( "indent-number", 2 ); //Used in some XML implementations

    Transformer transformer = transformerFactory.newTransformer();
    transformer.setOutputProperty( OutputKeys.INDENT, "yes" );
    transformer.setOutputProperty( "{http://xml.apache.org/xslt}indent-amount", "2" ); //Used for XALAN(?)

    return transformer;
  }

  /**
   * <p>parse</p>
   *
   * @param bytes an array of byte.
   * @return a {@link Document} object.
   *
   * @throws IOException  if any.
   * @throws SAXException if any.
   */
  @Nonnull
  public static Document parse( @Nonnull byte[] bytes ) throws IOException, SAXException {
    return parse( new ByteArrayInputStream( bytes ) );
  }

  /**
   * <p>parse</p>
   *
   * @param in a {@link InputStream} object.
   * @return a {@link Document} object.
   *
   * @throws IOException  if any.
   * @throws SAXException if any.
   */
  @Nonnull
  public static Document parse( @Nonnull InputStream in ) throws IOException, SAXException {
    return getDocumentBuilder().parse( in );
  }

  /**
   * <p>toString</p>
   *
   * @param document a {@link Document} object.
   * @return a {@link String} object.
   */
  @Nonnull
  public static String toString( @Nonnull Document document ) {
    StringWriter stringWriter = new StringWriter();
    out( document, stringWriter );
    return stringWriter.toString();
  }
}
