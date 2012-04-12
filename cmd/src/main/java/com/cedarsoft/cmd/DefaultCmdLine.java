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

import javax.inject.Singleton;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;

/**
 * The default command line
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
@Singleton
public class DefaultCmdLine extends AbstractCmdLine {
  /**
   * <p>isAnsiAware</p>
   *
   * @return a boolean.
   */
  public static boolean isAnsiAware() {
    String osName = System.getProperty( "os.name" );
    if ( osName.contains( "indows" ) ) {
      return false;
    }

    //Within IDE?
    return !Boolean.parseBoolean( System.getProperty( "ansiDisabled" ) );
  }

  private final ConsolePrinter consolePrinter;

  /**
   * <p>Constructor for DefaultCmdLine.</p>
   */
  public DefaultCmdLine() {
    if ( isAnsiAware() ) {
      this.consolePrinter = new AnsiAwareConsolePrinter();
    } else {
      this.consolePrinter = new DefaultConsolePrinter();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Nonnull
  @Override
  protected ConsolePrinter getConsolePrinter() {
    return consolePrinter;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean readBoolean( @Nonnull String message ) throws IOException {
    String answer = read( message + " (y/n)" );
    if ( answer.equalsIgnoreCase( "y" ) ) {
      return true;
    }
    if ( answer.equalsIgnoreCase( "j" ) ) {
      return true;
    }
    if ( answer.equalsIgnoreCase( "1" ) ) {
      return true;
    }
    return Boolean.parseBoolean( message );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public String read( @Nonnull String message ) {
    return read( message, ( String ) null );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public String read( @Nonnull String message, @Nullable String defaultValue ) {
    //First clear the input stream
    try {
      int available;
      while ( ( available = getIn().available() ) > 0 ) {
        getIn().skip( available );
      }
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }

    if ( defaultValue == null ) {
      out( message );
    } else {
      out( message + " [" + defaultValue + ']' );
    }

    try {
      byte[] buffer = new byte[500];
      //noinspection ResultOfMethodCallIgnored
      getIn().read( buffer, 0, buffer.length );
      String read = new String( buffer ).trim();
      if ( read.length() == 0 && defaultValue != null ) {
        return defaultValue;
      }
      return read;
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  /**
   * <p>getIn</p>
   *
   * @return a {@link InputStream} object.
   */
  @Nonnull
  public InputStream getIn() {
    return System.in;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public PrintStream getOut() {
    return System.out;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int readInt( @Nonnull String message, int lower, int upper ) {
    try {
      while ( true ) {
        int value = readInt( message );
        if ( value < lower || value >= upper ) {
          out( "Value out of range. Please enter an int between " + lower + " (incl) and " + upper + " (excl)." );
        } else {
          return value;
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
  public int readInt( @Nonnull String message ) throws IOException {
    while ( true ) {
      String value = read( message );
      try {
        return Integer.parseInt( value );
      } catch ( NumberFormatException ignore ) {
        //noinspection HardCodedStringLiteral
        out( "Invalid argument. Please enter an Integer" );
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void pause( int seconds ) {
    getOut().print( "Pausing for " + seconds + " seconds:" );
    for ( int i = 1; i <= seconds; i++ ) {
      getOut().print( " " );
      getOut().print( i );
      try {
        Thread.sleep( 1000 );
      } catch ( InterruptedException e ) {
        System.exit( 0 );
      }
    }
    getOut().println();
  }
}
