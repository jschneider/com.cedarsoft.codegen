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

package com.cedarsoft.codegen;

import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.JPackage;

import javax.annotation.Nonnull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Johannes Schneider (<a href="mailto:js@cedarsoft.com">js@cedarsoft.com</a>)
 */
public class MemoryCodeWriter extends CodeWriter {
  @Nonnull
  private final Map<String, ByteArrayOutputStream> files = new HashMap<String, ByteArrayOutputStream>();

  @Override
  public OutputStream openBinary( JPackage pkg, String fileName ) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    files.put( pkg.name() + "." + fileName, out );
    return out;
  }

  @Nonnull
  public Map<String, ByteArrayOutputStream> getFiles() {
    return Collections.unmodifiableMap( files );
  }

  @Nonnull
  public String getFileContent( @Nonnull String packageName, @Nonnull String fileName ) {
    String fqnName = packageName + "." + fileName;
    ByteArrayOutputStream found = files.get( fqnName );
    if ( found == null ) {
      throw new IllegalArgumentException( "No file found for <" + fqnName + ">" );
    }

    return new String( found.toByteArray() );
  }

  @Override
  public void close() throws IOException {
  }

  @Nonnull
  public String allFilesToString() {
    List<String> sortedKeys = new ArrayList<String>( files.keySet() );
    Collections.sort( sortedKeys, new Comparator<String>() {
      @Override
      public int compare( String o1, String o2 ) {
        if ( o1.endsWith( ".java" ) && !o2.endsWith( ".java" ) ) {
          return -1;
        }
        if ( !o1.endsWith( ".java" ) && o2.endsWith( ".java" ) ) {
          return 1;
        }

        return o1.compareTo( o2 );
      }
    } );

    StringBuilder large = new StringBuilder();
    for ( String key : sortedKeys ) {
      large.append( "-----------------------------------" ).append( key ).append( "-----------------------------------\n" );
      large.append( files.get( key ) );
    }

    return large.toString();
  }
}
