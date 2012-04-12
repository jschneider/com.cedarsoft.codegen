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

//package de.xore.util.servlets;
//
//import de.xore.util.persistence.hibernate.HibernateHelper;
//import de.xore.util.persistence.hibernate.HibernateHelperImpl;
//import de.xore.util.persistence.hibernate.HibernateUtil;
//import org.apache.log4j.Category;
//import org.apache.velocity.context.Context;
//import org.hibernate.HibernateException;
//
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//
///*
//* Created by XoreSystems (Johannes Schneider).
//* User: Johannes
//* Date: 23.12.2003
//* Time: 20:00:58
//*
//*/
//
///**
// * Dieses Servlet stellt einene einfachen Weg dar, um eine HibernateQuery dazustellen.
// * <p/>
// * <p/>
// * Date: 23.12.2003<br> Time: 20:00:58<br>
// *
// * @author <a href="http://johannes-schneider.info">Johannes Schneider</a> - <a href="http://www.xore.de">Xore
// *         Systems</a>
// */
//public abstract class XoreServlet extends LayoutServlet {
//  private static final Category CAT = Category.getInstance( XoreServlet.class );
//
//  protected static final HibernateHelper HIBERNATE_HELPER = new HibernateHelperImpl();
//
//  @Override
//  protected void error( HttpServletRequest request, HttpServletResponse response, Exception cause ) throws ServletException, IOException {
//    try {
//      HibernateUtil.getInstance().closeSession();
//    } catch ( HibernateException e ) {
//      CAT.error( e );
//      throw new ServletException( e );
//    }
//
//    super.error( request, response, cause );
//  }
//
//  @Override
//  protected void requestCleanup( HttpServletRequest request, HttpServletResponse response, Context context ) {
//    try {
//      HibernateUtil.getInstance().closeSession();
//    } catch ( HibernateException e ) {
//      CAT.error( e );
//    }
//  }
//
//  public void delete( Object obj ) throws HibernateException {
//    HIBERNATE_HELPER.delete( obj );
//  }
//
//  public void refresh( Object obj ) throws HibernateException {
//    HIBERNATE_HELPER.refresh( obj );
//  }
//
//  public void save( Object obj ) throws HibernateException {
//    HIBERNATE_HELPER.save( obj );
//  }
//
//  public void saveOrUpdate( Object obj ) throws HibernateException {
//    HIBERNATE_HELPER.saveOrUpdate( obj );
//  }
//
//  public void update( Object obj ) throws HibernateException {
//    HIBERNATE_HELPER.update( obj );
//  }
//}
