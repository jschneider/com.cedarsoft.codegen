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

package com.cedarsoft.io;


import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Class to help determining the OS.
 *
 * @author <a href="mailto:stefan.bodewig@epost.de">Stefan Bodewig</a>
 * @author <a href="mailto:umagesh@apache.org">Magesh Umasankar</a>
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 */
public final class Os {
  /**
   * All Windows based OSes.
   */
  public static final OsFamily OS_FAMILY_WINDOWS = new OsFamily( "windows" );

  /**
   * All DOS based OSes.
   */
  public static final OsFamily OS_FAMILY_DOS = new OsFamily( "dos" );

  /**
   * All Windows NT based OSes.
   */
  public static final OsFamily OS_FAMILY_WINNT = new OsFamily( "nt", new OsFamily[]{OS_FAMILY_WINDOWS} );

  /**
   * All Windows 9x based OSes.
   */
  public static final OsFamily OS_FAMILY_WIN9X = new OsFamily( "win9x", new OsFamily[]{OS_FAMILY_WINDOWS, OS_FAMILY_DOS} );

  /**
   * OS/2.
   */
  public static final OsFamily OS_FAMILY_OS2 = new OsFamily( "os/2", new OsFamily[]{OS_FAMILY_DOS} );

  /**
   * Netware.
   */
  public static final OsFamily OS_FAMILY_NETWARE = new OsFamily( "netware" );

  /**
   * All UNIX based OSes.
   */
  public static final OsFamily OS_FAMILY_UNIX = new OsFamily( "unix" );

  /**
   * All Mac based OSes.
   */
  public static final OsFamily OS_FAMILY_MAC = new OsFamily( "mac" );

  /**
   * OSX.
   */
  public static final OsFamily OS_FAMILY_OSX = new OsFamily( "osx", new OsFamily[]{OS_FAMILY_UNIX, OS_FAMILY_MAC} );

  private static final String OS_NAME = System.getProperty( "os.name" ).toLowerCase( Locale.US );
  private static final String OS_ARCH = System.getProperty( "os.arch" ).toLowerCase( Locale.US );
  private static final String OS_VERSION = System.getProperty( "os.version" ).toLowerCase( Locale.US );
  private static final String PATH_SEP = System.getProperty( "path.separator" );
  private static final OsFamily OS_FAMILY;
  private static final OsFamily[] OS_ALL_FAMILIES;

  private static final OsFamily[] ALL_FAMILIES = {
    OS_FAMILY_DOS,
    OS_FAMILY_MAC,
    OS_FAMILY_NETWARE,
    OS_FAMILY_OS2,
    OS_FAMILY_OSX,
    OS_FAMILY_UNIX,
    OS_FAMILY_WINDOWS,
    OS_FAMILY_WINNT,
    OS_FAMILY_WIN9X
  };

  static {
    OS_FAMILY = determineOsFamily();
    OS_ALL_FAMILIES = determineAllFamilies();
  }

  /**
   * Private constructor to block instantiation.
   */
  private Os() {
  }

  /**
   * Determines if the OS on which Ant is executing matches the given OS
   * version.
   *
   * @param version The version to check.
   * @return true if the version matches.
   */
  public static boolean isVersion( final String version ) {
    return isOs( ( OsFamily ) null, null, null, version );
  }

  /**
   * Determines if the OS on which Ant is executing matches the given OS
   * architecture.
   *
   * @param arch The architecture to check.
   * @return true if the architecture mataches.
   */
  public static boolean isArch( final String arch ) {
    return isOs( ( OsFamily ) null, null, arch, null );
  }

  /**
   * Determines if the OS on which Ant is executing matches the given OS
   * family.
   *
   * @param family The family to check.
   * @return true if the family matches.
   */
  public static boolean isFamily( final String family ) {
    return isOs( family, null, null, null );
  }

  /**
   * Determines if the OS on which Ant is executing matches the given OS
   * family.
   *
   * @param family The family to check.
   * @return true if the family matches.
   */
  public static boolean isFamily( final OsFamily family ) {
    return isOs( family, null, null, null );
  }

  /**
   * Determines if the OS on which Ant is executing matches the given OS name.
   *
   * @param name Description of Parameter
   * @return The Name value
   *
   * @since 1.7
   */
  public static boolean isName( final String name ) {
    return isOs( ( OsFamily ) null, name, null, null );
  }

