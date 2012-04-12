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

package de.xore.utils;

/**
 * <p/>
 * Date: 08.07.2006<br>
 * Time: 20:26:59<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class StringUtils {
  private StringUtils() {
  }

  public static String wrap( String unwrapped ) {
    return createMultiLineText( unwrapped, 60, "<br>" );
  }

  public static String createMultiLineText( String text, int maxLineLength, String lineBreak ) {
    StringBuilder splitText = new StringBuilder( text.length() + 10 );
    String[] words = text.split( " " );

    int lineLength = 0;
    for ( String word : words ) {
      if ( lineLength + 1 + word.length() > maxLineLength ) {
        splitText.append( lineBreak );
        lineLength = 0;
      }
      splitText.append( word );
      splitText.append( ' ' );
      lineLength += word.length() + 1;
    }
    return splitText.toString();
  }

  /**
   * splits the text into multiple lines
   *
   * @param text          (in one line)
   * @param maxLineLength the maximal length of the resulting line
   * @return the string with "\n" between the words
   */
  public static String createMultiLineText( String text, int maxLineLength ) {
    return createMultiLineText( text, maxLineLength, "\n" );
  }
}
