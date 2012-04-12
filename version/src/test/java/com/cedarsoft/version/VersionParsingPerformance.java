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

package com.cedarsoft.version;

import com.google.common.base.Splitter;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Iterator;
import java.util.concurrent.Callable;

import static org.junit.Assert.*;

/**
 *
 */
public class VersionParsingPerformance {
  @Nonnull
  public static final Splitter DOT_SPLITTER = Splitter.on( "." );
  @Nonnull
  public static final String VERSION = "1.2.3-suffix";

  public static void main( String[] args ) throws Exception {
    run( "String.plit", new Callable<Version>() {
      @Override
      public Version call() throws Exception {
        String[] parts = VERSION.split( "\\." );
        if ( parts.length != 3 ) {
          throw new IllegalArgumentException( "Version <" + VERSION + "> must contain exactly three parts delimited with '.'" );
        }

        int build;

        @Nullable
        String suffix;
        if ( parts[2].contains( "-" ) ) {
          int firstIndex = parts[2].indexOf( '-' );
          String buildAsString = parts[2].substring( 0, firstIndex );

          build = Integer.parseInt( buildAsString );
          suffix = parts[2].substring( firstIndex + 1, parts[2].length() );
        } else {
          build = Integer.parseInt( parts[2] );
          suffix = null;
        }

        int major = Integer.parseInt( parts[0] );
        int minor = Integer.parseInt( parts[1] );

        return new Version( major, minor, build, suffix );
      }
    } );

    run( "static Splitter", new Callable<Version>() {
      @Override
      public Version call() throws Exception {
        Iterable<String> parts = DOT_SPLITTER.split( VERSION );
        Iterator<String> iterator = parts.iterator();

        String part0 = iterator.next();
        String part1 = iterator.next();
        String part2 = iterator.next();

        if ( iterator.hasNext() ) {
          throw new IllegalArgumentException( "In" );
        }

        int index = part2.indexOf( "-" );
        if ( index > -1 ) {
          String suffix = part2.substring( index + 1 );
          int build = Integer.parseInt( part2.substring( 0, index ) );
          return new Version( Integer.parseInt( part0 ), Integer.parseInt( part1 ), build, suffix );
        } else {
          return new Version( Integer.parseInt( part0 ), Integer.parseInt( part1 ), Integer.parseInt( part0 ) );
        }
      }
    } );
    run( "version.parse", new Callable<Version>() {
      @Override
      public Version call() throws Exception {
        return Version.parse( VERSION );
      }
    } );

    run( "indexOf", new Callable<Version>() {
      @Override
      public Version call() throws Exception {
        int index0 = VERSION.indexOf( "." );
        int index1 = VERSION.indexOf( ".", index0 + 2 );
        int indexMinus = VERSION.indexOf( "-", index1 + 2 );

        if ( index0 == -1 || index1 == -1 ) {
          throw new IllegalArgumentException();
        }

        int major = Integer.parseInt( VERSION.substring( 0, index0 ) );
        int minor = Integer.parseInt( VERSION.substring( index0 + 1, index1 ) );
        int build = Integer.parseInt( VERSION.substring( index1 + 1, indexMinus ) );
        String suffix = VERSION.substring( indexMinus + 1 );

        return new Version( major, minor, build, suffix );
      }
    } );

  }

  private static void run( @Nonnull String description, @Nonnull Callable<Version> callable ) throws Exception {
    //Warmup
    for ( int i = 0; i < 10000000; i++ ) {
      Version version = callable.call();
      assertEquals( 1, version.getMajor() );
      assertEquals( 2, version.getMinor() );
      assertEquals( 3, version.getBuild() );
      assertEquals( "suffix", version.getSuffix() );
    }

    //Do the work
    long start = System.currentTimeMillis();
    for ( int i = 0; i < 10000000; i++ ) {
      Version version = callable.call();
      assertEquals( 1, version.getMajor() );
      assertEquals( 2, version.getMinor() );
      assertEquals( 3, version.getBuild() );
      assertEquals( "suffix", version.getSuffix() );
    }

    long end = System.currentTimeMillis();
    System.out.println( description + " took " + ( end - start ) );
  }

}
