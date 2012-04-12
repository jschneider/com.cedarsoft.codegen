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

import com.cedarsoft.renderer.DefaultRenderer;
import com.cedarsoft.renderer.Renderer;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.text.MessageFormat;
import java.util.List;

/**
 * Base class that delegates all methods to the a few core methods.
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public abstract class AbstractCmdLine implements CmdLine {
  /**
   * {@inheritDoc}
   */
  @Override
  public void warning( @Nonnull String message, @Nonnull Object... objects ) {
    getOut().println( getConsolePrinter().createWarning( message, objects ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void error( @Nonnull String message, @Nonnull Object... objects ) {
    getOut().println( getConsolePrinter().createError( message, objects ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void success( @Nonnull String message, @Nonnull Object... objects ) {
    getOut().println( getConsolePrinter().createSuccess( message, objects ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void outNl() {
    getOut().println();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void out( @Nonnull String message, @Nonnull Object... objects ) {
    if ( objects.length == 0 ) {
      getOut().println( message );
    } else {
      getOut().println( MessageFormat.format( message, objects ) );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void out( @Nonnull Process process ) {
    try {
      BufferedReader defaultIn = new BufferedReader( new InputStreamReader( process.getInputStream() ) );
      try {
        String line;
        while ( ( line = defaultIn.readLine() ) != null ) {
          out( line );
        }
      } finally {
        defaultIn.close();
      }

      BufferedReader errorIn = new BufferedReader( new InputStreamReader( process.getErrorStream() ) );
      try {
        String line;
        while ( ( line = errorIn.readLine() ) != null ) {
          error( line );
        }
      } finally {
        errorIn.close();
      }
    } catch ( IOException e ) {
      throw new RuntimeException( e );
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public <T> T readSelection( @Nonnull String message, @Nonnull List<? extends T> elements ) {
    return readSelection( message, elements, null );
  }

  /**
   * <p>printPossibleElements</p>
   *
   * @param elements  a {@link List} object.
   * @param presenter a {@link Renderer} object.
   */
  protected <T> void printPossibleElements( @Nonnull List<? extends T> elements, @Nullable Renderer<? super T, Object> presenter ) {
    if ( presenter == null ) {
      //noinspection AssignmentToMethodParameter
      presenter = new DefaultRenderer();
    }
    int index = 0;
    for ( T element : elements ) {
      out( "\t" + index + '\t' + presenter.render( element, null ) );
      index++;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public <T> T readSelection( @Nonnull String message, @Nonnull List<? extends T> elements, @Nullable Renderer<? super T, Object> presenter ) {
    out( message );
    printPossibleElements( elements, presenter );
    return elements.get( readInt( "Enter the number of the element you want to select", 0, elements.size() ) );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public String read( @Nonnull String message, @Nonnull List<String> elements ) {
    out( message );
    printPossibleElements( elements, null );

    @Nullable
    String read = null;
    while ( read == null || read.length() == 0 ) {
      read = read( "Enter the value. Or enter the number of the element you want to select." );

      try {
        int selected = Integer.parseInt( read );

        if ( selected < 0 || selected > elements.size() - 1 ) {
          read = null;
          warning( "Selection invalid: <" + selected + "> " );
        } else {
          return elements.get( selected );
        }
      } catch ( NumberFormatException ignore ) {
        return read;
      }
    }

    throw new IllegalStateException( "Should not reach" );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public String read( @Nonnull String message, @Nonnull List<String> elements, @Nonnull String preselected ) {
    out( message );
    printPossibleElements( elements, null );

    String read = read( "Enter the value. Or enter the number of the element you want to select. Default selection: " + preselected );

    if ( read.length() == 0 ) {
      return preselected;
    } else {
      return read;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public <T> T read( @Nonnull String message, @Nonnull List<? extends T> elements, @Nullable Renderer<T, Object> presenter, @Nonnull ObjectFactory<T> objectFactory ) {
    out( message );
    printPossibleElements( elements, presenter );

    String read = null;

    while ( read == null ) {
      read = read( "Enter the value. Or enter the number of the element you want to select." );
      try {
        int index = Integer.parseInt( read );
        if ( index >= 0 && index < elements.size() ) {
          return elements.get( index );
        }
      } catch ( NumberFormatException ignore ) {
      }

      try {
        return objectFactory.create( read );
      } catch ( ObjectFactory.InvalidRepresentationException e ) {
        this.error( "Invalid input: " + e.getMessage() );
        //noinspection AssignmentToNull
        read = null;
      }
    }

    throw new IllegalStateException( "Should not reach" );
  }

  /**
   * <p>getOut</p>
   *
   * @return a {@link PrintStream} object.
   */
  @Nonnull
  public abstract PrintStream getOut();

  /**
   * <p>getConsolePrinter</p>
   *
   * @return a {@link ConsolePrinter} object.
   */
  @Nonnull
  protected abstract ConsolePrinter getConsolePrinter();
}
