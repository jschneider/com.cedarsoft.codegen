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

package com.cedarsoft.test.utils.matchers;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import javax.annotation.Nonnull;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 */
public class ContainsOnlyFilesMatcher extends BaseMatcher<File> {
  @Nonnull
  private final List<String> filePaths;

  public ContainsOnlyFilesMatcher( @Nonnull String... relativeFilePaths ) {
    filePaths = new ArrayList<String>( Arrays.asList( relativeFilePaths ) );
  }

  @Override
  public boolean matches( Object o ) {
    File dir = ( File ) o;
    if ( !( ( File ) o ).isDirectory() ) {
      return false;
    }

    Collection<? extends File> files = FileUtils.listFiles( dir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE );
    if ( files.size() != filePaths.size() ) {
      return false;
    }

    //Create the set with the expected files
    Set<? extends File> expected = createExepectedSet( dir );


    for ( File file : files ) {
      if ( !expected.contains( file ) ) {
        return false;
      }
    }

    return true;
  }

  @Nonnull
  private Set<? extends File> createExepectedSet( @Nonnull File baseDir ) {
    Set<File> expected = new HashSet<File>();
    for ( String filePath : filePaths ) {
      expected.add( new File( baseDir, filePath ) );
    }
    return expected;
  }

  @Override
  public void describeTo( Description description ) {
    description.appendText( "contains only files " + filePaths );
  }

  @Nonnull
  public List<? extends String> getFilePaths() {
    return Collections.unmodifiableList( filePaths );
  }

  @Nonnull
  public static Matcher<File> containsOnlyFiles( @Nonnull String... relativeFilePaths ) {
    return new ContainsOnlyFilesMatcher( relativeFilePaths );
  }

  @Nonnull
  public static String toTree( @Nonnull File dir ) {
    return ContainsFileMatcher.toTree( dir );
  }
}
