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

package de.xore.util.velocity;

import de.xore.util.SessionContext;
import org.apache.velocity.app.tools.VelocityFormatter;
import org.apache.velocity.context.Context;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/*
 * Created by XoreSystems (Johannes Schneider).
 * User: Johannes
 * Date: 11.05.2004
 * Time: 16:19:10
 *
 */

/**
 * <p/>
 * Date: 11.05.2004<br> Time: 16:19:10<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public class XoreVelocityFormatter extends VelocityFormatter {
  private HttpServletResponse response;
  private SessionContext sessionContext;
  @Nonnull
  private SimpleDateFormat monthYearFormatter = new SimpleDateFormat( "MMMM yyyy" );

  public XoreVelocityFormatter( Context ctx, HttpServletRequest request, HttpServletResponse response, SessionContext sessionContext ) {
    super( ctx );
    this.response = response;
    this.sessionContext = sessionContext;
  }

  public String formatLink( String url ) {
    return encodeUrl( url );
  }

  public String encodeUrl( String url ) {
    return response.encodeURL( sessionContext.getRoot() + '/' + url );
  }

  public String formatNumber( long bytes ) {
    return NumberFormat.getNumberInstance().format( bytes );
  }

  public String formatMonthYear( Calendar calendar ) {
    return monthYearFormatter.format( calendar.getTime() );
  }
}
