package com.gdemarcsek.appsec.visibility.demo.resources;

import com.gdemarcsek.appsec.visibility.demo.core.Person;
import com.gdemarcsek.appsec.visibility.demo.db.PersonDAO;
import com.gdemarcsek.appsec.visibility.demo.presentation.CreatePersonDto;
import com.gdemarcsek.appsec.visibility.demo.presentation.GetPersonDto;

import io.dropwizard.hibernate.UnitOfWork;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.modelmapper.ModelMapper;


@Path("/people")
@Produces(MediaType.APPLICATION_JSON)
public class PersonResource {
    private final PersonDAO peopleDAO;

    private final ModelMapper modelMapper;

    public PersonResource(PersonDAO dao, ModelMapper mm) {
        this.peopleDAO = dao;
        this.modelMapper = mm;
    }

    @POST
    @UnitOfWork
    @RolesAllowed({ "ADMIN" })
    @Valid
    public GetPersonDto addPerson(@Valid CreatePersonDto person) {
        Person p = this.peopleDAO.create(modelMapper.map(person, Person.class));
        return modelMapper.map(p, GetPersonDto.class);
    }
}