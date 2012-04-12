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

package de.xore.util.html;

/**
 * <p/>
 * Date: 17.06.2006<br>
 * Time: 14:16:23<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> -
 *         <a href="http://www.xore.de">Xore Systems</a>
 */
public class HtmlEncoder {
  private HtmlEncoder() {
  }

  public static final String escapeHTML( String nonHtml ) {
    StringBuilder sb = new StringBuilder();
    int n = nonHtml.length();
    for ( int i = 0; i < n; i++ ) {
      char c = nonHtml.charAt( i );
      switch ( c ) {
        case '<':
          sb.append( "&lt;" );
          break;
        case '>':
          sb.append( "&gt;" );
          break;
        case '&':
          sb.append( "&amp;" );
          break;
        case '"':
          sb.append( "&quot;" );
          break;
        case '\u00e0':
          sb.append( "&agrave;" );
          break;
        case '\u00c0':
          sb.append( "&Agrave;" );
          break;
        case '\u00e2':
          sb.append( "&acirc;" );
          break;
        case '\u00c2':
          sb.append( "&Acirc;" );
          break;
        case '\u00e4':
          sb.append( "&auml;" );
          break;
        case '\u00c4':
          sb.append( "&Auml;" );
          break;
        case '\u00e5':
          sb.append( "&aring;" );
          break;
        case '\u00c5':
          sb.append( "&Aring;" );
          break;
        case '\u00e6':
          sb.append( "&aelig;" );
          break;
        case '\u00c6':
          sb.append( "&AElig;" );
          break;
        case '\u00e7':
          sb.append( "&ccedil;" );
          break;
        case '\u00c7':
          sb.append( "&Ccedil;" );
          break;
        case '\u00e9':
          sb.append( "&eacute;" );
          break;
        case '\u00c9':
          sb.append( "&Eacute;" );
          break;
        case '\u00e8':
          sb.append( "&egrave;" );
          break;
        case '\u00c8':
          sb.append( "&Egrave;" );
          break;
        case '\u00ea':
          sb.append( "&ecirc;" );
          break;
        case '\u00ca':
          sb.append( "&Ecirc;" );
          break;
        case '\u00eb':
          sb.append( "&euml;" );
          break;
        case '\u00cb':
          sb.append( "&Euml;" );
          break;
        case '\u00ef':
          sb.append( "&iuml;" );
          break;
        case '\u00cf':
          sb.append( "&Iuml;" );
          break;
        case '\u00f4':
          sb.append( "&ocirc;" );
          break;
        case '\u00d4':
          sb.append( "&Ocirc;" );
          break;
        case '\u00f6':
          sb.append( "&ouml;" );
          break;
        case '\u006d':
          sb.append( "&Ouml;" );
          break;
        case '\u00f8':
          sb.append( "&oslash;" );
          break;
        case '\u00d8':
          sb.append( "&Oslash;" );
          break;
        case '\u00df':
          sb.append( "&szlig;" );
          break;
        case '\u00f9':
          sb.append( "&ugrave;" );
          break;
        case '\u00d9':
          sb.append( "&Ugrave;" );
          break;
        case '\u00fb':
          sb.append( "&ucirc;" );
          break;
        case '\u00db':
          sb.append( "&Ucirc;" );
          break;
        case '\u00fc':
          sb.append( "&uuml;" );
          break;
        case '\u00dc':
          sb.append( "&Uuml;" );
          break;
        case '\u00ae':
          sb.append( "&reg;" );
          break;
        case '\u00a9':
          sb.append( "&copy;" );
          break;
        case '\u20ac':
          sb.append( "&euro;" );
          break;
        //          // be carefull with this one (non-breaking whitee space)
        //        case' ':
        //          sb.append( "&nbsp;" );
        //          break;

        default:
          sb.append( c );
          break;
      }
    }
    return sb.toString();
  }
}
