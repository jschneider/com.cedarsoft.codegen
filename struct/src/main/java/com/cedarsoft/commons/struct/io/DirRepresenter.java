/**
 * Copyright (C) 2010 cedarsoft GmbH.
 *
 * Licensed under the GNU General Public License version 3 (the "License")
 * with Classpath Exception; you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 *         http://www.cedarsoft.org/gpl3ce.txt
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

package com.cedarsoft.commons.struct.io;

import com.cedarsoft.commons.struct.BreadthFirstStructureTreeWalker;
import com.cedarsoft.commons.struct.Node;
import com.cedarsoft.commons.struct.NodeFactory;
import com.cedarsoft.commons.struct.Path;
import com.cedarsoft.commons.struct.StructPart;
import com.cedarsoft.commons.struct.StructureTreeWalker;
import com.cedarsoft.lookup.Lookups;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 *
 */
public class DirRepresenter {
  @NotNull
  private final Node root;

  private final boolean rootVisible;

  public DirRepresenter( @NotNull Node root, boolean rootVisible ) {
    this.root = root;
    this.rootVisible = rootVisible;
  }

  /**
   * Stores the struct to the given base dir
   *
   * @param baseDir  the base dir
   * @param callback the callback that is called for each created directory
   */
  public void store( @NotNull final File baseDir, @Nullable final StoreCallback callback ) {
    if ( !baseDir.isDirectory() ) {
      throw new IllegalArgumentException( "Base dir is not a directory" );
    }

    StructureTreeWalker walker = new BreadthFirstStructureTreeWalker();
    walker.walk( root, new StructureTreeWalker.WalkerCallBack() {
      @Override
      public void nodeReached( @NotNull StructPart node, int level ) {
        Path path = Path.buildPath( node ); //pop the root
        if ( !rootVisible ) {
          path = path.popped();
        }

        File dir = getDir( baseDir, path );
        dir.mkdir();

        if ( callback != null ) {
          callback.dirCreated( node, path, dir );
        }
      }
    } );
  }

  /**
   * Parses the file structure and adds the nodes to the root
   *
   * @param baseDir     the base dir
   * @param nodeFactory the node factory used to create new nodes. The context will contain the directory (File) and the parent Node
   */
  public void parse( @NotNull File baseDir, @NotNull NodeFactory nodeFactory, int maxDepth ) {
    if ( !root.getChildren().isEmpty() ) {
      throw new IllegalStateException( "Root still has children!" );
    }

    if ( rootVisible ) {
      File[] subDirs = baseDir.listFiles( ( FileFilter ) DirectoryFileFilter.DIRECTORY );
      if ( subDirs.length != 1 ) {
        throw new IllegalStateException( "Invalid dirs count <" + Arrays.toString( subDirs ) + ">" );
      }
      File dir = subDirs[0];
      if ( !dir.getName().equals( root.getName() ) ) {
        throw new IllegalArgumentException( "Root node does not match dir. Node: <" + root.getName() + ">, Dir: <" + dir.getName() + '>' );
      }

      parse( root, dir, nodeFactory, maxDepth );
    } else {
      parse( root, baseDir, nodeFactory, maxDepth );
    }
  }

  protected void parse( @NotNull Node node, @NotNull File currentDir, @NotNull NodeFactory nodeFactory, int maxDepth ) {
    if ( maxDepth == 0 ) {
      return;
    }

    for ( File dir : listDirsSorted( currentDir ) ) {
      String name = dir.getName();

      Node child = nodeFactory.createNode( name, Lookups.dynamicLookup( dir, node ) );
      node.addChild( child );

      parse( child, dir, nodeFactory, maxDepth - 1 );
    }
  }

  @NotNull
  private static Iterable<? extends File> listDirsSorted( @NotNull File currentDir ) {
    List<File> subDirs = new ArrayList<File>( Arrays.asList( currentDir.listFiles( ( FileFilter ) DirectoryFileFilter.DIRECTORY ) ) );
    Collections.sort( subDirs );
    return subDirs;
  }

  /**
   * Returns the directory for the given base dir and path
   *
   * @param baseDir the base dir
   * @param path    the path
   * @return the dir
   */
  @NotNull
  protected static File getDir( @NotNull File baseDir, @NotNull Path path ) {
    File current = baseDir;
    for ( String element : path.getElements() ) {
      current = new File( current, element );
    }

    return current;
  }

  /**
   * Callback
   */
  public interface StoreCallback {
    /**
     * Is called every time a directory has been created
     *
     * @param node the node
     * @param path the path
     * @param dir  the directory that has been created
     */
    void dirCreated( @NotNull StructPart node, @NotNull Path path, @NotNull File dir );
  }
}
