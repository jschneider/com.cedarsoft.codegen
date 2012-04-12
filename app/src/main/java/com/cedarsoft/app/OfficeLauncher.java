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

package com.cedarsoft.app;


import javax.annotation.Nonnull;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * <p>OfficeLauncher class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class OfficeLauncher {
  @Nonnull
  public static final String OPEN_OFFICE_WINDOWS_2_3 = "c:\\Programme\\Openoffice.org 2.3\\program\\soffice.exe";
  @Nonnull
  public static final String OPEN_OFFICE_WINDOWS = "c:\\Programme\\Openoffice.org 2.4\\program\\soffice.exe";
  @Nonnull
  public static final String OPEN_OFFICE_LINUX = "/usr/bin/soffice";
  @Nonnull
  public static final String EXCEL_WINDOWS = "c:\\Programme\\Microsoft Office\\Office\\excel.exe";

  @Nonnull
  private static final List<String> writerBins = new ArrayList<String>();
  @Nonnull
  private static final List<String> spreadsheetBins = new ArrayList<String>();

  private OfficeLauncher() {
  }

  static {
    writerBins.add( OPEN_OFFICE_LINUX );
    writerBins.add( OPEN_OFFICE_WINDOWS );
    writerBins.add( OPEN_OFFICE_WINDOWS_2_3 );

    spreadsheetBins.add( OPEN_OFFICE_LINUX );
    spreadsheetBins.add( OPEN_OFFICE_WINDOWS );
    spreadsheetBins.add( OPEN_OFFICE_WINDOWS_2_3 );
    spreadsheetBins.add( EXCEL_WINDOWS );
  }

  @Nonnull
  public static List<? extends String> getSpreadsheetBins() {
    return Collections.unmodifiableList( spreadsheetBins );
  }

  @Nonnull
  public static List<? extends String> getWriterBins() {
    return Collections.unmodifiableList( writerBins );
  }

  /**
   * <p>openWriter</p>
   *
   * @param file a {@link File} object.
   * @return a {@link Process} object.
   *
   * @throws IOException if any.
   */
  @Nonnull
  public static Process openWriter( @Nonnull File file ) throws IOException {
    return openFile( writerBins, file );
  }

  /**
   * <p>openSpreadsheet</p>
   *
   * @param file a {@link File} object.
   * @return a {@link Process} object.
   *
   * @throws IOException if any.
   */
  @Nonnull
  public static Process openSpreadsheet( @Nonnull File file ) throws IOException {
    return openFile( spreadsheetBins, file );
  }

  @Nonnull
  private static Process openFile( @Nonnull Iterable<? extends String> possibleBins, @Nonnull File file ) throws IOException {
    for ( String possibleBin : possibleBins ) {
      if ( new File( possibleBin ).exists() ) {
        return Runtime.getRuntime().exec( new String[]{possibleBin, file.getAbsolutePath()} );
      }
    }
    throw new IllegalStateException( "No Office installation found..." );
  }
}
