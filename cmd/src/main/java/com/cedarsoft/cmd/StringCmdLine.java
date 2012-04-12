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

package com.cedarsoft.cmd;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.Object;
import java.lang.String;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Command Line implementation that is based on strings.
 * <p/>
 * Date: 25.09.2006<br>
 * Time: 19:43:04<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class StringCmdLine extends AbstractCmdLine implements CmdLine {
  private int expectedIndex;
  private List<String> expectedOut = new ArrayList<String>();
  private List<Object> answers = new ArrayList<Object>();
  private ConsolePrinter consolePrinter = new SimpleConsolePrinter();

  private void checkExpectedOut( @Nonnull Object given ) {
    if ( expectedOut.size() <= expectedIndex ) {
      throw new IndexOutOfBoundsException( "No entries left for: " + given );
    }

    @Nonnull
    String nextExpected = this.expectedOut.get( expectedIndex );
    expectedIndex++;

    if ( nextExpected == null ) {
      return;
    }

    if ( !nextExpected.equals( String.valueOf( given ) ) ) {
      throw new RuntimeException( "Did not match: \"" + nextExpected + "\" - but was:  \"" + given + "\"" );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public PrintStream getOut() {
    throw new UnsupportedOperationException();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  protected ConsolePrinter getConsolePrinter() {
    return consolePrinter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean readBoolean( @Nonnull String message ) throws IOException {
    messages.add( message );
    return ( Boolean ) getNextAnswer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public String read( @Nonnull String message ) {
    messages.add( message );
    return ( String ) getNextAnswer();
  }

  private final List<String> messages = new ArrayList<String>();

  /**
   * <p>Getter for the field <code>messages</code>.</p>
   *
   * @return a {@link List} object.
   */
  @Nonnull
  public List<String> getMessages() {
    return Collections.unmodifiableList( messages );
  }

  /**
   * <p>getNextAnswer</p>
   *
   * @return a {@link Object} object.
   */
  protected Object getNextAnswer() {
    if ( answers.isEmpty() ) {
      throw new IllegalStateException( "No answer left" );
    }
    return answers.remove( 0 );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public String read( @Nonnull String message, @Nullable String defaultValue ) {
    return read( message );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int readInt( @Nonnull String message, int lower, int upper ) {
    messages.add( message );
    return ( Integer ) getNextAnswer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int readInt( @Nonnull String message ) throws IOException {
    messages.add( message );
    return ( Integer ) getNextAnswer();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void pause( int seconds ) {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void out( @Nonnull String message, @Nonnull Object... objects ) {
    messages.add( message );
    checkExpectedOut( consolePrinter.createSuccess( message, objects ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void out( @Nonnull Process process ) {
    try {
      BufferedReader defaultIn = null;
      try {
        defaultIn = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
        String line;
        while ( ( line = defaultIn.readLine() ) != null ) {
          out( line );
        }
      } finally {
        if ( defaultIn != null ) {
          defaultIn.close();
        }
      }

      BufferedReader errorIn = null;
      try {
        errorIn = new BufferedReader( new InputStreamReader( process.getErrorStream() ) );
        String line;
        while ( ( line = errorIn.readLine() ) != null ) {
          error( line );
        }
      } finally {
        if ( errorIn != null ) {
          errorIn.close();
        }
      }
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void error( @Nonnull String message, @Nonnull Object... objects ) {
    messages.add( message );
    checkExpectedOut( getConsolePrinter().createError( message, objects ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void warning( @Nonnull String message, @Nonnull Object... objects ) {
    messages.add( message );
    checkExpectedOut( getConsolePrinter().createWarning( message, objects ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void success( @Nonnull String message, @Nonnull Object... objects ) {
    messages.add( message );
    checkExpectedOut( getConsolePrinter().createSuccess( message, objects ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void outNl() {
    checkExpectedOut( "" );
  }

  /**
   * <p>addExpectedOut</p>
   *
   * @param expected a {@link String} object.
   */
  public void addExpectedOut( @Nullable String expected ) {
    expectedOut.add( expected );
  }

  /**
   * <p>Getter for the field <code>expectedOut</code>.</p>
   *
   * @return a {@link List} object.
   */
  @Nonnull
  public List<String> getExpectedOut() {
    return Collections.unmodifiableList( expectedOut );
  }

  /**
   * <p>Getter for the field <code>answers</code>.</p>
   *
   * @return a {@link List} object.
   */
  @Nonnull
  public List<Object> getAnswers() {
    return Collections.unmodifiableList( answers );
  }

  /**
   * <p>Setter for the field <code>answers</code>.</p>
   *
   * @param answers a {@link List} object.
   */
  public void setAnswers( @Nonnull List<Object> answers ) {
    this.answers.clear();
    this.answers.addAll( answers );
  }

  /**
   * <p>addAnswer</p>
   *
   * @param answer a {@link Object} object.
   */
  public void addAnswer( @Nonnull Object answer ) {
    answers.add( answer );
  }

  /**
   * <p>removeAnswer</p>
   *
   * @param answer a {@link Object} object.
   */
  public void removeAnswer( @Nonnull Object answer ) {
    answers.remove( answer );
  }

  private static class SimpleConsolePrinter implements ConsolePrinter {
    @Override
    @Nonnull
    public String createError( @Nonnull String message, @Nonnull Object... objects ) {
      return "ERROR: " + MessageFormat.format( message, objects );
    }

    @Override
    @Nonnull
    public String createWarning( @Nonnull String message, @Nonnull Object... objects ) {
      return "WARN: " + MessageFormat.format( message, objects );
    }

    @Override
    @Nonnull
    public String createSuccess( @Nonnull String message, @Nonnull Object... objects ) {
      return MessageFormat.format( message, objects );
    }
  }
}
