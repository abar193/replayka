package me.mrabar.sq.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.vertx.ext.web.RoutingContext;
import org.jboss.resteasy.reactive.RestForm;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

@Path("/form")
public class FeedbackResource {
  @Inject
  Template form;

  @GET
  public TemplateInstance getForm(@Context HttpHeaders headers, @Context RoutingContext context) {
    return form.data("data", "Template result!");
  }

  @POST
  public String submit(@Context HttpHeaders headers, @Context RoutingContext context, @RestForm String fav_language) {
    System.out.println(fav_language);
    return "OK";
  }
}
