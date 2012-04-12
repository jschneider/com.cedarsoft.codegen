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

package com.cedarsoft.utils.excel;

import org.apache.poi.hssf.usermodel.HSSFCell;
import javax.annotation.Nonnull;

import java.util.Date;

/**
 *
 */
public enum CellTypes implements CellType {
  INTEGER {
    @Nonnull
    @Override
    public Integer getValue( @Nonnull HSSFCell cell ) {
      return ( int ) cell.getNumericCellValue();
    }},

  STRING {
    @Nonnull
    @Override
    public String getValue( @Nonnull HSSFCell cell ) throws NoValueFoundException {
      try {
        String value = cell.getRichStringCellValue().getString();
        if ( value == null ) {
          throw new NoValueFoundException( "No value found at: " + getCellLocationString( cell ) );
        }
        return value;
      } catch ( NumberFormatException ignore ) {
        return String.valueOf( ( int ) cell.getNumericCellValue() );
      }
    }},

  BOOLEAN {
    @Nonnull
    @Override
    public Boolean getValue( @Nonnull HSSFCell cell ) throws NoValueFoundException {
      try {
        return cell.getBooleanCellValue();
      } catch ( NumberFormatException ignore ) {
      }

      try {
        double numValue = cell.getNumericCellValue();
        //noinspection FloatingPointEquality
        return numValue == 1;
      } catch ( NumberFormatException ignore ) {
      }

      String asText = cell.getRichStringCellValue().getString();
      if ( asText.equalsIgnoreCase( "ja" ) ) {
        return true;
      }
      if ( asText.equalsIgnoreCase( "yes" ) ) {
        return true;
      }
      if ( asText.equalsIgnoreCase( "x" ) ) {
        return true;
      }
      if ( asText.equalsIgnoreCase( "1" ) ) {
        return true;
      }

      return false;
    }},

  DATE {
    @Override
    @Nonnull
    public Date getValue( @Nonnull HSSFCell cell ) throws NoValueFoundException {
      Date value = cell.getDateCellValue();
      if ( value == null ) {
        throw new NoValueFoundException( "No value found at: " + getCellLocationString( cell ) );
      }
      return value;
    }},

  DOUBLE {
    @Override
    @Nonnull
    public Double getValue( @Nonnull HSSFCell cell ) throws NoValueFoundException {
      return cell.getNumericCellValue();
    }};

  public static String getCellLocationString( @Nonnull HSSFCell cell ) {
    return String.valueOf( cell.getCellNum() );
  }
}