  /**
   * Determines if the OS on which Ant is executing matches the given OS
   * family, name, architecture and version.
   *
   * @param family  The OS family
   * @param name    The OS name
   * @param arch    The OS architecture
   * @param version The OS version
   * @return The Os value
   */
  public static boolean isOs( final String family, final String name, final String arch, final String version ) {
    return isOs( getFamily( family ), name, arch, version );
  }

  /**
   * Determines if the OS on which Ant is executing matches the given OS
   * family, name, architecture and version.
   *
   * @param family  The OS family
   * @param name    The OS name
   * @param arch    The OS architecture
   * @param version The OS version
   * @return The Os value
   */
  public static boolean isOs( final OsFamily family, final String name, final String arch, final String version ) {
    if ( family != null || name != null || arch != null || version != null ) {
      final boolean isFamily = familyMatches( family );
      final boolean isName = nameMatches( name );
      final boolean isArch = archMatches( arch );
      final boolean isVersion = versionMatches( version );

      return isFamily && isName && isArch && isVersion;
    } else {
      return false;
    }
  }

  /**
   * Locates an OsFamily by name (case-insensitive).
   *
   * @param name The family name to lookup.
   * @return the OS family, or null if not found.
   */
  @Nullable
  public static OsFamily getFamily( @Nonnull final String name ) {
    for ( final OsFamily osFamily : ALL_FAMILIES ) {
      if ( osFamily.getName().equalsIgnoreCase( name ) ) {
        return osFamily;
      }
    }

    return null;
  }

  private static boolean versionMatches( @Nonnull final String version ) {
    boolean isVersion = true;
    if ( version != null ) {
      isVersion = version.equalsIgnoreCase( OS_VERSION );
    }
    return isVersion;
  }

  private static boolean archMatches( @Nonnull final String arch ) {
    boolean isArch = true;
    if ( arch != null ) {
      isArch = arch.equalsIgnoreCase( OS_ARCH );
    }
    return isArch;
  }

  private static boolean nameMatches( @Nonnull final String name ) {
    boolean isName = true;
    if ( name != null ) {
      isName = name.equalsIgnoreCase( OS_NAME );
    }
    return isName;
  }

  private static boolean familyMatches( final OsFamily family ) {
    if ( family == null ) {
      return false;
    }
    for ( final OsFamily osFamily : OS_ALL_FAMILIES ) {
      if ( family == osFamily ) {
        return true;
      }
    }
    return false;
  }

  private static OsFamily[] determineAllFamilies() {
    // Determine all families the current OS belongs to
    Collection<OsFamily> allFamilies = new HashSet<OsFamily>();
    if ( OS_FAMILY != null ) {
      List<OsFamily> queue = new ArrayList<OsFamily>();
      queue.add( OS_FAMILY );
      while ( !queue.isEmpty() ) {
        final OsFamily family = queue.remove( 0 );
        allFamilies.add( family );
        final OsFamily[] families = family.getFamilies();
        for ( OsFamily parent : families ) {
          queue.add( parent );
        }
      }
    }
    return allFamilies.toArray( new OsFamily[allFamilies.size()] );
  }

  private static OsFamily determineOsFamily() {
    // Determine the most specific OS family
    if ( OS_NAME.contains( "windows" ) ) {
      if ( OS_NAME.contains( "xp" )
        || OS_NAME.contains( "2000" )
        || OS_NAME.contains( "nt" ) ) {
        return OS_FAMILY_WINNT;
      } else {
        return OS_FAMILY_WIN9X;
      }
    } else if ( OS_NAME.contains( "os/2" ) ) {
      return OS_FAMILY_OS2;
    } else if ( OS_NAME.contains( "netware" ) ) {
      return OS_FAMILY_NETWARE;
    } else if ( OS_NAME.contains( "mac" ) ) {
      if ( OS_NAME.endsWith( "x" ) ) {
        return OS_FAMILY_OSX;
      } else {
        return OS_FAMILY_MAC;
      }
    } else if ( PATH_SEP.equals( ":" ) ) {
      return OS_FAMILY_UNIX;
    } else {
      return null;
    }
  }
}
