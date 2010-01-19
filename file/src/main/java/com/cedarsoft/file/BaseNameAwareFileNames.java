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

package com.cedarsoft.file;

import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 */
public class BaseNameAwareFileNames {
  @NotNull
  private final SortedMap<BaseName, FileNames> entries = new TreeMap<BaseName, FileNames>();

  public void add( @NotNull FileName fileName ) {
    getEntry( fileName.getBaseName() ).add( fileName );
  }

  @NotNull
  public Collection<? extends Map.Entry<BaseName, FileNames>> getEntries() {
    return Collections.unmodifiableCollection( entries.entrySet() );
  }

  @NotNull
  public FileNames getEntry( @NotNull @NonNls BaseName baseName ) {
    FileNames found = entries.get( baseName );
    if ( found != null ) {
      return found;
    }

    FileNames created = new FileNames();
    entries.put( baseName, created );
    return created;
  }

  public int size() {
    return entries.size();
  }
}
