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

import javax.swing.JOptionPane;

/**
 * <p>PdfViewerLauncher class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class PdfViewerLauncher {
  private static final String ERR_MSG = "Error attempting to launch pdf viewer";

  private PdfViewerLauncher() {
  }

  /**
   * Opens a browser
   *
   * @param file the url that is opened within the browser
   */
  public static void openFile( @Nonnull String file ) {
    String osName = getOsName();
    try {
      if ( osName.startsWith( "Mac OS" ) ) {
        throw new UnsupportedOperationException( "Not implemented for Mac OS yet" );
        //        Class<?> fileMgr = Class.forName( "com.apple.eio.FileManager" );
        //        Method openFile = fileMgr.getDeclaredMethod( "openFile", new Class[]{String.class} );
        //        openFile.invoke( null, file );
      } else if ( osName.startsWith( "Windows" ) ) {
        Runtime.getRuntime().exec( "start \"" + file + "\"" );
      } else {
        //assume Unix or Linux
        String[] bins = {"acroread", "evince"};
        String browser = null;
        for ( int count = 0; count < bins.length && browser == null; count++ ) {
          if ( Runtime.getRuntime().exec( new String[]{"which", bins[count]} ).waitFor() == 0 ) {
            browser = bins[count];
          }
        }
        if ( browser == null ) {
          throw new Exception( "Could not find pdf viewer" );
        } else {
          Runtime.getRuntime().exec( new String[]{browser, file} );
        }
      }
    } catch ( Exception e ) {
      JOptionPane.showMessageDialog( null, ERR_MSG + ":\n" + e.getLocalizedMessage() );
    }
  }

  @Nonnull
  protected static String getOsName() {
    return System.getProperty( "os.name" );
  }
}
