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

package de.xore.util.servlets;

import de.xore.util.SessionContext;
import de.xore.util.XoreException;
import de.xore.util.velocity.XoreVelocityFormatter;
import org.apache.velocity.Template;
import org.apache.velocity.context.Context;
import org.apache.velocity.servlet.VelocityServlet;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Enumeration;

/*
 * Created by XoreSystems (Johannes Schneider).
 * User: Johannes
 * Date: 20.12.2003
 * Time: 16:55:59
 *
 */

/**
 * <p/>
 * Date: 20.12.2003<br> Time: 16:55:59<br>
 *
 * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
 *         Systems</a>
 */
public abstract class LayoutServlet extends VelocityServlet {
  @Nonnull
  public static final String CONTENT_TEMPLATE_NAME = "contentTemplateName";
  @Nonnull
  private static final String EXCEPTION = "exception";
  @Nonnull
  private static final String SESSION_CONTEXT = "context";
  @Nonnull
  private static final String FORMATTER = "format";
  @Nonnull
  protected static final String REDIRECT_URL = "REDIRECT_URL";

  protected boolean isStringEmpty( String string ) {
    return string == null || string.length() == 0;
  }

  @Override
  protected void error( @Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Exception cause ) throws ServletException, IOException {
    StringBuilder html = new StringBuilder();

    html.append( "<html>" );
    html.append( "<body bgcolor=\"#ffffff\">" );
    html.append( "<h2>Fehler</h2>" );
    html.append( "<pre>" );
    html.append( cause );
    html.append( "<br>" );

    StringWriter sw = new StringWriter();
    cause.printStackTrace( new PrintWriter( sw ) );

    html.append( sw.toString() );
    html.append( "</pre>" );

    Enumeration<?> attributeNames = request.getSession().getAttributeNames();
    while ( attributeNames.hasMoreElements() ) {
      String name = ( String ) attributeNames.nextElement();
      html.append( "<li>" );
      html.append( name );
      html.append( ':' );
      html.append( request.getSession().getAttribute( name ) );
      html.append( "</li>" );
    }

    html.append( "</body>" );
    html.append( "</html>" );
    response.getOutputStream().print( html.toString() );
  }

  @Override
  protected Template handleRequest( @Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Context ctx ) throws Exception {
    HttpSession session = request.getSession();
    SessionContext sessionContext = getSessionContext( session );
    refreshSessionContext( sessionContext );
    checkSecurity( sessionContext );

    ctx.put( SESSION_CONTEXT, sessionContext );
    ctx.remove( FORMATTER );
    ctx.put( FORMATTER, new XoreVelocityFormatter( ctx, request, response, sessionContext ) );
    try {
      //Diese Methode wird in der Sub-Klasse implementiert.
      handle( request, response, ctx, session, sessionContext );
    } catch ( XoreException e ) {
      postProcessingContext( request, response, ctx );
      ctx.put( EXCEPTION, e );
      ctx.put( CONTENT_TEMPLATE_NAME, "error.vm" );
      return getLayoutTemplate();
    }

    ctx.put( CONTENT_TEMPLATE_NAME, getContentTemplateName( request ) );
    postProcessingContext( request, response, ctx );

    if ( redirectUrl( request ) ) {
      response.sendRedirect( response.encodeRedirectURL( sessionContext.getRoot() + '/' + getRedirectUrl( request ) ) );
    }
    return getLayoutTemplate();
  }

  @Nullable
  @Nonnull
  private String getRedirectUrl( @Nonnull HttpServletRequest request ) {
    if ( getRedirectUrl() != null ) {
      return getRedirectUrl();
    }
    return ( String ) request.getAttribute( REDIRECT_URL );
  }

  private boolean redirectUrl( @Nonnull HttpServletRequest request ) {
    return getRedirectUrl( request ) != null;
  }

  @Nullable
  @Nonnull
  protected String getRedirectUrl() {
    return null;
  }

  /**
   * Nach Ablauf von handle() wird diese Methode durchgefuehrt. Dient z.B. zum Einhaengen von Standard-Werten in den
   * Context
   * <p/>
   * Z.B. einhaengen des SessioNContext
   *
   * @param ctx      the context
   * @param request  the request
   * @param response the response
   */
  protected abstract void postProcessingContext( @Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Context ctx );

  /**
   * Sicherheits-Ueberpruefungen (statisch bzw. deklarativ) koennen hier stattfinden. Wird direkt nach
   * refreshSessionContext und vor handle() aufgerufen.
   *
   * @param sessionContext thhe session context
   * @throws XoreException the exception
   */
  public abstract void checkSecurity( @Nonnull SessionContext sessionContext ) throws XoreException;

  /**
   * Vor dem Aufruf von handle() werden hier alle persistenten Objekte (Hibernate) aktualisiert.
   */
  public abstract void refreshSessionContext( @Nonnull SessionContext sessionContext );

  /**
   * Liefert den SessionContext zu einer bestimmten Session bzw. legt ihn neu an
   */
  public abstract SessionContext getSessionContext( @Nonnull HttpSession session );

  /**
   * Wird vom einzelnen Servlet ueberschrieben
   */
  protected abstract String getContentTemplateName( @Nonnull HttpServletRequest request );

  /**
   * Liefert das "Haupt-Template", welches entsprechend das von getContentTemplateName zurückgegebene Template
   * inkludiert
   * @return the Template
   * @throws Exception
   */
  public abstract Template getLayoutTemplate() throws Exception;

  protected abstract void handle( @Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Context ctx, @Nonnull HttpSession session, @Nonnull SessionContext sessionContext ) throws Exception;
}
