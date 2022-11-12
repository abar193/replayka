package me.mrabar.sq.web;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import io.vertx.ext.web.RoutingContext;
import me.mrabar.sq.exception.BlogNotFoundException;
import me.mrabar.sq.exception.ScriptKiddieException;
import me.mrabar.sq.service.GatheringService;
import org.jboss.resteasy.reactive.RestForm;
import org.jboss.resteasy.reactive.RestPath;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

@Path("/f/{key}")
@Produces("text/html")
public class GatheringFormResource {
  @Inject
  GatheringService gatheringService;

  @Inject
  Template form;
  @Inject
  Template ok;
  @Inject
  Template message;

  @GET
  public TemplateInstance getForm(
      @Context HttpHeaders headers,
      @Context RoutingContext context,
      @RestPath String key,
      @QueryParam("page") String page
  ) {
    try {
      var requestId = gatheringService.createRequestId(
          key, page, context.request().host(), headers.getRequestHeaders()
      );
      return form.data("data", requestId);
    } catch (BlogNotFoundException e) {
      return message.data("message", "Invalid blogKey provided. How could this ever happen?!");
    }
  }

  @POST
  public TemplateInstance submit(
      @RestPath String key,
      @RestForm String requestId,
      @RestForm Integer score,
      @RestForm String comments
  ) {
    try {
      validateParams(score, comments);
    } catch (ScriptKiddieException e) {
      return message.data("message", "Oh no, you've totally hacked this service, I surrender!");
    }

    gatheringService.saveFeedback(requestId, score, comments);
    return ok.instance();
  }

  private void validateParams(Integer score, String comments) throws ScriptKiddieException {
    if (score < 0 || score > 10 || comments.length() > 510) {
      throw new ScriptKiddieException();
    }
  }
}
