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

package com.cedarsoft.commons.struct;

import com.cedarsoft.lookup.Lookup;
import com.cedarsoft.lookup.Lookups;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import java.lang.String;
import java.util.List;

/**
 * <p>DefaultNode class.</p>
 *
 * @author Johannes Schneider (<a href=mailto:js@cedarsoft.com>js@cedarsoft.com</a>)
 */
public class DefaultNode implements Node {
  @Nonnull
  private final String name;
  @Nonnull
  private final ChildrenSupport childrenSupport;
  @Nonnull
  private final Lookup lookup;
  @Nullable
  private Node parent;

  /**
   * <p>Constructor for DefaultNode.</p>
   *
   * @param name a {@link String} object.
   */
  public DefaultNode( @Nonnull String name ) {
    this( name, new DefaultChildrenSupport(), Lookups.emtyLookup() );
  }

  /**
   * <p>Constructor for DefaultNode.</p>
   *
   * @param name   a {@link String} object.
   * @param lookup a {@link Lookup} object.
   */
  public DefaultNode( @Nonnull String name, @Nonnull Lookup lookup ) {
    this( name, new DefaultChildrenSupport(), lookup );
  }

  /**
   * <p>Constructor for DefaultNode.</p>
   *
   * @param name            a {@link String} object.
   * @param childrenSupport a {@link ChildrenSupport} object.
   * @param lookup          a {@link Lookup} object.
   */
  public DefaultNode( @Nonnull String name, @Nonnull ChildrenSupport childrenSupport, @Nonnull Lookup lookup ) {
    this.lookup = lookup;
    this.childrenSupport = childrenSupport;
    this.childrenSupport.setParentNode( this );
    this.name = name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void setParent( @Nullable Node parent ) {
    if ( parent != null && !parent.isChild( this ) ) {
      throw new IllegalArgumentException( "invalid parent " + parent );
    }
    this.parent = parent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean isChild( @Nonnull StructPart child ) {
    return childrenSupport.isChild( child );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean hasParent() {
    return parent != null;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Node findChild( @Nonnull String childName ) throws ChildNotFoundException {
    return childrenSupport.findChild( childName );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public String getName() {
    return name;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Lookup getLookup() {
    return lookup;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public List<? extends Node> getChildren() {
    return childrenSupport.getChildren();
  }

  /**
   * <p>setChildren</p>
   *
   * @param children a {@link List} object.
   */
  public void setChildren( @Nonnull List<? extends Node> children ) {
    childrenSupport.setChildren( children );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addChild( int index, @Nonnull Node child ) {
    childrenSupport.addChild( index, child );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addChild( @Nonnull Node child ) {
    childrenSupport.addChild( child );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void detachChild( @Nonnull Node child ) {
    childrenSupport.detachChild( child );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void detachChild( int index ) {
    childrenSupport.detachChild( index );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nullable
  public Node getParent() {
    return parent;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  @Nonnull
  public Path getPath() {
    return Path.buildPath( this );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addStructureListener( @Nonnull StructureListener structureListener ) {
    childrenSupport.addStructureListener( structureListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void addStructureListenerWeak( @Nonnull StructureListener structureListener ) {
    childrenSupport.addStructureListenerWeak( structureListener );
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void removeStructureListener( @Nonnull StructureListener structureListener ) {
    childrenSupport.removeStructureListener( structureListener );
  }

  @Nonnull
  ChildrenSupport getChildrenSupport() {
    return childrenSupport;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String toString() {
    return "DefaultNode{" +
      "name='" + name + '\'' +
      ", parent=" + parent +
      ", children.size=" + childrenSupport.getChildren().size() +
      '}';
  }

  /**
   * Detaches all children
   */
  public void detachChildren() {
    childrenSupport.detachChildren();
  }
}
