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

package com.cedarsoft.zip;

import junit.framework.Assert;
import junit.framework.AssertionFailedError;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

/**
 * Assertions for zip files
 */
public class ZipAssertions {
  private ZipAssertions() {
  }

  public static void compare( @Nonnull ZipFile expected, @Nonnull ZipFile actual ) {
    if ( expected == actual ) {
      return;
    }

    Set<String> testedActualEntries = new HashSet<String>( actual.size() );

    //Forward
    Enumeration<? extends ZipEntry> expectedEntries = expected.entries();
    while ( expectedEntries.hasMoreElements() ) {
      ZipEntry expectedEntry = expectedEntries.nextElement();
      ZipEntry actualEntry = actual.getEntry( expectedEntry.getName() );
      compare( expectedEntry, actualEntry );
      testedActualEntries.add( actualEntry.getName() );
    }

    //Backward
    Enumeration<? extends ZipEntry> actualEntries = actual.entries();
    while ( actualEntries.hasMoreElements() ) {
      ZipEntry actualEntry = actualEntries.nextElement();
      if ( testedActualEntries.contains( actualEntry.getName() ) ) {
        continue;
      }
      throw new AssertionFailedError( "Uups. Unexpected element in actual: " + actualEntry.getName() );
    }
    Assert.assertEquals( expected.size(), actual.size() );
  }

  public static void compare( @Nullable ZipEntry expectedEntry, @Nullable ZipEntry actualEntry ) {
    if ( expectedEntry == actualEntry ) {
      return;
    }
    if ( expectedEntry == null ) {
      throw new AssertionFailedError( "ExpectedEntry is null for " + actualEntry.getName() );
    }
    if ( actualEntry == null ) {
      throw new AssertionFailedError( "ActualEntry is null for " + expectedEntry.getName() );
    }

    Assert.assertEquals( expectedEntry.getName(), actualEntry.getName() );
    Assert.assertEquals( expectedEntry.getCrc(), actualEntry.getCrc() );
    Assert.assertEquals( expectedEntry.getSize(), actualEntry.getSize() );
    Assert.assertEquals( "Different Name. Expected: <" + expectedEntry.getName() + "> but was <" + actualEntry.getName() + ">", expectedEntry.getName(), actualEntry.getName() );
    Assert.assertEquals( "Different Size. Expected: <" + expectedEntry.getSize() + "> but was <" + actualEntry.getSize() + ">", expectedEntry.getSize(), actualEntry.getSize() );
    Assert.assertEquals( "Different CRC. Expected: <" + expectedEntry.getCrc() + "> but was <" + actualEntry.getCrc() + ">", expectedEntry.getCrc(), actualEntry.getCrc() );
  }
}
