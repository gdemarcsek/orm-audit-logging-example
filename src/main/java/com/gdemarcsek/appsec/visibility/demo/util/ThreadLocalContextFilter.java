package com.gdemarcsek.appsec.visibility.demo.util;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Context;
import javax.ws.rs.ext.Provider;

import com.gdemarcsek.appsec.visibility.demo.core.User;

import java.io.IOException;
import java.security.Principal;

import java.util.Optional;

import org.slf4j.MDC;

@Provider
public class ThreadLocalContextFilter implements ContainerRequestFilter {
    @Context
    private HttpServletRequest httpRequest;

    private void addAuthenticationInformation(ContainerRequestContext requestContext) throws IOException {
        Optional<Principal> authUser = Optional.ofNullable(requestContext.getSecurityContext().getUserPrincipal());
        if (authUser.isPresent()) {
            User user = (User) authUser.get();
            AppRequestContext.setCurrentUser(user);
            MDC.put("authUser", user.getId());
        }

    }

    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        this.addAuthenticationInformation(requestContext);
    }
}
