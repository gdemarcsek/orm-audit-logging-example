package com.gdemarcsek.appsec.visibility.demo.core;

import java.net.URI;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import javax.ws.rs.client.Client;
import javax.ws.rs.core.Response;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;


@Builder
@Valid
@Slf4j
public class AuthzPolicyServiceClient<T> {
      private final Client client;
      private final URI serviceURL;

      @Pattern(regexp = "^v[0-9]+$")
      private final String apiVersion;

      @Pattern(regexp = "^[a-z0-9_\\.]+$")
      private final String policyPackage;

      @Data
      class AuthzQueryResult {
            private Boolean allow;
      }

      @Data
      class AuthzResponseMessage {
            private AuthzQueryResult result;
      }

      public boolean check(T request) {
            try {
                  Response response = this.client
                  .target(serviceURL)
                  .path(String.format("/%s/data/%s/authz", this.policyPackage, this.apiVersion))
                  .request(MediaType.APPLICATION_JSON)
                  .post(Entity.json(request));
            
                  AuthzResponseMessage result = (AuthzResponseMessage) response.readEntity(AuthzResponseMessage.class);

                  return result.getResult().getAllow();
            } catch(Exception error) {
                  log.error("Error during executing authorization checks, failing safely...", error);
                  
                  return false;
            }
      }

      public void fail(T request) {
            boolean result = this.check(request);
            if (!result) {
                  // TODO: Replace with something more reasonable
                  throw new RuntimeException("Authorization failed");
            }
      }
}
