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


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.Serializable;

/**
 * A version
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class Version implements Comparable<Version>, Serializable {
  /**
   * Constant <code>MAX=99</code>
   */
  protected static final int MAX = 99;

  protected final int major;
  protected final int minor;
  protected final int build;
  @Nullable
  protected final String suffix;


  /**
   * <p>Constructor for Version.</p>
   *
   * @param major a int.
   * @param minor a int.
   * @param build a int.
   */
  public Version( int major, int minor, int build ) {
    this( major, minor, build, null );
  }

  /**
   * Creates a version
   *
   * @param major  the major part
   * @param minor  the minor part
   * @param build  the build
   * @param suffix the suffix
   */
  public Version( int major, int minor, int build, @Nullable String suffix ) {
    this.major = major;
    this.build = build;
    this.minor = minor;
    this.suffix = suffix;

    if ( major > MAX || major < 0 ) {
      throw new IllegalArgumentException( "Invalid major <" + major + ">" );
    }
    if ( minor > MAX || minor < 0 ) {
      throw new IllegalArgumentException( "Invalid minor <" + minor + ">" );
    }
    if ( build > MAX || build < 0 ) {
      throw new IllegalArgumentException( "Invalid build <" + build + ">" );
    }
  }

  /**
   * Returns the optional suffix
   *
   * @return the suffix or null if no suffix has been set
   */
  @Nullable
  public String getSuffix() {
    return suffix;
  }

  /**
   * <p>Getter for the field <code>build</code>.</p>
   *
   * @return a int.
   */
  public int getBuild() {
    return build;
  }

  /**
   * <p>Getter for the field <code>major</code>.</p>
   *
   * @return a int.
   */
  public int getMajor() {
    return major;
  }

  /**
   * <p>Getter for the field <code>minor</code>.</p>
   *
   * @return a int.
   */
  public int getMinor() {
    return minor;
  }

  /**
   * <p>format</p>
   *
   * @return a {@link String} object.
   */
  @Nonnull
  public String format() {
    return toString();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    if ( suffix != null && suffix.length() > 0 ) {
      return major + "." + minor + '.' + build + '-' + suffix;
    } else {
      return major + "." + minor + '.' + build;
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int compareTo( Version o ) {
    if ( major != o.major ) {
      return Integer.valueOf( major ).compareTo( o.major );
    }

    if ( minor != o.minor ) {
      return Integer.valueOf( minor ).compareTo( o.minor );
    }

    return Integer.valueOf( build ).compareTo( o.build );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean equals( Object o ) {
    if ( this == o ) return true;
    if ( !( o instanceof Version ) ) return false;

    if ( !this.getClass().equals( o.getClass() ) ) {
      return false;
    }

    Version version = ( Version ) o;

    if ( build != version.build ) return false;
    if ( major != version.major ) return false;
    if ( minor != version.minor ) return false;
    if ( suffix != null ? !suffix.equals( version.suffix ) : version.suffix != null ) return false;

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public int hashCode() {
    int result = major;
    result = 31 * result + minor;
    result = 31 * result + build;
    result = 31 * result + ( suffix != null ? suffix.hashCode() : 0 );
    return result;
  }

  /**
   * Liefert den Int-Werte des Versionsnummer. Major, Minor und Build gehen maximal bis 99.
   *
   * @return the int value for the version
   */
  public int toInt() {
    return major * 10000 + minor * 100 + build;
  }

  /**
   * Parses a version
   *
   * @param version the version number as string
   * @return the parsed version
   *
   * @throws IllegalArgumentException if any.
   */
  @Nonnull
  public static Version parse( @Nonnull String version ) throws IllegalArgumentException {
    int indexDot0 = version.indexOf( '.' );
    int indexDot1 = version.indexOf( '.', indexDot0 + 2 );
    int indexMinus = version.indexOf( '-', indexDot1 + 2 );

    if ( indexDot0 == -1 || indexDot1 == -1 ) {
      throw new IllegalArgumentException();
    }

    int major = Integer.parseInt( version.substring( 0, indexDot0 ) );
    int minor = Integer.parseInt( version.substring( indexDot0 + 1, indexDot1 ) );

    if ( indexMinus == -1 ) {
      int build = Integer.parseInt( version.substring( indexDot1 + 1 ) );
      return new Version( major, minor, build );
    } else {
      int build = Integer.parseInt( version.substring( indexDot1 + 1, indexMinus ) );
      String suffix = version.substring( indexMinus + 1 );
      return new Version( major, minor, build, suffix );
    }
  }

  /**
   * <p>verifyMatch</p>
   *
   * @param expected a {@link Version} object.
   * @param actual   a {@link Version} object.
   * @throws VersionMismatchException if any.
   */
  public static void verifyMatch( @Nonnull Version expected, @Nonnull Version actual ) throws VersionMismatchException {
    if ( !expected.equals( actual ) ) {
      throw new VersionMismatchException( expected, actual );
    }
  }

  /**
   * <p>sameOrSmallerThan</p>
   *
   * @param version a {@link Version} object.
   * @return a boolean.
   */
  public boolean sameOrSmallerThan( @Nonnull Version version ) {
    return this.compareTo( version ) <= 0;
  }

  /**
   * <p>smallerThan</p>
   *
   * @param version a {@link Version} object.
   * @return a boolean.
   */
  public boolean smallerThan( @Nonnull Version version ) {
    return this.compareTo( version ) < 0;
  }

  /**
   * <p>sameOrGreaterThan</p>
   *
   * @param version a {@link Version} object.
   * @return a boolean.
   */
  public boolean sameOrGreaterThan( @Nonnull Version version ) {
    return this.compareTo( version ) >= 0;
  }

  /**
   * <p>greaterThan</p>
   *
   * @param version a {@link Version} object.
   * @return a boolean.
   */
  public boolean greaterThan( @Nonnull Version version ) {
    return this.compareTo( version ) > 0;
  }

  /**
   * <p>valueOf</p>
   *
   * @param major a int.
   * @param minor a int.
   * @param build a int.
   * @return a {@link Version} object.
   */
  @Nonnull
  public static Version valueOf( int major, int minor, int build ) {
    return new Version( major, minor, build );
  }

  /**
   * @param major the major
   * @param minor the minor
   * @param build the build
   * @noinspection ParameterHidesMemberVariable
   */
  public boolean equals( int major, int minor, int build ) {
    return this.major == major && this.minor == minor && this.build == build;
  }
}
