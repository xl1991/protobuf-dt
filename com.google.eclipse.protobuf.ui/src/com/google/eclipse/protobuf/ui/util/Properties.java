/*
 * Copyright (c) 2011 Google Inc.
 *
 * All rights reserved. This program and the accompanying materials are made available under the terms of the Eclipse
 * Public License v1.0 which accompanies this distribution, and is available at
 *
 * http://www.eclipse.org/legal/epl-v10.html
 */
package com.google.eclipse.protobuf.ui.util;

import static java.lang.Math.max;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Keyword;

import com.google.eclipse.protobuf.protobuf.*;
import com.google.eclipse.protobuf.ui.grammar.Keywords;
import com.google.inject.Inject;
import com.google.inject.Singleton;

/**
 * Utility methods re <code>{@link Property}</code>.
 *
 * @author alruiz@google.com (Alex Ruiz)
 */
@Singleton
public class Properties {

  @Inject private Keywords keywords;

  public boolean isPrimitive(Property p) {
    AbstractTypeReference r = p.getType();
    if (!(r instanceof ScalarTypeReference)) return false;
    String typeName = ((ScalarTypeReference) r).getScalar().getName();
    return !keywords.string().getValue().equals(typeName) && !keywords.bytes().getValue().equals(typeName);
  }

  /**
   * Indicates whether the given property is of type {@code bool}.
   * @param p the given property.
   * @return {@code true} if the given property is of type {@code bool}, {@code false} otherwise.
   */
  public boolean isBool(Property p) {
    return isOfScalarType(p, keywords.bool());
  }

  /**
   * Indicates whether the given property is of type {@code string}.
   * @param p the given property.
   * @return {@code true} if the given property is of type {@code string}, {@code false} otherwise.
   */
  public boolean isString(Property p) {
    return isOfScalarType(p, keywords.string());
  }
  
  private boolean isOfScalarType(Property p, Keyword typeKeyword) {
    return typeKeyword.getValue().equals(typeNameOf(p));
  }

  /**
   * Returns the name of the type of the given <code>{@link Property}</code>.
   * @param p the given {@code Property}.
   * @return the name of the type of the given {@code Property}.
   */
  public String typeNameOf(Property p) {
    AbstractTypeReference r = p.getType();
    if (r instanceof ScalarTypeReference) return ((ScalarTypeReference) r).getScalar().getName();
    if (r instanceof TypeReference) {
      Type type = ((TypeReference) r).getType();
      return type == null ? null : type.getName();
    }
    return r.toString();
  }

  /**
   * Calculates the tag number value for the given property. The calculated tag number value is the maximum of all the 
   * tag number values of the given property's siblings, plus one. The minimum tag number value is 1.
   * <p>
   * For example, in the following message:
   * 
   * <pre>
   * message Person {
   *   required string name = 1;
   *   optional string email = 2;
   *   optional PhoneNumber phone =
   * </pre>
   * 
   * The calculated tag number value for the property {@code PhoneNumber} will be 3.
   * </p>
   * @param p the given property.
   * @return the calculated value for the tag number of the given property.
   */
  public int calculateTagNumberOf(Property p) {
    int index = 0;
    for (EObject o : p.eContainer().eContents()) {
      if (o == p || !(o instanceof Property)) continue;
      Property c = (Property) o;
      index = max(index, c.getIndex());
    }
    return ++index;
  }
}