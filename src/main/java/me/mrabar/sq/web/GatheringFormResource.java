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
import javax.ws.rs.core.MediaType;

@Path("/f/{key}")
@Produces(MediaType.TEXT_HTML)
public class GatheringFormResource {
  @Inject
  GatheringService gatheringService;

  @Inject
  Template form;
  @Inject
  Template ok;

  @GET
  public TemplateInstance getForm(
      @Context HttpHeaders headers,
      @Context RoutingContext context,
      @RestPath String key,
      @QueryParam("page") String page
  ) throws BlogNotFoundException {

    var requestId = gatheringService.createRequestId(
        key, page, context.request().remoteAddress().hostAddress(), gatheringService.cleanHeaders(headers)
    );

    return form.data("data", requestId);
  }

  @POST
  @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
  public TemplateInstance submit(
      @RestPath String key,
      @RestForm String requestId,
      @RestForm Integer score,
      @RestForm String comments
  ) throws ScriptKiddieException {

    validateParams(score, comments);
    gatheringService.saveFeedback(requestId, score, comments);

    return ok.instance();
  }

  private void validateParams(Integer score, String comments) throws ScriptKiddieException {
    if (score < 0 || score > 10 || comments.length() > 500) {
      throw new ScriptKiddieException();
    }
  }
}
