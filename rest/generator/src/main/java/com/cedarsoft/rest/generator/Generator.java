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

package com.cedarsoft.rest.generator;

import com.cedarsoft.codegen.CodeGenerator;
import com.cedarsoft.codegen.NamingSupport;
import com.cedarsoft.codegen.model.DomainObjectDescriptor;
import com.cedarsoft.codegen.model.FieldWithInitializationInfo;
import com.cedarsoft.id.NameSpaceSupport;
import com.sun.codemodel.JClass;
import com.sun.codemodel.JClassAlreadyExistsException;
import com.sun.codemodel.JDefinedClass;
import com.sun.codemodel.JExpr;
import com.sun.codemodel.JFieldVar;
import com.sun.codemodel.JMethod;
import com.sun.codemodel.JMod;
import com.sun.codemodel.JVar;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 */
public class Generator extends AbstractGenerator {

  public Generator( @NotNull CodeGenerator<JaxbObjectGenerator.MyDecisionCallback> codeGenerator, @NotNull DomainObjectDescriptor descriptor ) {
    super( codeGenerator, descriptor );
  }

  public void generate() throws JClassAlreadyExistsException {
    JDefinedClass jaxbClass = codeGenerator.getModel()._class( getJaxbClassName() );
    jaxbClass.annotate( XmlRootElement.class ).param( "namespace", NameSpaceSupport.createNameSpaceUriBase( descriptor.getQualifiedName() ) );

    for ( FieldWithInitializationInfo fieldInfo : descriptor.getFieldsToSerialize() ) {
      JClass fieldType = codeGenerator.ref( fieldInfo.getType() );
      JFieldVar field = addField( jaxbClass, fieldType, fieldInfo );
      addGetter( jaxbClass, fieldType, fieldInfo, field );
      addSetter( jaxbClass, fieldType, fieldInfo, field );
    }
  }

  private void addSetter( @NotNull JDefinedClass jaxbClass, @NotNull JClass fieldType, @NotNull FieldWithInitializationInfo fieldInfo, @NotNull JFieldVar field ) {
    JMethod setter = jaxbClass.method( JMod.PUBLIC, Void.TYPE, NamingSupport.createSetter( fieldInfo.getSimpleName() ) );
    JVar param = setter.param( fieldType, fieldInfo.getSimpleName() );
    setter.body().assign( JExpr._this().ref( field ), param );
  }

  private void addGetter( @NotNull JDefinedClass jaxbClass, @NotNull JClass fieldType, @NotNull FieldWithInitializationInfo fieldInfo, @NotNull JFieldVar field ) {
    JMethod getter = jaxbClass.method( JMod.PUBLIC, fieldType, NamingSupport.createGetterName( fieldInfo.getSimpleName() ) );
    getter.body()._return( field );
  }

  @NotNull
  private JFieldVar addField( @NotNull JDefinedClass jaxbClass, @NotNull JClass fieldType, @NotNull FieldWithInitializationInfo fieldInfo ) {
    return jaxbClass.field( JMod.PRIVATE, fieldType, fieldInfo.getSimpleName() );
  }

}
