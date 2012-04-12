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

import com.google.common.io.Files;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.io.IOException;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 *
 */
public class ContainsOnlyFilesMatcherTest {
  @Rule
  public final TemporaryFolder tmp = new TemporaryFolder();

  @Rule
  public final ExpectedException expectedException = ExpectedException.none();

  @Test
  public void testOnly() throws IOException {
    Files.touch( new File( tmp.newFolder( "dir" ), "a" ) );
    assertThat( ContainsOnlyFilesMatcher.toTree( tmp.getRoot() ), tmp.getRoot(), ContainsOnlyFilesMatcher.containsOnlyFiles( "dir/a" ) );

    expectedException.expect( AssertionError.class );
    Files.touch( new File( tmp.newFolder( "dir2" ), "a2" ) );
    assertThat( ContainsOnlyFilesMatcher.toTree( tmp.getRoot() ), tmp.getRoot(), ContainsOnlyFilesMatcher.containsOnlyFiles( "dir/a" ) );
  }

  @Test
  public void testMessage() {
    Description description = new StringDescription();
    ContainsOnlyFilesMatcher.containsOnlyFiles( "dir/a", "dir2/b" ).describeTo( description );
    assertThat( description.toString(), is( "contains only files [dir/a, dir2/b]" ) );
  }

  @Test
  public void testNone() throws IOException {
    expectedException.expect( AssertionError.class );
    assertThat( ContainsOnlyFilesMatcher.toTree( tmp.getRoot() ), tmp.getRoot(), ContainsOnlyFilesMatcher.containsOnlyFiles( "dir/a" ) );
  }

  @Test
  public void testWrong() throws IOException {
    expectedException.expect( AssertionError.class );
    Files.touch( new File( tmp.newFolder( "dir" ), "a" ) );
    assertThat( ContainsOnlyFilesMatcher.toTree( tmp.getRoot() ), tmp.getRoot(), ContainsOnlyFilesMatcher.containsOnlyFiles( "dir/b" ) );
  }
}
