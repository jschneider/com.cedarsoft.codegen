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

package de.xore;

/*
 * Created by XoreSystems (Johannes Schneider).
 * User: Johannes
 * Date: 09.04.2004
 * Time: 00:58:08
 *
 *************************************************
 *  $$Log: XoreRuntimeException.java,v $
 *  $Revision 1.1  2004/05/11 14:40:24  johannes
 *  $init
 *  $
 *************************************************
 */

/**
 * <p/>
 * Date: 09.04.2004<br> Time: 00:58:08<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public class XoreRuntimeException extends RuntimeException {
  public XoreRuntimeException() {
  }

  public XoreRuntimeException( String message ) {
    super( message );
  }

  public XoreRuntimeException( Throwable cause ) {
    super( cause );
  }

  public XoreRuntimeException( String message, Throwable cause ) {
    super( message, cause );
  }

}
