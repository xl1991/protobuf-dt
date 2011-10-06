/*
 * Copyright (c) 2011 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.google.eclipse.protobuf.model.util;

import static com.google.eclipse.protobuf.junit.core.Setups.unitTestSetup;
import static com.google.eclipse.protobuf.junit.core.XtextRule.createWith;
import static com.google.eclipse.protobuf.junit.model.find.Name.name;
import static com.google.eclipse.protobuf.junit.model.find.PropertyFinder.findProperty;
import static com.google.eclipse.protobuf.junit.model.find.Root.in;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import com.google.eclipse.protobuf.junit.core.XtextRule;
import com.google.eclipse.protobuf.protobuf.*;

import org.junit.*;

/**
 * Tests for <code>{@link Properties#typeNameOf(Property)}</code>.
 *
 * @author alruiz@google.com (Alex Ruiz)
 */
public class Properties_typeNameOf_Test {

  @Rule public XtextRule xtext = createWith(unitTestSetup());

  private Protobuf root;
  private Properties properties;

  @Before public void setUp() {
    root = xtext.root();
    properties = xtext.getInstanceOf(Properties.class);
  }

  // message Person {
  //   optional string name = 1;
  // }
  @Test public void should_return_name_of_scalar() {
    Property name = findProperty(name("name"), in(root));
    assertThat(properties.typeNameOf(name), equalTo("string"));
  }

  // message Person {
  //   optional string name = 1;
  //   optional PhoneNumber number = 2;
  //
  //   message PhoneNumber {
  //     optional string value = 1;
  //   }
  // }
  @Test public void should_return_name_of_type() {
    Property number = findProperty(name("number"), in(root));
    assertThat(properties.typeNameOf(number), equalTo("PhoneNumber"));
  }

}