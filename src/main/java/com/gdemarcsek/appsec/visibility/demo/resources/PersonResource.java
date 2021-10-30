package com.gdemarcsek.appsec.visibility.demo.resources;

import com.gdemarcsek.appsec.visibility.demo.core.Person;
import com.gdemarcsek.appsec.visibility.demo.db.PersonDAO;

import io.dropwizard.hibernate.UnitOfWork;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/people")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {
    private final PersonDAO peopleDAO;

    public PersonResource(PersonDAO peopleDAO) {
        this.peopleDAO = peopleDAO;
    }

    @POST
    @UnitOfWork
    @RolesAllowed({ "ADMIN" })
    public Person addPerson(@Valid Person person) {
        this.peopleDAO.create(person);
        return person;
    }
}