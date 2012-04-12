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

package com.cedarsoft.configuration.xstream;

import com.cedarsoft.configuration.ConfigurationManager;
import com.cedarsoft.configuration.ConfigurationPersister;
import com.thoughtworks.xstream.XStream;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.String;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Persists a configuration using XStream
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class XStreamPersister implements ConfigurationPersister {
  @Nonnull
  private static final String XML_HEADER_BEGIN = "<?xml version=\"1.0\"";
  @Nonnull
  private static final String XML_ENCODING_BEGIN = " encoding=\"";
  @Nonnull
  private static final String XML_ENCODING_SUFFIX = "\"";
  @Nonnull
  private static final String XML_HEADER_SUFFIX = "?>";

  @Nonnull
  private final XStream xStream;
  @Nonnull
  private static final Charset DEFAULT_CHARSET = Charset.forName( "UTF-8" );

  /**
   * Creates a new persister
   */
  public XStreamPersister() {
    this.xStream = new XStream();
    xStream.alias( "configurations", Configurations.class );
    xStream.addImplicitCollection( Configurations.class, "configurations" );
  }

  /**
   * {@inheritDoc}
   * <p/>
   * Persists the given configuration manager
   */
  @Override
  public void persist( @Nonnull ConfigurationManager configurationManager, @Nonnull Writer out ) throws IOException {
    persist( configurationManager, out, null );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void persist( @Nonnull ConfigurationManager configurationManager, @Nonnull Writer out, @Nullable String encoding ) throws IOException {
    persist( configurationManager.getConfigurations(), out, encoding );
  }

  /**
   * {@inheritDoc}
   * <p/>
   * Persists the given configurations
   */
  @Override
  public void persist( @Nonnull List<?> configurations, @Nonnull Writer out ) throws IOException {
    persist( configurations, out, null );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void persist( @Nonnull List<?> configurations, @Nonnull Writer out, @Nullable String encoding ) throws IOException {
    out.write( createXmlHeader( encoding ) );
    out.write( "\n" );
    xStream.toXML( new Configurations( configurations ), out );
  }

  /**
   * <p>createXmlHeader</p>
   *
   * @param encoding a {@link String} object.
   * @return a {@link String} object.
   */
  @Nonnull
  protected String createXmlHeader( @Nullable String encoding ) {
    if ( encoding == null ) {
      return XML_HEADER_BEGIN + XML_HEADER_SUFFIX;
    } else {
      return XML_HEADER_BEGIN + XML_ENCODING_BEGIN + encoding + XML_ENCODING_SUFFIX + XML_HEADER_SUFFIX;
    }
  }

  /**
   * {@inheritDoc}
   * <p/>
   * Persists the configurations for the given manager into a string
   */
  @Deprecated
  @Override
  @Nonnull
  public String persist( @Nonnull ConfigurationManager manager ) throws IOException {
    StringWriter out = new StringWriter();
    persist( manager.getConfigurations(), out );
    return out.toString();
  }

  /**
   * {@inheritDoc}
   * <p/>
   * Loads the configurations
   */
  @Override
  @Nonnull
  public List<?> load( @Nonnull String serialized ) throws IOException {
    StringReader reader = new StringReader( serialized );
    return load( reader );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<?> load( @Nonnull File file ) throws IOException {
    BufferedInputStream in = new BufferedInputStream( new FileInputStream( file ) );
    try {
      return load( in, DEFAULT_CHARSET );
    } finally {
      in.close();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<?> load( @Nonnull BufferedInputStream in, @Nonnull Charset charset ) throws IOException {
    //noinspection IOResourceOpenedButNotSafelyClosed
    return load( ( Reader ) new InputStreamReader( in, charset ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<?> load( @Nonnull Reader in ) throws IOException {
    //noinspection IOResourceOpenedButNotSafelyClosed
    BufferedReader br = new BufferedReader( in );
    String firstLine = br.readLine().trim();//xml

    if ( !firstLine.startsWith( XML_HEADER_BEGIN ) || !firstLine.endsWith( XML_HEADER_SUFFIX ) ) {
      throw new IllegalStateException( "Invalid start of file. Was <" + firstLine + "> but exepcted something like <" + XML_HEADER_BEGIN + XML_HEADER_SUFFIX + '>' );
    }

    return ( ( Configurations ) xStream.fromXML( br ) ).getConfigurations();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void persist( @Nonnull ConfigurationManager manager, @Nonnull File file ) throws IOException {
    OutputStream out = new BufferedOutputStream( new FileOutputStream( file ) );
    try {
      persist( manager, out, DEFAULT_CHARSET );
    } finally {
      out.close();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void persist( @Nonnull ConfigurationManager manager, @Nonnull OutputStream out, @Nonnull Charset charset ) throws IOException {
    //noinspection IOResourceOpenedButNotSafelyClosed
    Writer writer = new OutputStreamWriter( out, charset );
    persist( manager, writer, charset.name() );
    writer.flush();
  }


  private static class Configurations {
    @Nonnull
    private final List<Object> configurations = new ArrayList<Object>();

    private Configurations( @Nonnull List<?> configurations ) {
      this.configurations.addAll( configurations );
    }

    @Nonnull
    public List<?> getConfigurations() {
      return Collections.unmodifiableList( configurations );
    }
  }
}
