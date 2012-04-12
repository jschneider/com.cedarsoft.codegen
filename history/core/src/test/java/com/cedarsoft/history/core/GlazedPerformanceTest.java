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

package com.cedarsoft.history.core;

import ca.odell.glazedlists.GlazedLists;
import javax.annotation.Nonnull;
import org.springframework.util.StopWatch;
import org.junit.*;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Checks the performance of the glazed lists
 */
public class GlazedPerformanceTest {
  @Test
  public void testTiming() {
    for ( int i = 0; i < 5; i++ ) {
      checkPerformance( new ArrayList<String>() );
      checkPerformance( GlazedLists.<String>eventList( new ArrayList<String>() ) );
    }

    StopWatch watch0 = checkPerformance( new ArrayList<String>() );
    System.out.println( watch0.prettyPrint() );
    StopWatch watch1 = checkPerformance( GlazedLists.<String>eventList( new ArrayList<String>() ) );
    System.out.println( watch1.prettyPrint() );

    System.out.println( "Delta: " + ( watch1.getTotalTimeMillis() - watch0.getTotalTimeMillis() ) );
  }

  private static StopWatch checkPerformance( @Nonnull List<String> list ) {
    StopWatch stopWatch = new StopWatch();

    stopWatch.start( "adding to " + list.getClass().getName() );
    for ( int i = 0; i < 100000; i++ ) {
      list.add( String.valueOf( i ) );
    }
    stopWatch.stop();

    stopWatch.start( "iterating through " + list.getClass().getName() );
    for ( String currentEntry : list ) {
      assertNotNull( currentEntry );
    }
    stopWatch.stop();

    assertNotNull( stopWatch );
    return stopWatch;
  }
}
