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
import org.junit.*;
import org.junit.rules.*;

import java.io.File;
import java.io.IOException;

import static com.cedarsoft.test.utils.matchers.ContainsFileMatcher.containsFiles;
import static com.cedarsoft.test.utils.matchers.ContainsFileMatcher.empty;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

/**
 *
 */
public class ContainsFileMatcherTest {
  @Rule
  public final TemporaryFolder tmp = new TemporaryFolder();

  @Test
  public void testBasic() throws IOException {
    Files.touch( tmp.newFile( "a" ) );
    assertThat( tmp.getRoot(), containsFiles( "a" ) );
    Files.touch( tmp.newFile( "b" ) );
    assertThat( tmp.getRoot(), containsFiles( "a", "b" ) );
  }

  @Test
  public void testSubDir() throws IOException {
    Files.touch( new File( tmp.newFolder( "dir" ), "a" ) );
    assertThat( tmp.getRoot(), containsFiles( "dir/a" ) );
  }

  @Test
  public void testEmpty() throws IOException {
    assertThat( tmp.getRoot(), is( empty() ) );

    expectedException.expect( AssertionError.class );
    Files.touch( tmp.newFile( "a" ) );
    assertThat( tmp.getRoot(), is( empty() ) );
  }

  @Test
  public void testList() throws IOException {
    {
      File dir = tmp.newFolder( "dir" );
      Files.touch( new File( dir, "b" ) );
      Files.touch( new File( dir, "a" ) );
      Files.touch( new File( dir, "c" ) );
    }

    {
      File dir = tmp.newFolder( "dir2" );
      Files.touch( new File( dir, "a" ) );
      Files.touch( new File( dir, "c" ) );
      Files.touch( new File( dir, "b" ) );
    }


    String tree = ContainsFileMatcher.toTree(tmp.getRoot());

    assertTrue(tree.contains("dir" + File.separator + "a"));
    assertTrue(tree.contains("dir" + File.separator + "b"));
    assertTrue(tree.contains("dir" + File.separator + "c"));
    assertTrue(tree.contains("dir2" + File.separator + "a"));
    assertTrue(tree.contains("dir2" + File.separator + "b"));
    assertTrue(tree.contains("dir2" + File.separator + "c"));
  }

  @Rule
  public final ExpectedException expectedException = ExpectedException.none();

  @Test
  public void testNontExistent() throws IOException {
    expectedException.expect( AssertionError.class );
    assertThat( tmp.getRoot(), containsFiles( "a" ) );
  }
}
