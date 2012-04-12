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
import org.apache.poi.hssf.usermodel.HSSFRow;
import javax.annotation.Nonnull;

/**
 * A mapping for a spread sheet column
 */
public class SpreadSheetColumnMapping<T> {
  @Nonnull
  private final CellType type;
  private final short index;

  public SpreadSheetColumnMapping( int index, @Nonnull CellType type ) {
    this.type = type;
    this.index = ( short ) index;
  }

  public short getIndex() {
    return index;
  }

  @Nonnull
  public CellType getType() {
    return type;
  }

  @Nonnull
  public T getValue( @Nonnull HSSFCell cell ) {
    return ( T ) type.getValue( cell );
  }

  @Nonnull
  public T getValue( @Nonnull HSSFRow row ) throws NoValueFoundException {
    HSSFCell cell = row.getCell( getIndex() );
    if ( cell == null ) {
      throw new NoValueFoundException( "No cell found at index " + getIndex() + " for row " + row.getRowNum() );
    }
    return getValue( cell );
  }
}
