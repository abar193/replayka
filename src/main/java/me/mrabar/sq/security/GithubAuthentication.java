package me.mrabar.sq.security;

import io.quarkus.oidc.IdToken;
import io.quarkus.oidc.RefreshToken;
import org.eclipse.microprofile.jwt.JsonWebToken;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;

@Path("/github")
@Produces(MediaType.TEXT_HTML)
public class GithubAuthentication {
  private static final Logger LOG = Logger.getLogger(GithubAuthentication.class);

  @Inject
  @IdToken
  JsonWebToken idToken;
  @Inject
  JsonWebToken accessToken;
  @Inject
  RefreshToken refreshToken;

  @GET
  public Response githubAuth() {
    LOG.info("WE GOT ONE!!!");
    LOG.info(String.join(", ", idToken.getClaimNames()));
    return Response.seeOther(UriBuilder.fromPath("/internal").build()).entity("GITHUB").build();
  }
}
