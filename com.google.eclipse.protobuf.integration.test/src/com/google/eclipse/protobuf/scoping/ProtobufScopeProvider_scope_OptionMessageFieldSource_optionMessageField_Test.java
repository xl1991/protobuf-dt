/*
 * Copyright (c) 2011 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.google.eclipse.protobuf.scoping;

import static com.google.eclipse.protobuf.junit.core.Setups.integrationTestSetup;
import static com.google.eclipse.protobuf.junit.core.XtextRule.createWith;
import static com.google.eclipse.protobuf.scoping.ContainAllFieldsInMessage.containAllFieldsIn;
import static com.google.eclipse.protobuf.scoping.IEObjectDescriptions.descriptionsIn;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;

import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.scoping.IScope;
import org.junit.*;

import com.google.eclipse.protobuf.junit.core.XtextRule;
import com.google.eclipse.protobuf.protobuf.*;

/**
 * Tests for <code>{@link ProtobufScopeProvider#scope_OptionMessageFieldSource_optionMessageField(OptionMessageFieldSource, EReference)}</code>.
 *
 * @author alruiz@google.com (Alex Ruiz)
 */
public class ProtobufScopeProvider_scope_OptionMessageFieldSource_optionMessageField_Test {

  private static EReference reference;

  @BeforeClass public static void setUpOnce() {
    reference = mock(EReference.class);
  }

  @Rule public XtextRule xtext = createWith(integrationTestSetup());

  private ProtobufScopeProvider provider;

  @Before public void setUp() {
    provider = xtext.getInstanceOf(ProtobufScopeProvider.class);
  }

  // import 'google/protobuf/descriptor.proto';
  //
  // message Type {
  //   optional double code = 1;
  //   optional string name = 2;
  // }
  //
  // extend google.protobuf.FileOptions {
  //   optional Type type = 1000;
  // }
  //
  // option (type).code = 68;
  @Test public void should_provide_message_fields_for_first_field_in_custom_option() {
    CustomOption option = xtext.find("type", ")", CustomOption.class);
    OptionMessageFieldSource optionField = (OptionMessageFieldSource) option.getOptionFields().get(0);
    IScope scope = provider.scope_OptionMessageFieldSource_optionMessageField(optionField, reference);
    Message typeMessage = xtext.find("Type", " {", Message.class);
    assertThat(descriptionsIn(scope), containAllFieldsIn(typeMessage));
  }

  // import 'google/protobuf/descriptor.proto';
  //
  // extend google.protobuf.FileOptions {
  //   optional group Type = 1000 {
  //     optional double code = 1001;
  //     optional string name = 1002;
  //   }
  // }
  //
  // option (Type).code = 68;
  @Test public void should_provide_group_fields_for_first_field_in_custom_option() {
    CustomOption option = xtext.find("Type", ")", CustomOption.class);
    OptionMessageFieldSource optionField = (OptionMessageFieldSource) option.getOptionFields().get(0);
    IScope scope = provider.scope_OptionMessageFieldSource_optionMessageField(optionField, reference);
    Group groupMessage = xtext.find("Type", " =", Group.class);
    assertThat(descriptionsIn(scope), containAllFieldsIn(groupMessage));
  }

  // import 'google/protobuf/descriptor.proto';
  //
  // message Type {
  //   optional double code = 1;
  //   optional string name = 2;
  // }
  //
  // extend google.protobuf.FieldOptions {
  //   optional Type type = 1000;
  // }
  //
  // message Person {
  //   optional boolean active = 1 [(type).code = 68];
  // }
  @Test public void should_provide_message_fields_for_first_field_in_field_custom_option() {
    CustomFieldOption option = xtext.find("type", ")", CustomFieldOption.class);
    OptionMessageFieldSource optionField = (OptionMessageFieldSource) option.getOptionFields().get(0);
    IScope scope = provider.scope_OptionMessageFieldSource_optionMessageField(optionField, reference);
    Message typeMessage = xtext.find("Type", " {", Message.class);
    assertThat(descriptionsIn(scope), containAllFieldsIn(typeMessage));
  }


  // import 'google/protobuf/descriptor.proto';
  //
  // extend google.protobuf.FieldOptions {
  //   optional group Type = 1000 {
  //     optional double code = 1001;
  //     optional string name = 1002;
  //   }
  // }
  //
  // message Person {
  //   optional boolean active = 1 [(Type).code = 68];
  // }
  @Test public void should_provide_group_fields_for_first_field_in_field_custom_option() {
    CustomFieldOption option = xtext.find("Type", ")", CustomFieldOption.class);
    OptionMessageFieldSource optionField = (OptionMessageFieldSource) option.getOptionFields().get(0);
    IScope scope = provider.scope_OptionMessageFieldSource_optionMessageField(optionField, reference);
    Group groupMessage = xtext.find("Type", " =", Group.class);
    assertThat(descriptionsIn(scope), containAllFieldsIn(groupMessage));
  }
}
