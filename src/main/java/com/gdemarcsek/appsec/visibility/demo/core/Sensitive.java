package com.gdemarcsek.appsec.visibility.demo.core;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.Value;

@JsonIgnoreType
@Value
public class Sensitive<T> {
      private transient T _value;
      private transient boolean readOnce;
      
      public Sensitive(T val, boolean readOnce) {
            this._value = val;
            this.readOnce = readOnce;
      }
}
