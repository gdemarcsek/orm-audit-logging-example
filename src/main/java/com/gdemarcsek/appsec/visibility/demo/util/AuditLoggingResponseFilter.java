package com.gdemarcsek.appsec.visibility.demo.util;

import java.io.IOException;

import java.lang.reflect.Type;
import java.lang.reflect.Modifier;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;

import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;

import com.gdemarcsek.appsec.visibility.demo.core.EntityBase;

import org.slf4j.MDC;

@Slf4j
public class AuditLoggingResponseFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException, IllegalArgumentException {

        Type entityType = responseContext.getEntityType();

        if (!Arrays.stream(entityType.getClass().getDeclaredFields())
                .anyMatch(f -> Modifier.isPublic(f.getModifiers()))) {
            Object responseEntity = responseContext.getEntity();
            if (responseEntity != null && EntityBase.class.isAssignableFrom(responseEntity.getClass())) {
                MDC.put("accessedEntityId", ((EntityBase) responseContext.getEntity()).getId().toString());
            }

            log.info("sensitive entity accessed");
        }

    }

}
