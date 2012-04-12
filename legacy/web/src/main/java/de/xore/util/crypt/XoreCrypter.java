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

package de.xore.util.crypt;

import java.security.MessageDigest;

/*
 * Created by XoreSystems (Johannes Schneider).
 * User: Johannes
 * Date: 05.05.2004
 * Time: 21:22:47
 *
 *************************************************
 *  $$Log: XoreCrypter.java,v $
 *  $Revision 1.1  2004/05/11 14:40:24  johannes
 *  $init
 *  $
 *************************************************
 */

/**
 * <p/>
 * Date: 05.05.2004<br> Time: 21:22:47<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public class XoreCrypter {
  private XoreCrypter() {
  }

  public static String encryp( String clearText ) {
    try {
      MessageDigest messageDigest = MessageDigest.getInstance( "MD5" );
      messageDigest.update( clearText.getBytes() );

      StringBuilder dig = new StringBuilder();
      byte[] digest = messageDigest.digest();
      for ( byte aDigest : digest ) {
        dig.append( Integer.toHexString( aDigest & 0xff ) );
      }
      return dig.toString();

    } catch ( Exception e ) {
      throw new RuntimeException( e );
    }
  }
}
