package com.gdemarcsek.appsec.visibility.demo.core;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import com.gdemarcsek.appsec.visibility.demo.util.AuditAccess;

import lombok.Data;

@Data
@Entity
@Table(name = "people")
@NamedQueries({ @NamedQuery(name = "com.example.helloworld.core.Person.findAll", query = "SELECT p FROM Person p") })
public class Person extends EntityBase {
        @Column(name = "fullName", nullable = false)
        @AuditAccess
        private String fullName;

        @Column(name = "jobTitle", nullable = false)
        private String jobTitle;

        @Column(name = "yearBorn")
        @Min(value = 0)
        @Max(value = 9999)
        private int yearBorn;

        public Person(String fullName, String jobTitle, int yearBorn) {
                this.fullName = fullName;
                this.jobTitle = jobTitle;
                this.yearBorn = yearBorn;
        }

        private Person() {
        }
}