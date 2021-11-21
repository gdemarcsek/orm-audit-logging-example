package com.gdemarcsek.appsec.visibility.demo.presentation;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import com.gdemarcsek.appsec.visibility.demo.util.AuditAccess;
import com.gdemarcsek.appsec.visibility.demo.core.Sensitive;

@Data
@AuditAccess
public class GetPersonDto {
      @NotNull
      @JsonProperty("name")
      private Sensitive<String> fullName;

      private int yearBorn;

      @NotNull
      private String id;
}
