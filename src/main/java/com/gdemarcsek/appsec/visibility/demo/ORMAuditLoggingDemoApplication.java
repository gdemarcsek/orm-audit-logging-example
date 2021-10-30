package com.gdemarcsek.appsec.visibility.demo;

import com.gdemarcsek.appsec.visibility.demo.core.Person;
import com.gdemarcsek.appsec.visibility.demo.core.User;
import com.gdemarcsek.appsec.visibility.demo.db.*;
import com.gdemarcsek.appsec.visibility.demo.resources.*;
import com.gdemarcsek.appsec.visibility.demo.util.*;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.hibernate.SessionFactoryFactory;
import io.dropwizard.migrations.MigrationsBundle;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.auth.AuthValueFactoryProvider;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.google.common.collect.ImmutableList;


public class ORMAuditLoggingDemoApplication extends Application<ORMAuditLoggingDemoConfiguration> {
    private final HibernateBundle<ORMAuditLoggingDemoConfiguration> hibernateBundle = new HibernateBundle<ORMAuditLoggingDemoConfiguration>(
            ImmutableList.<Class<?>>of(Person.class), new SessionFactoryFactory() {
                @Override
                protected void configure(org.hibernate.cfg.Configuration configuration,
                        org.hibernate.service.ServiceRegistry registry) {
                    super.configure(configuration, registry);
                    configuration.setInterceptor(new AuditInterceptor());
                }
            }) {
        @Override
        public DataSourceFactory getDataSourceFactory(ORMAuditLoggingDemoConfiguration configuration) {
            return configuration.getDataSourceFactory();
        }
    };

    public static void main(final String[] args) throws Exception {
        new ORMAuditLoggingDemoApplication().run(args);
    }

    @Override
    public String getName() {
        return "ORMAuditLoggingDemo";
    }

    @Override
    public void initialize(final Bootstrap<ORMAuditLoggingDemoConfiguration> bootstrap) {
        bootstrap.addBundle(hibernateBundle);
        bootstrap.addBundle(new MigrationsBundle<ORMAuditLoggingDemoConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(ORMAuditLoggingDemoConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });
    }

    @Override
    public void run(final ORMAuditLoggingDemoConfiguration configuration, final Environment environment) {
        final PersonDAO dao = new PersonDAO(hibernateBundle.getSessionFactory());

        final PersonResource pResource = new PersonResource(dao);

        environment.jersey()
                .register(new AuthDynamicFeature(new BasicCredentialAuthFilter.Builder<User>()
                        .setAuthenticator(new ExampleAuthenticator()).setAuthorizer(new ExampleAuthorizer())
                        .setRealm("SUPER SECRET STUFF").buildAuthFilter()));
        environment.jersey().register(RolesAllowedDynamicFeature.class);
        environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
        environment.jersey().register(new ThreadLocalContextFilter());

        environment.jersey().register(new AuditLoggingResponseFilter());
        environment.jersey().register(pResource);
    }

}
