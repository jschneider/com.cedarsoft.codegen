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

package com.cedarsoft.codegen;

import com.cedarsoft.codegen.parser.Classpath;
import com.cedarsoft.execution.Executor;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Files;
import com.sun.tools.xjc.api.util.APTClassLoader;
import com.sun.tools.xjc.api.util.ToolsJarNotFoundException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.fest.reflect.core.Reflection;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.List;

/**
 * Abstract base class for Generators.
 * Handles options and classloader issues
 */
public abstract class AbstractGenerator {
  @Nonnull
  public static final String GENERATE_METHOD_NAME = "generate";


  protected void run( @Nonnull Collection<? extends File> domainSourceFiles, @Nonnull File destination, @Nonnull File resourcesDestination, @Nonnull File testDestination, @Nonnull File testResourcesDestination, @Nullable Classpath classpath, @Nonnull PrintWriter logOut ) throws ToolsJarNotFoundException, ClassNotFoundException, IOException, InterruptedException {
    GeneratorConfiguration configuration = new GeneratorConfiguration( domainSourceFiles, destination, resourcesDestination, testDestination, testResourcesDestination, classpath, logOut );

    File tmpDestination = createEmptyTmpDir();
    File tmpTestDestination = createEmptyTmpDir();

    File tmpResourcesDestination = createEmptyTmpDir();
    File tmpTestResourcesDestination = createEmptyTmpDir();

    GeneratorConfiguration tmpConfiguration = new GeneratorConfiguration( domainSourceFiles, tmpDestination, tmpResourcesDestination, tmpTestDestination, tmpTestResourcesDestination, classpath, logOut );

    //Now start the generator
    run( tmpConfiguration );

    transferFiles( tmpConfiguration, configuration );

    FileUtils.deleteDirectory( tmpDestination );
    FileUtils.deleteDirectory( tmpTestDestination );
  }

  public void run( @Nonnull GeneratorConfiguration configuration ) throws ToolsJarNotFoundException, ClassNotFoundException {
    run( configuration, createRunner() );
  }

  public void run( @Nonnull Class<?> runnerType, @Nonnull GeneratorConfiguration configuration ) throws ToolsJarNotFoundException, ClassNotFoundException {
    run( configuration, createRunner( runnerType ) );
  }

  public void run( @Nonnull GeneratorConfiguration configuration, @Nonnull Object runner ) {
    Reflection.method( GENERATE_METHOD_NAME ).withParameterTypes( GeneratorConfiguration.class ).in( runner ).invoke( configuration );
  }

  @Nonnull
  public Object createRunner() throws ToolsJarNotFoundException, ClassNotFoundException {
    Class<?> runnerType = getRunnerType();
    return createRunner( runnerType );
  }

  public Object createRunner( @Nonnull Class<?> runnerType ) {
    return Reflection.constructor().in( runnerType ).newInstance();
  }

  @Nonnull
  public Class<?> getRunnerType() throws ToolsJarNotFoundException, ClassNotFoundException {
    ClassLoader aptClassLoader = createAptClassLoader();
    Thread.currentThread().setContextClassLoader( aptClassLoader );

    return aptClassLoader.loadClass( getRunnerClassName() );
  }

  /**
   * Transfers the files
   *
   * @param tmpConfiguration the temporary configuration (the source)
   * @param configuration    the configuration (the target)
   * @throws IOException
   * @throws InterruptedException
   */
  public void transferFiles( @Nonnull GeneratorConfiguration tmpConfiguration, @Nonnull GeneratorConfiguration configuration ) throws IOException, InterruptedException {
    transferFiles( tmpConfiguration.getDestination(), configuration.getDestination(), configuration.getLogOut() );
    transferFiles( tmpConfiguration.getResourcesDestination(), configuration.getResourcesDestination(), configuration.getLogOut() );

    transferFiles( tmpConfiguration.getTestDestination(), configuration.getTestDestination(), configuration.getLogOut() );
    transferFiles( tmpConfiguration.getTestResourcesDestination(), configuration.getTestResourcesDestination(), configuration.getLogOut() );
  }

  @Nonnull
  public APTClassLoader createAptClassLoader() throws ToolsJarNotFoundException {
    return createAptClassLoader( getDefaultClassLoader() );
  }

  @Nonnull
  public APTClassLoader createAptClassLoader( @Nonnull ClassLoader defaultClassLoader ) throws ToolsJarNotFoundException {
    return new APTClassLoader( defaultClassLoader, getPackagePrefixes().toArray( new String[getPackagePrefixes().size()] ) );
  }

  @Nonnull
  public ClassLoader getDefaultClassLoader() {
    ClassLoader defaultClassLoader = getClass().getClassLoader();
    if ( defaultClassLoader == null ) {
      defaultClassLoader = ClassLoader.getSystemClassLoader();
    }
    return defaultClassLoader;
  }

  @Nonnull
  protected List<? extends String> getPackagePrefixes() {
    return ImmutableList.of(
      "com.cedarsoft.codegen.",
      "com.sun.istack.tools.",
      "com.sun.tools.apt.",
      "com.sun.tools.javac.",
      "com.sun.tools.javadoc.",
      "com.sun.mirror."
    );
  }

  protected void transferFiles( @Nonnull File sourceDir, @Nonnull File destination, @Nonnull PrintWriter logOut ) throws IOException, InterruptedException {
    Collection<? extends File> serializerFiles = FileUtils.listFiles( sourceDir, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE );
    for ( File serializerFile : serializerFiles ) {
      String relativePath = calculateRelativePath( sourceDir, serializerFile );

      File targetFile = new File( destination, relativePath );
      if ( targetFile.exists() ) {
        Executor executor = new Executor( new ProcessBuilder( "meld", targetFile.getAbsolutePath(), serializerFile.getAbsolutePath() ) );
        executor.execute();
      } else {
        //Create the directory if necessary
        File targetDir = targetFile.getParentFile();
        if ( !targetDir.isDirectory() ) {
          targetDir.mkdirs();
        }
        Files.move( serializerFile, targetFile );
      }
    }
  }

  @Nonnull
  protected static String calculateRelativePath( @Nonnull File dir, @Nonnull File serializerFile ) throws IOException {
    return serializerFile.getCanonicalPath().substring( dir.getCanonicalPath().length() + 1 );
  }

  @Nonnull
  public static File createEmptyTmpDir() {
    return Files.createTempDir();
  }

  @Nonnull
  protected abstract String getRunnerClassName();

  /**
   * The runner interface
   */
  public interface Runner {
    /**
     * Generates the code
     *
     * @param configuration the configuration
     * @throws Exception
     */
    void generate( @Nonnull GeneratorConfiguration configuration ) throws Exception;
  }
}
