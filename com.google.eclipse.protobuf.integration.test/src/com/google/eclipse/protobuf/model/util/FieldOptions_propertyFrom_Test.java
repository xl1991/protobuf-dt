/*
 * Copyright (c) 2011 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.google.eclipse.protobuf.model.util;

import static com.google.eclipse.protobuf.junit.core.Setups.integrationTestSetup;
import static com.google.eclipse.protobuf.junit.core.XtextRule.createWith;
import static com.google.eclipse.protobuf.junit.model.find.FieldOptionFinder.findFieldOption;
import static com.google.eclipse.protobuf.junit.model.find.Name.name;
import static com.google.eclipse.protobuf.junit.model.find.Root.in;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import com.google.eclipse.protobuf.junit.core.XtextRule;
import com.google.eclipse.protobuf.protobuf.*;

import org.junit.*;

/**
 * Tests for <code>{@link FieldOptions#propertyFrom(FieldOption)}</code>.
 *
 * @author alruiz@google.com (Alex Ruiz)
 */
public class FieldOptions_propertyFrom_Test {

  @Rule public XtextRule xtext = createWith(integrationTestSetup());

  private Protobuf root;
  private FieldOptions fieldOptions;

  @Before public void setUp() {
    root = xtext.root();
    fieldOptions = xtext.getInstanceOf(FieldOptions.class);
  }

  // message Person {
  //   optional boolean active = 1 [deprecated = false];
  // }
  @Test public void should_return_property_of_native_field_option() {
    FieldOption option = findFieldOption(name("deprecated"), in(root));
    Property p = fieldOptions.propertyFrom(option);
    assertThat(p.getName(), equalTo("deprecated"));
  }

  // import 'google/protobuf/descriptor.proto';
  //
  // extend google.protobuf.FieldOptions {
  //   optional string encoding = 1000;
  // }
  //
  // message Person {
  //   optional boolean active = 1 [(encoding) = 'UTF-8'];
  // }
  @Test public void should_return_property_of_custom_field_option() {
    FieldOption option = findFieldOption(name("encoding"), in(root));
    Property p = fieldOptions.propertyFrom(option);
    assertThat(p.getName(), equalTo("encoding"));
  }
}